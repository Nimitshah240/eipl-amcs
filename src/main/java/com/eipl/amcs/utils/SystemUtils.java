package com.eipl.amcs.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SystemUtils {
    public static String getSystemMAC() {
        StringBuilder sb = new StringBuilder();
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface network = networkInterfaces.nextElement();
                    byte[] mac = network.getHardwareAddress();
                    if (mac != null && (network.getDisplayName().toLowerCase().contains("eth")
                            || network.getName().toLowerCase().contains("eth"))) {
                        // sb = new StringBuilder();
                        // if (mac != null) {
                        for (int i = 0; i < mac.length; i++) {
                            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                        }
                        break;
                    }

                }
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", "ifconfig | grep 'ether'");
                    Process process = processBuilder.start();
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = "";

                    while ((line = br.readLine()) != null) {
                        if (line.contains("eth") && line.contains(" ")) {
                            sb.append(line.split(" ")[1]);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", "ifconfig | grep 'ether'");
                    Process process = processBuilder.start();
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = "";

                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("e") && line.contains("HWaddr")) {
                            sb.append(line.substring(line.indexOf("HWaddr") + 6).trim().replaceAll(":", "-"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static List<String> getAllMac() {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface network = networkInterfaces.nextElement();
                    byte[] mac = network.getHardwareAddress();
                    if (mac != null) {
                        // sb = new StringBuilder();
                        // if (mac != null) {
                        for (int i = 0; i < mac.length; i++) {
                            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                        }
                        list.add(sb.toString());
                        sb = new StringBuilder();
                    }
                }
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", "ifconfig | grep 'ether'");
                    Process process = processBuilder.start();
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = "";

                    while ((line = br.readLine()) != null) {
                        if (line.contains("eth") && line.contains(" ")) {
                            sb.append(line.split(" ")[1]);
                            list.add(sb.toString());
                            sb = new StringBuilder();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", "ifconfig | grep 'ether'");
                    Process process = processBuilder.start();
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = "";

                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("e") && line.contains("HWaddr")) {
                            sb.append(line.substring(line.indexOf("HWaddr") + 6).trim().replaceAll(":", "-"));
                            list.add(sb.toString());
                            sb = new StringBuilder();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return list;
    }
}
