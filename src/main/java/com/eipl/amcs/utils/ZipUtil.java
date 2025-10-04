package com.eipl.amcs.utils;

import java.io.File;
import java.io.IOException;

public class ZipUtil {

    public static boolean executeZipArchiveCommand(File source, File target, String pass) {
        try {
            String osname = System.getProperty("os.name");
            ProcessBuilder processBuilder = null;
            if (osname.contains("win") || osname.contains("Win")) {
                File commandExe = new File("resources/cmd");
                String xx = "cd \"" + commandExe.getAbsolutePath() + "\" && 7za.exe a \"" + target.getAbsolutePath() + "\" -p"
                        + pass + " \"" + source.getAbsolutePath();
                processBuilder = new ProcessBuilder("cmd.exe", "/c",
                        xx);
            } else {
                processBuilder = new ProcessBuilder("/bin/bash", "-c", "7za a " + target.getAbsolutePath() + " -p" + pass
                        + " " + source.getAbsolutePath());
            }
            Process process;

            process = processBuilder.start();
            if (process.waitFor() == 0) {
                return true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
