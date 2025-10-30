package com.eipl.amcs.operation.procurement.serial;

import com.eipl.amcs.master.procurement.model.HardwareDevice;
import com.eipl.amcs.operation.procurement.serial.exception.CommportNotBindException;
import com.eipl.amcs.operation.procurement.serial.exception.DeviceNotFoundException;
import com.eipl.amcs.operation.procurement.serial.exception.UnableToOpenSerialPort;
import com.eipl.amcs.utils.AppConstant;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class WsSerial implements SerialPortDataListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(WsSerial.class);
    private static String response = "";
    private final HardwareDevice hardwareDevice;
    private final StringBuffer readBuffer = new StringBuffer();
    private final char endChar;
    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean isDeviceReady = false;
    private DeviceCallback callback;

    public WsSerial(HardwareDevice hardwareDevice, String commPort, DeviceCallback callback) {
        this.hardwareDevice = hardwareDevice;
        this.callback = callback;

        if (this.hardwareDevice == null)
            throw new DeviceNotFoundException();

        if (commPort == null)
            throw new CommportNotBindException();

        if (hardwareDevice.getEndChar() == null || hardwareDevice.getEndChar().isEmpty()) {
            this.endChar = '\n';
        } else if (hardwareDevice.getEndChar().contains("n")) {
            this.endChar = '\n';
        } else if (hardwareDevice.getEndChar().contains("r")) {
            this.endChar = '\r';
        } else {
            this.endChar = hardwareDevice.getEndChar().toCharArray()[0];
        }

        for (SerialPort port : SerialPort.getCommPorts()) {
            if (port.getSystemPortName().equalsIgnoreCase(commPort)) {
                serialPort = port;
                serialPort.setBaudRate(hardwareDevice.getBaudRate());
                serialPort.setNumDataBits(hardwareDevice.getBitRate());
                serialPort.setNumStopBits(hardwareDevice.getStopBit());
                serialPort.setParity(hardwareDevice.getParity());
                serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 1000, 1000);
                serialPort.addDataListener(this);
                break;
            }
        }

        if (!serialPort.openPort())
            throw new UnableToOpenSerialPort();
        if (serialPort.isOpen()) {
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            isDeviceReady = true;
        }
    }

    public void setCallback(DeviceCallback callback) {
        this.callback = callback;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
            return;

        int c;
        byte[] readData = new byte[1];
        try {
            while (inputStream.available() != 0) {
                c = inputStream.read(readData);
                for (int i = 0; i < c; i++) {
                    getResponse((char) readData[i]);
                    LOGGER.info("WS Reading char {}", (char) readData[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getResponse(char c) {
        if (endChar == c && readBuffer.toString().length() >= hardwareDevice.getLength()) {
            response = readBuffer.toString().trim();
            String data = "";
            if (!response.startsWith("-")) {
                data = response;
            } else {
                if (response.contains("00000")) {
                    data = response.replace("-", "");
                } else {
                    data = "";
                }
            }
            if (data.equals("")) {
                readBuffer.setLength(0);
                response = "";
                return;
            }

            if (hardwareDevice.getDiscardChars() != null && !hardwareDevice.getDiscardChars().isEmpty()) {
                if (hardwareDevice.getDiscardChars().contains("#")) {
                    for (String s : hardwareDevice.getDiscardChars().split("#"))
                        data = data.replace(s, "");
                } else {
                    data = data.replace(hardwareDevice.getDiscardChars(), "");
                }
            }
            LOGGER.info("WS SERIAL DATA: {}", data);
            Map<String, String> list = ResponseParserUtilNew.parse(data, hardwareDevice.getRegEx());
            if (list != null && !list.isEmpty()) {
                if (callback != null)
                    callback.onResponseFromDevice(list, AppConstant.DEVICE_TAG.WS_TAG);
                readBuffer.setLength(0);
                response = "";
            }
        } else {
            readBuffer.append(c);
        }

    }

    public void tareWs() {
        if (hardwareDevice.getTareChar() == null || hardwareDevice.getTareChar().isEmpty())
            return;
        try {
            if (outputStream != null) {
                if (hardwareDevice.getTareChar().length() > 1 && hardwareDevice.getTareChar().contains("#")) {
                    for (String str : hardwareDevice.getTareChar().split("#")) {
                        if (str.contains("\\r"))
                            outputStream.write(0xd);
                        else if (str.contains("\\n"))
                            outputStream.write(0xa);
                        else if (str.contains("\\t"))
                            outputStream.write(0x9);
                        else if (str.contains("\\f"))
                            outputStream.write(0xc);
                        else if (str.contains("\\b"))
                            outputStream.write(0x8);
                        else if (str.contains("ESC"))
                            outputStream.write(0x1b);
                        else
                            outputStream.write(str.getBytes());
                    }
                    outputStream.flush();
                } else {
                    outputStream.write(hardwareDevice.getTareChar().getBytes());
                    outputStream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        serialPort.removeDataListener();
        serialPort.closePort();
    }
}
