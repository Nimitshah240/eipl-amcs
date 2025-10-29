package com.eipl.amcs.base.model;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.task.DbBackupTask;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

public class DownloadFileTask extends Task<Map<String, Object>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadFileTask.class);
    public String url;

    public DownloadFileTask(String url) {
        this.url = url;
    }

    @Override
    protected Map<String, Object> call() throws Exception {

        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            // Optional Accept header
            RequestCallback requestCallback = request -> request.getHeaders().setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));

            // Streams the response instead of loading it all in memory
            ResponseExtractor<Void> responseExtractor = response -> {
                File file = new File("resources/appupdate/" + LocalDate.now().toString().replace("-", ""));
                file.mkdirs();
                File file2 = new File(file, "update.zip");

                Files.copy(response.getBody(), Path.of(file2.getAbsolutePath()));
                System.out.println("Downloaded");
                startUpdateProcess(file2);
                return null;
            };
            restTemplate.execute(url, HttpMethod.GET, requestCallback, responseExtractor);
            return null;
        } catch (Exception e) {
            LOGGER.error("Updater Information Failed", e);
        }
//        startUpdateProcess(new File("resources/appupdate/20221018/update.zip"));
        return null;
    }

    private void startUpdateProcess(File appZipFile) {


        try {
            System.out.println("Starting update process...");
            String backupPath = MainApp.getProperty("backuppath", null);
            if (backupPath != null) {
                backupPath = backupPath.replace(" ", "");
            }
            var task = new DbBackupTask(backupPath);
            task.setOnSucceeded(e -> {
                System.out.println("Backup Done");
            });
            new Thread(task).start();


        } catch (Exception e) {
            e.printStackTrace();
        }


        File currentLocation = new File("");
        if (currentLocation != null && !currentLocation.getAbsolutePath().isEmpty() && appZipFile != null) {
            String dirName = UUID.randomUUID().toString();
            File tempDir = new File(System.getProperty("java.io.tmpdir") + dirName);
            tempDir.mkdir();
            String osname = System.getProperty("os.name");
            if (osname.contains("win") || osname.contains("Win")) {
                ProcessBuilder processBuilder = null;
                Process p = null;
                File commandExe = new File("resources/cmd");
                processBuilder = new ProcessBuilder("cmd.exe", "/c", "cd \"" + commandExe.getAbsolutePath().replace("\\", "\\\\") + "\" && 7za.exe x \"" + appZipFile.getAbsolutePath().replace("\\", "\\\\") + "\" -o\"" + (tempDir.getAbsolutePath() + File.separator).replace("\\", "\\\\") + "\"");

                try {
                    p = processBuilder.start();
                    if (p.waitFor() == 0) {

                        String appPath = System.getProperty("user.home") + "\\eipl-amcs\\application";
                        File appDirPath = new File(appPath);
                        Path liquibase2 = Paths.get(String.valueOf(appDirPath), "liquibase2");

                        String version = null;
                        for (File f : tempDir.listFiles()) {
                            if (f.getName().endsWith(".jar")) {
                                try {
                                    if (f.getName().startsWith("eipl-amcs")) { // app jars
                                        try {
//                                        eipl-amcs-1.0
//                                        eipl-amcs-boot-1.0
                                            if (!f.getName().startsWith("eipl-amcs-boot"))
                                                version = f.getName().substring(10, 13);
                                            Files.copy(f.toPath(), new File(appDirPath, f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                        } catch (IndexOutOfBoundsException e) {
                                            version = null;
                                        }
                                    } else { // lib
                                        try {
                                            File libDir = new File(appDirPath, "lib");
                                            Files.copy(f.toPath(), new File(libDir, f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }


                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }


                            } else if (f.getName().endsWith(".xml")) {
                                try {
                                    File libDir = null;
                                    if (Files.isDirectory(liquibase2)) {
                                        libDir = new File(appDirPath, "liquibase2/db-repository");
                                    } else {
                                        libDir = new File(appDirPath, "liquibase/db-repository");
                                    }
                                    Files.copy(f.toPath(), new File(libDir, f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (f.getName().equalsIgnoreCase("SP.sql") || f.getName().equalsIgnoreCase("Device.sql")) {
                                try {
                                    Files.copy(f.toPath(), new File(appDirPath, f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else if (f.getName().equalsIgnoreCase("account.sql")) {
                                try {
                                    Files.copy(f.toPath(), new File(appDirPath, f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else if (f.getName().endsWith(".csv") || f.getName().endsWith(".sql")) {
                                try {// liquibase2 initial data
                                    File libDir = null;
                                    if (Files.isDirectory(liquibase2)) {
                                        libDir = new File(appDirPath, "liquibase2/db-repository/initial-data");
                                    } else {
                                        libDir = new File(appDirPath, "liquibase/db-repository/initial-data");
                                    }
                                    Files.copy(f.toPath(), new File(libDir, f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (f.getName().endsWith(".jasper")) {
                                try {
                                    // report
                                    File libDir = new File(appDirPath, "resources/report/milkcollection");
                                    Files.copy(f.toPath(), new File(libDir, f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (f.getName().endsWith(".properties")) {
                                try {
                                    // message prop
                                    File libDir = new File(appDirPath, "resources/messages");
                                    Files.copy(f.toPath(), new File(libDir, f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            } else if (f.getName().endsWith(".ttf")) {
                                try {
                                    // message prop
                                    File libDir = new File(appDirPath, "resources/noto");
                                    Files.copy(f.toPath(), new File(libDir, f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            } else if ((f.getName().startsWith("PrintSlip") || f.getName().startsWith("BonusSlip")) && f.getName().endsWith(".txt")) {
                                try {
                                    // message prop
                                    File libDir = new File(appDirPath, "resources/collection");
                                    Files.copy(f.toPath(), new File(libDir, f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            } else if (f.getName().endsWith(".jrxml")) {
                                // message prop
                                continue;
                            } else if (f.getName().endsWith(".bat")) {
                                try {
                                    Files.copy(f.toPath(), new File(appDirPath, f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            } else {
                                try {
                                    // guj
                                    File libDir = new File(appDirPath, "resources/messages");
                                    Files.copy(f.toPath(), new File(libDir, f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            }
                        }

                        //SP update
                        Process process1 = Runtime.getRuntime().exec("cmd /c SP.bat", null, appDirPath);
                        int res1 = process1.waitFor();
                        System.out.println("SP res: " + res1);

                        System.out.println("liquibase start");

                        //liquibase2 update
//                        File liquibaseDbRepo = new File(appDirPath, "liquibase2/db-repository");
//                        Process process = Runtime.getRuntime().exec("cmd /c ..\\liquibase update", null, liquibaseDbRepo);
//                        int res = process.waitFor();
//                        System.out.println("Liquibase res: " + res);

                        File liquibaseDbRepo = null;
                        if (Files.isDirectory(liquibase2)) {
                            liquibaseDbRepo = new File(appDirPath, "liquibase2/db-repository");
                        } else {
                            liquibaseDbRepo = new File(appDirPath, "liquibase/db-repository");
                        }

// Run 'liquibase clearCheckSums'
                        Process clearProcess = Runtime.getRuntime().exec("cmd /c ..\\liquibase clearCheckSums", null, liquibaseDbRepo);
                        int clearRes = clearProcess.waitFor();
                        System.out.println("Liquibase clearCheckSums result: " + clearRes);

// Run 'liquibase update'
                        Process updateProcess = Runtime.getRuntime().exec("cmd /c ..\\liquibase update", null, liquibaseDbRepo);
                        int updateRes = updateProcess.waitFor();
                        System.out.println("Liquibase update result: " + updateRes);

                        //Device
                        Process process2 = Runtime.getRuntime().exec("cmd /c Device.bat", null, appDirPath);
                        int res2 = process2.waitFor();
                        System.out.println("DEVICE res: " + res2);


                        //account script
                        Process process3 = Runtime.getRuntime().exec("cmd /c account.bat", null, appDirPath);
                        int res3 = process3.waitFor();
                        System.out.println("account res: " + res3);

                        try {
                            if (version != null && !version.isEmpty()) {
                                List<String> lines = Files.readAllLines(new File("resources/app.properties").toPath());
                                List<String> newLines = new ArrayList<>();
                                for (String line : lines) {
                                    if (line.contains("identity.version")) {
                                        System.out.println(version);
                                        newLines.add("identity.version=" + new String(Base64.getEncoder().encode(version.getBytes())));
                                        System.out.println("app pro changed");
                                    } else {
                                        newLines.add(line);
                                    }
                                }
                                File appPro = new File("resources/app.properties");
                                Files.write(appPro.toPath(), newLines);
                                File libDir = new File(appDirPath, "resources");
                                Files.copy(appPro.toPath(), new File(libDir, appPro.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);

                            }
                        } catch (Exception ex1) {
                            ex1.printStackTrace();
                        }
                    }
                } catch (Exception ee) {
                    System.out.println(ee.getMessage());
                    ee.printStackTrace();
                }
            }
        }
    }
}