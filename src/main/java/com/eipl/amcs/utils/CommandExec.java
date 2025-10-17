package com.eipl.amcs.utils;

import com.eipl.amcs.base.SplashController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandExec {
    private static final Logger LOGGER = LoggerFactory.getLogger(SplashController.class);

    public static int exec(String command) {
        try {
            ProcessBuilder processBuilder = null;
            String osname = System.getProperty("os.name");
            if (osname.contains("win") || osname.contains("Win"))
                processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            else
                processBuilder = new ProcessBuilder("/bin/bash", "-c", command);
            Process p = processBuilder.start();
            return p.waitFor();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return -1;
        }
    }
}
