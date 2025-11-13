package com.eipl.amcs.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class ProcessUtil {

    private static Logger logger = LogManager.getLogger(ProcessUtil.class.getName());

    public static boolean executeScriptFile(File file) {
        try {
            String mysqlDumpCmd = "mysql --user=root -p" + AppConstant.EIPL_DB_PASS + " --port=3366 -f --default-character-set=utf8 --database=" + AppConstant.EIPL_DB_NAME + " < "
                    + file.getAbsolutePath();
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", mysqlDumpCmd);
            Process p = processBuilder.start();
            int statusCode = p.waitFor();
            return statusCode == 0;
        } catch (Exception e) {
            logger.catching(e);
        }
        return false;
    }
}