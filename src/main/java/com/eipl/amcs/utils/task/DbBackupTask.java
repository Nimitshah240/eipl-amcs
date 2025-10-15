package com.eipl.amcs.utils.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.sync.producer.BroadcastedService;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommandExec;
import com.eipl.amcs.utils.ZipUtil;
import javafx.concurrent.Task;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DbBackupTask extends Task<Boolean> {
    private String path;

    public DbBackupTask(String path) {
        this.path = path;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            //Send All Broadcasted In One

            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            int timeout = 60000;

            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(timeout);
            requestFactory.setReadTimeout(timeout);
            restTemplate.setRequestFactory(requestFactory);

            BroadcastedService broadcastedService = EmcsAppContext.getContext().getBean(BroadcastedService.class);
            broadcastedService.sendBroadcastedAll();


            String filename;
            if (LocalDateTime.now().getHour() < 12) {
                filename = MainApp.identityDto.getSociety().getCode() + "-" +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + "M.sql";
            } else {

                filename = MainApp.identityDto.getSociety().getCode() + "-" +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + "E.sql";
            }
            String mysqlPath = System.getProperty("user.home") + "\\eipl-amcs\\mysql\\bin";
            String command = null;
            File file = new File(mysqlPath);
            File file1 = new File(path);
            if (!file1.exists())
                file1.mkdir();
            if (file.exists()) {
                command = "cd " + mysqlPath + " && mysqldump --routines -u root -p" + AppConstant.EIPL_DB_PASS + " --port=3366 --host=localhost --add-drop-database " +
                        "--complete-insert=TRUE --default-character-set=utf8 --single-transaction=TRUE  --databases \"" + AppConstant.EIPL_DB_NAME + "\" --result-file=" +
                        path + File.separator + filename;
            } else {
                command = "mysqldump --routines -u root -p" + AppConstant.EIPL_DB_PASS + " --port=3366 --host=localhost --add-drop-database " +
                        "--complete-insert=TRUE --default-character-set=utf8 --single-transaction=TRUE  --databases \"" + AppConstant.EIPL_DB_NAME + "\" --result-file=" +
                        path + File.separator + filename;
            }
//            zip(path + File.separator + filename);

            if (command == null)
                return false;

            if (CommandExec.exec(command) == 0) {
                File f = new File(path + File.separator + filename);
                File f1 = new File(path + File.separator + filename + ".zip");
                ZipUtil.executeZipArchiveCommand(f, f1, "EAmcs2022");
                f.delete();
                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
