package com.eipl.amcs.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProcessUtil {

    private static Logger logger = LogManager.getLogger(ProcessUtil.class.getName());

    public static boolean executeScriptFile(File file) {
        try {
            String mysqlDumpCmd = "mysql --user=root -p" + AppConstant.EIPL_DB_PASS + " --port=3366 -f --default-character-set=utf8 --database=" + AppConstant.EIPL_DB_NAME + " < "
                    + file.getAbsolutePath();
            if (processSqlFile(mysqlDumpCmd))
                return true;

            Path currentDir = Paths.get(System.getProperty("user.dir"),
                    "resources",
                    "mysql"
            );
            String mysqlExecutablePath = Paths.get(currentDir.toString(), "mysql.exe").toString();
            mysqlDumpCmd = "\"" + mysqlExecutablePath + "\"  --user=root -p" + AppConstant.EIPL_DB_PASS + " --port=3366 -f --default-character-set=utf8 --database=" + AppConstant.EIPL_DB_NAME + " < "
                    + file.getAbsolutePath();
            return processSqlFile(mysqlDumpCmd);

        } catch (Exception e) {
            logger.catching(e);
        }
        return false;
    }

    private static boolean processSqlFile(String mysqlDumpCmd) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", mysqlDumpCmd);
            Process p = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("Process Output: {}", line);
                }
            }
            int statusCode = p.waitFor();
            return statusCode == 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

}