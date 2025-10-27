package com.eipl.amcs.operation.procurement.serial;

import com.eipl.amcs.master.procurement.model.HardwareDevice;
import com.eipl.amcs.operation.procurement.serial.exception.CommportNotBindException;
import com.eipl.amcs.operation.procurement.serial.exception.DeviceNotFoundException;
import com.eipl.amcs.operation.procurement.serial.exception.UnableToOpenSerialPort;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;

public class DisplaySerial implements SerialPortDataListener {
    private HardwareDevice hardwareDevice;
    private SerialPort serialPort;
    private OutputStream outputStream;
    private boolean isDeviceReady = false;

    private DecimalFormat qtyDecimalFormat;
    private DecimalFormat wgtDecimalFormat, rateDecimalFormat;

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplaySerial.class);

    public DisplaySerial(HardwareDevice hardwareDevice, String commPort) {
        this.hardwareDevice = hardwareDevice;
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
                serialPort.addDataListener(this);
                break;
            }
        }

        if (serialPort != null && !serialPort.openPort())
            throw new UnableToOpenSerialPort();
        if (serialPort != null && serialPort.isOpen()) {
            outputStream = serialPort.getOutputStream();
            isDeviceReady = true;

            qtyDecimalFormat = new DecimalFormat("00.0");
            wgtDecimalFormat = new DecimalFormat("00000.00");
            rateDecimalFormat = new DecimalFormat("00.00");

        }
    }

    public boolean isDeviceReady() {
        return isDeviceReady;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
            return;
    }

    public void sendCommand(String command) {
        if (command == null) {
            return;
        }
        LOGGER.info("Display SERIAL {}", command);
        try {
            if (hardwareDevice.getxCol1().equals("1")) {
                for (char ch : command.toCharArray()) {
                    outputStream.write(String.valueOf(ch).getBytes());
                    outputStream.flush();
                }
            } else {
                outputStream.write(command.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
        }
    }

    public void disconnect() {
        if (serialPort != null) {
            serialPort.closePort();
            serialPort.removeDataListener();
        }
    }

    public void resetDisplay() {
        try {
            String command = null;
            // For Everest New Model Display only!
            if (hardwareDevice.getxCol1().equals("1")) {
                command = "(H)";
            }

            if (command != null && !command.isEmpty()) {
                outputStream.write(command.getBytes());
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void displayRtpl(String command) {
        try {
            if (command != null) {
                if (hardwareDevice.getxCol1().equals("2")) {
                    for (char ch : command.toCharArray()) {
                        outputStream.write(String.valueOf(ch).getBytes());
                        outputStream.flush();
                    }
                } else {
                    outputStream.write(command.getBytes());
                    outputStream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayAmount(String command) {
        try {
            if (command != null) {
                if (hardwareDevice.getxCol1().equals("2")) {
                    for (char ch : command.toCharArray()) {
                        outputStream.write(String.valueOf(ch).getBytes());
                        outputStream.flush();
                    }
                } else {
                    outputStream.write(command.getBytes());
                    outputStream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void displayQaulityParam(String command) {
        try {
            if (command != null) {
                if (hardwareDevice.getxCol1().equals("2")) {
                    for (char ch : command.toCharArray()) {
                        outputStream.write(String.valueOf(ch).getBytes());
                        outputStream.flush();
                    }
                } else {
                    outputStream.write(command.getBytes());
                    outputStream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayQuantity(String command) {
        try {
            if (command != null) {
                if (hardwareDevice.getxCol1().equals("2")) {
                    for (char ch : command.toCharArray()) {
                        outputStream.write(String.valueOf(ch).getBytes());
                        outputStream.flush();
                    }
                } else {
                    outputStream.write(command.getBytes());
                    outputStream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayAnimalType(String command) {
        try {
            if (command != null) {
                if (hardwareDevice.getxCol1().equals("2")) {
                    for (char ch : command.toCharArray()) {
                        outputStream.write(String.valueOf(ch).getBytes());
                        outputStream.flush();
                    }
                } else {
                    outputStream.write(command.getBytes());
                    outputStream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DecimalFormat getWgtDecimalFormat() {
        return wgtDecimalFormat;
    }

    public DecimalFormat getQtyDecimalFormat() {
        return qtyDecimalFormat;
    }

    public DecimalFormat getRateDecimalFormat() {
        return rateDecimalFormat;
    }


    public HardwareDevice getDispDevice() {
        return hardwareDevice;
    }
}
