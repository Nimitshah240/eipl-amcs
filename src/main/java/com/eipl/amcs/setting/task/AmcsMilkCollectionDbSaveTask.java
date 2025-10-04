package com.eipl.amcs.setting.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.operation.procurement.dto.CollectionImportDto;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.task.MilkCollectionListSaveTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
//import net.ucanaccess.console.Main;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class AmcsMilkCollectionDbSaveTask extends Task<Boolean> {
    private List<MilkType> milkTypeList;
    private List<Shift> shiftList;
    private List<Member> memberList;
    private String filePath;
    private String cowRange;
    private String buffRange;
    List<MilkCollection> collectionList = new ArrayList<>();

    public AmcsMilkCollectionDbSaveTask(List<MilkType> milkTypeList, List<Shift> shiftList, String filePath, String cowRange, String buffRange, List<Member> memberList) {
        this.milkTypeList = milkTypeList;
        this.shiftList = shiftList;
        this.memberList = memberList;
        this.filePath = filePath;
        this.cowRange = cowRange;
        this.buffRange = buffRange;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            Map<String, Integer> mapShift = new HashMap<>();
            for (Shift shift : shiftList) {
                mapShift.put(shift.getName().substring(0, 1).toUpperCase(), shift.getCode());
                mapShift.put(shift.getCode().toString(), shift.getCode());
            }
            Map<String, Integer> mapMilkType = new HashMap<>();
            for (MilkType milkType : milkTypeList) {
                mapMilkType.put(milkType.getName().toUpperCase().substring(0, 1), milkType.getCode());
            }
            try (Stream<String> lines = Files.lines(new File(filePath).toPath(), Charset.forName("UTF-8"))) {
                lines.forEach(line -> {
                    MilkCollection mc = new MilkCollection();
//                    String line1 = new String(Base64.getDecoder().decode(line.getBytes()));
                    String line1 = line;
                    String[] arr = line1.split("#");

                    Member member = memberList.stream().filter(p -> arr[23].equals(p.getCode())).findAny().orElse(null);
                    mc.setMember(member);


                    mc.setFat(new BigDecimal(arr[2]));
                    mc.setSnf(new BigDecimal(0));
                    mc.setQty(new BigDecimal(arr[10]));
                    mc.setAmount(new BigDecimal(arr[11]));
                    mc.setRtpl(new BigDecimal(arr[9]));
                    mc.setRateCode(arr[17]);

                    mc.setDensity(BigDecimal.ZERO);
                    mc.setLectose(BigDecimal.ZERO);
                    mc.setProtein(BigDecimal.ZERO);
                    mc.setClr(BigDecimal.ZERO);


                    mc.setWeightAuto(arr[12].equalsIgnoreCase("1"));
                    mc.setQualityAuto(arr[13].equalsIgnoreCase("1"));
                    mc.setAvgParam(arr[14].equalsIgnoreCase("1"));
                    mc.setUnionCode(MainApp.identityDto.getUnion().getCode());
                    mc.setSociety(MainApp.identityDto.getSociety());
                    mc.setDock(MainApp.identityDto.getDock());


                    mc.setQtyMode(CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.MEMBER_COLLECTION_QTY_MODE, "0")));
                    mc.setConvertedQtyMode(mc.getQtyMode() == 1 ? 0 : 1);
                    mc.setConvertedQty(CommonUtils.convertQty(AppConstant.CollectionType.MEMBER_COLL, arr[5]));


                    String shiftStr = arr[24];
                    Shift shift = null;
                    if (shiftStr == null || shiftStr.isEmpty()) shift = shiftList.get(0);
                    else {
                        shift = shiftList.stream().filter(p -> p.getCode() == Integer.parseInt(shiftStr)).findAny().orElse(null);
                        if (shift == null) shift = shiftList.get(0);
                    }
                    mc.setShift(shift);

                    mc.setCollectionDate(LocalDateTime.parse(arr[1]));


                    String milkTypeStr = arr[25];
                    MilkType milkType = null;
                    if (milkTypeStr == null || milkTypeStr.isEmpty()) milkType = milkTypeList.get(0);
                    else {
                        milkType = milkTypeList.stream().filter(p -> p.getCode() == Integer.parseInt(milkTypeStr)).findAny().get();
                        if (milkType == null) milkType = milkTypeList.get(0);
                    }
                    mc.setMilkType(milkType);


                    mc.setSampleNo(CommonUtils.strToInteger(arr[0]));
                    mc.setxCol1(arr[29]);
                    mc.setxCol2(arr[30]);
                    mc.setxCol3(arr[31]);

                    collectionList.add(mc);
                });
            }
            startImportProcess();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startImportProcess() {
        var task = new MilkCollectionListSaveTask(collectionList);
        task.setOnSucceeded(e -> {
            try {
                MainApp.paneDrop.setVisible(false);
                List<CollectionImportDto> list = task.get();
                if (list == null || list.isEmpty()) {
                    MyAlert alert = new WarningAlert(MainApp.getStage(), "Milk Collection", "Error");
                    alert.createAlert();
                    return;
                }
                StringBuilder builder = new StringBuilder();
                builder.append("Import success: ");
                builder.append(list.stream().filter(p -> p.getStatus().equalsIgnoreCase("success")).count());
                builder.append("\n");
                builder.append("Import fail: ");
                builder.append(list.stream().filter(p -> p.getStatus().equalsIgnoreCase("error")).count());
                builder.append("\n");

                MyAlert alert = new InformationAlert(MainApp.getStage(), "Milk Collection", builder.toString());
                alert.createAlert();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
