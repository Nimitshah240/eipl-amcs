package com.eipl.amcs.operation.procurement.serial;

import com.eipl.amcs.master.procurement.model.HardwareDevice;
import com.eipl.amcs.operation.procurement.serial.exception.CommportNotBindException;
import com.eipl.amcs.operation.procurement.serial.exception.DeviceNotFoundException;
import com.eipl.amcs.operation.procurement.serial.exception.UnableToOpenSerialPort;
import com.eipl.amcs.utils.CommonUtils;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class AnalyserSerial implements SerialPortDataListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyserSerial.class);
    private final HardwareDevice hardwareDevice;
    private final String tag;
    int countToPick = 3;
    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String response = "";
    private String tempResp = "";
    private boolean isDeviceReady = false;
    private DeviceCallback callback;
    private char endChar;
    private boolean isParsing = false;

    public AnalyserSerial(HardwareDevice hardwareDevice, String commPort, String tag, DeviceCallback callback) {
        this.hardwareDevice = hardwareDevice;
        this.callback = callback;
        this.tag = tag;

        if (this.hardwareDevice == null)
            throw new DeviceNotFoundException();

        if (commPort == null)
            throw new CommportNotBindException();

        for (SerialPort port : SerialPort.getCommPorts()) {
            if (port.getSystemPortName().equalsIgnoreCase(commPort)) {
                serialPort = port;
                serialPort.setBaudRate(hardwareDevice.getBaudRate());
                serialPort.setNumDataBits(hardwareDevice.getBitRate());
                serialPort.setNumStopBits(hardwareDevice.getStopBit());
                serialPort.setParity(hardwareDevice.getParity());
                serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 1000, 1000);
                serialPort.addDataListener(this);
                LOGGER.info("Analyser Serial Port {}", serialPort.toString());
                break;
            }
        }

        if (hardwareDevice.getEndChar() != null && !hardwareDevice.getEndChar().isEmpty()) {
            if (hardwareDevice.getEndChar().contains("n")) {
                this.endChar = '\n';
            } else if (hardwareDevice.getEndChar().contains("r")) {
                this.endChar = '\r';
            } else {
                this.endChar = hardwareDevice.getEndChar().toCharArray()[0];
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
        try {
            if (inputStream.available() != -1 && serialPort.bytesAvailable() > 0) {
                byte[] newData = new byte[serialPort.bytesAvailable()];
                int c = inputStream.read(newData);
                for (int i = 0; i < c; i++) {
                    tempResp += String.valueOf((char) newData[i]);
                    LOGGER.info("Analyser Resp {}-{}", this.tag, tempResp);
                    if ((char) newData[i] == endChar) {
                        if (tempResp.length() >= hardwareDevice.getLength()) {
                            response = tempResp.substring(0, tempResp.indexOf(String.valueOf(endChar)));
                            LOGGER.info("Analyser Temp Resp: {}-{}", this.tag, response);
                            replaceStartEndDiscardChars();
                            tempResp = "";
                            if (!isParsing)
                                getResponse();
                            else
                                response = "";
                        } else {
                            tempResp = "";
                        }
                    } else {
                        if (tempResp.length() > hardwareDevice.getLength() * 2) {
                            tempResp = "";
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private void replaceStartEndDiscardChars() {
        if (hardwareDevice.getSplitChars() != null && !hardwareDevice.getSplitChars().isEmpty()) {
            preCheckForSeparatorLogic();
        }
        if (hardwareDevice.getStartChar() != null && !hardwareDevice.getStartChar().isEmpty()) {
            response = response.replace(hardwareDevice.getStartChar(), "");
        }
        response = response.replace(String.valueOf(endChar), "");
        if (hardwareDevice.getDiscardChars() != null && !hardwareDevice.getDiscardChars().isEmpty()) {
            response = response.replace("\r", "");
            response = response.replace("\n", "");
            for (String s : hardwareDevice.getDiscardChars().split("#"))
                response = response.replace(s, "");
        }
    }

    private void preCheckForSeparatorLogic() {
        int i = 1;
        int added = 0;
        int offset = CommonUtils.strToInteger(hardwareDevice.getxCol1());
        String str = "";
        for (String token : response.split(hardwareDevice.getSplitChars())) {
            if (i >= offset && !token.isEmpty()) {
                str = str + token;
                added++;
                if (added == countToPick)
                    break;
            }
            i++;
        }
        response = str;
    }

    public void getResponse() {
        String data = response;
        response = "";
        isParsing = true;
        Map<String, String> list = ResponseParserUtilNew.parse(data, hardwareDevice.getRegEx());
        if (list != null && !list.isEmpty()) {
            LOGGER.info("Analyser SERIAL DATA: {}-{}", this.tag, data);
            if (callback != null)
                callback.onResponseFromDevice(list, this.tag);
            response = "";
        }
        isParsing = false;
    }

    public void tareAnalyzer() {
        LOGGER.info(
                "Analyzer Tare Serial: Tate-{}", hardwareDevice.getTareChar());
        if (hardwareDevice.getTareChar() != null && !hardwareDevice.getTareChar().isEmpty() && outputStream != null) {
            try {
                outputStream.write(hardwareDevice.getTareChar().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        serialPort.removeDataListener();
        serialPort.closePort();
    }
}
