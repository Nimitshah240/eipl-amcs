package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateBased;
import com.eipl.amcs.operation.procurement.dto.HardwareSetting;
import com.eipl.amcs.operation.procurement.dto.MilkCollectionPreReqDto;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.serial.*;
import com.eipl.amcs.setting.controller.HardwareDeviceConfigurationController;
import com.eipl.amcs.setting.model.HardwareDeviceConfig;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

public abstract class MilkCollectionBaseController implements DeviceCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkCollectionBaseController.class);
    protected static BigDecimal weightLock = BigDecimal.ZERO;
    protected final int SCALE = 2;
    protected final RoundingMode ROUND = RoundingMode.HALF_UP;
    protected MilkCollectionPreReqDto collectionPreReqDto;
    protected MemberMilkPurchaseRate memberMilkPurchaseRate;
    protected List<MemberMilkPurchaseRateBased> memberRateBasedList;
    protected Map<String, BigDecimal> mapRateDetails;
    protected String collectionType;
    protected MilkCollection collection;
    protected LocalDateTime collectionDate;
    protected boolean qualityAuto = false;
    protected boolean weightAuto = false;
    protected boolean autoTare = false;
    protected int maSetting = 1;
    // String prop for fat snf values
    protected StringProperty fatStringProp = new SimpleStringProperty();
    protected StringProperty snfStringProp = new SimpleStringProperty();
    protected int analyserCount = 0;
    protected boolean analyserSeq = true;
    protected Map<String, Integer> analyserMilkType = new TreeMap<>();
    protected Map<Integer, String> analyserMilkTypeMapping = new HashMap<>();
    protected Map<Integer, String> analyserMilkTypeMappingLastSavedFrom = new HashMap<>();
    protected BigDecimal tempQty = BigDecimal.ZERO;
    protected BigDecimal tempFat = BigDecimal.ZERO;
    protected BigDecimal tempSnf = BigDecimal.ZERO;
    protected BigDecimal tempWater = BigDecimal.ZERO;
    protected BigDecimal tempFat2 = BigDecimal.ZERO;
    protected BigDecimal tempSnf2 = BigDecimal.ZERO;
    protected BigDecimal tempWater2 = BigDecimal.ZERO;
    protected BigDecimal tempFat3 = BigDecimal.ZERO;
    protected BigDecimal tempSnf3 = BigDecimal.ZERO;
    protected BigDecimal tempWater3 = BigDecimal.ZERO;
    protected BigDecimal tempFat4 = BigDecimal.ZERO;
    protected BigDecimal tempSnf4 = BigDecimal.ZERO;
    protected BigDecimal tempWater4 = BigDecimal.ZERO;
    protected BigDecimal prevFat1 = BigDecimal.ZERO;
    protected BigDecimal prevSnf1 = BigDecimal.ZERO;
    protected BigDecimal prevFat2 = BigDecimal.ZERO;
    protected BigDecimal prevSnf2 = BigDecimal.ZERO;
    protected BigDecimal prevFat3 = BigDecimal.ZERO;
    protected BigDecimal prevSnf3 = BigDecimal.ZERO;
    protected BigDecimal prevFat4 = BigDecimal.ZERO;
    protected BigDecimal prevSnf4 = BigDecimal.ZERO;
    String mapKey = null;

    protected void calculateClr(String fat, String snf) {
        if (!fat.isEmpty() && !snf.isEmpty()) {
            if (Double.parseDouble(snf) == 0) {
                setClr("0");
            } else {
                BigDecimal clr = CommonUtils.calculateClr(fat, snf);
                setClr(clr != null ? clr.toBigInteger().toString() : "0");
            }
        }
    }

    protected void calculateAmount(String rate, String qty) {
        try {
            if (!rate.isEmpty() && !qty.isEmpty()) {
                mapKey = null;
                try {
                    Platform.runLater(() ->
                            setAmount(new BigDecimal(rate).multiply(new BigDecimal(qty)).setScale(SCALE, ROUND).toString()));
                } catch (Exception exception) {
                }
            }
        } catch (Exception e) {

        }
    }

    protected void fetchRate(String fat, String snf, MilkType milkType, MilkQualityType milkQualityType) {

        if (!fat.isEmpty() && !snf.isEmpty() && milkType != null && milkQualityType != null) {
            BigDecimal snfVal = null;
            if (memberMilkPurchaseRate.getRateType().getCode().intValue() == 1) {
                snfVal = new BigDecimal(0).setScale(SCALE, ROUND);
            } else if (memberMilkPurchaseRate.getRateType().getCode().intValue() == 2) {
                snfVal = new BigDecimal(snf).setScale(SCALE, ROUND);
            }
            mapKey = new BigDecimal(fat).setScale(SCALE, ROUND) + "#" +
                    snfVal + "#" +
                    milkType.getCode() + "#" + milkQualityType.getCode();
            BigDecimal val = mapRateDetails.get(mapKey);
            setRate(val != null ? val.setScale(SCALE, ROUND).toString() : "0");
        }
    }

    protected void closeDevicesIfAny() {
        if (MainApp.wsSerial != null) {
            MainApp.wsSerial.disconnect();
            MainApp.wsSerial = null;
        }
        if (MainApp.analyserSerial != null) {
            MainApp.analyserSerial.disconnect();
            MainApp.analyserSerial = null;
        }
        if (MainApp.analyserSerial2 != null) {
            MainApp.analyserSerial2.disconnect();
            MainApp.analyserSerial2 = null;
        }
        if (MainApp.analyserSerial3 != null) {
            MainApp.analyserSerial3.disconnect();
            MainApp.analyserSerial3 = null;
        }
        if (MainApp.analyserSerial4 != null) {
            MainApp.analyserSerial4.disconnect();
            MainApp.analyserSerial4 = null;
        }
        if (MainApp.displaySerial != null) {
            MainApp.displaySerial.disconnect();
            MainApp.displaySerial = null;
        }
        if (MainApp.splitterSerial != null) {
            MainApp.splitterSerial.disconnect();
            MainApp.splitterSerial = null;
        }
    }

    protected void setupAutoManual() {
        LOGGER.info("In setupAutoManual");
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("resources/collection/setting.ser"))) {
            HardwareSetting setting = (HardwareSetting) in.readObject();
            if (setting != null) {
                weightAuto = setting.isAutoQuantity();
                qualityAuto = setting.isAutoQuality();
                autoTare = setting.isAutoTare();
            }
            LOGGER.info("Weight Auto {}", weightAuto);
            LOGGER.info("Weight Auto Tare {}", autoTare);
            LOGGER.info("Quality Auto {}", qualityAuto);

            if (weightAuto) {
                Optional<HardwareDeviceConfig> configWs = collectionPreReqDto.getHardwareConfigList().stream()
                        .filter(p -> MainApp.identityDto.getDock().getDockNo().equals(p.getDock().getDockNo()) &&
                                p.getDeviceType().equals(HardwareDeviceConfigurationController.WS))
                        .findAny();
                if (configWs.isPresent() && !"NA".equalsIgnoreCase(configWs.get().getCommPort())) {
                    try {
                        LOGGER.info("Initialize WS Device with {}-{}", configWs.get().getHardwareDevice().getDeviceName(), configWs.get().getCommPort());
                        MainApp.wsSerial = new WsSerial(configWs.get().getHardwareDevice(), configWs.get().getCommPort(), this);
                    } catch (Exception e) {
                        LOGGER.error("WS DEVICE INIT ERROR", e);
                    }
                } else {
                    Optional<HardwareDeviceConfig> configWsSplitter = collectionPreReqDto.getHardwareConfigList().stream()
                            .filter(p -> MainApp.identityDto.getDock().getDockNo().equals(p.getDock().getDockNo()) &&
                                    p.getDeviceType().equals(HardwareDeviceConfigurationController.SPLITTER))
                            .findAny();
                    if (configWsSplitter.isPresent() && !"NA".equalsIgnoreCase(configWsSplitter.get().getCommPort())) {
                        try {
                            LOGGER.info("Initialize WS Device in Splitter mode with {}-{}", configWsSplitter.get().getHardwareDevice().getDeviceName(), configWsSplitter.get().getCommPort());
                            MainApp.splitterSerial = new SplitterSerial(configWsSplitter.get().getHardwareDevice(), configWsSplitter.get().getCommPort(), this);
                        } catch (Exception e) {
                            LOGGER.error("WS SPLITTER DEVICE INIT ERROR", e);
                        }
                    }
                }
            }

            if (qualityAuto) {
                bindFatForAuto();
                bindSnfForAuto();

                Optional<HardwareDeviceConfig> configWsSplitter = collectionPreReqDto.getHardwareConfigList().stream()
                        .filter(p -> MainApp.identityDto.getDock().getDockNo().equals(p.getDock().getDockNo()) &&
                                p.getDeviceType().equals(HardwareDeviceConfigurationController.SPLITTER))
                        .findAny();
                if (configWsSplitter.isPresent() && !"NA".equalsIgnoreCase(configWsSplitter.get().getCommPort())) {
                    try {
                        LOGGER.info("Initialize WS Device in Splitter mode with {}-{}", configWsSplitter.get().getHardwareDevice().getDeviceName(), configWsSplitter.get().getCommPort());
                        MainApp.splitterSerial = new SplitterSerial(configWsSplitter.get().getHardwareDevice(), configWsSplitter.get().getCommPort(), this);
                    } catch (Exception e) {
                        LOGGER.error("WS SPLITTER DEVICE INIT ERROR", e);
                    }
                }

                Optional<HardwareDeviceConfig> configAnalyser = collectionPreReqDto.getHardwareConfigList().stream()
                        .filter(p -> MainApp.identityDto.getDock().getDockNo().equals(p.getDock().getDockNo()) &&
                                p.getDeviceType().equals(HardwareDeviceConfigurationController.ANALYSER))
                        .findAny();
                if (configAnalyser.isPresent() && !"NA".equalsIgnoreCase(configAnalyser.get().getCommPort())) {
                    try {
                        HardwareDeviceConfig hardwareDeviceConfig1 = configAnalyser.get();
                        LOGGER.info("Initialize Analyser1 Device with {}-{}", hardwareDeviceConfig1.getHardwareDevice().getDeviceName(), hardwareDeviceConfig1.getCommPort());
                        MainApp.analyserSerial = new AnalyserSerial(hardwareDeviceConfig1.getHardwareDevice(), hardwareDeviceConfig1.getCommPort(), AppConstant.DEVICE_TAG.ANALYSER_TAG, this);
                        analyserCount = 1;
                        analyserSeq = hardwareDeviceConfig1.getAnalyserModeType() != null && hardwareDeviceConfig1.getAnalyserModeType() == 0;
                        if (!analyserSeq)
                            analyserMilkType.put("MA1", hardwareDeviceConfig1.getAnalyserMilkType());
                        else
                            analyserMilkTypeMapping.put(0, "MA1");
                        analyserMilkTypeMappingLastSavedFrom.put(0, "MA1");
                    } catch (Exception e) {
                        LOGGER.error("ANALYSER1 DEVICE INIT ERROR", e);
                    }
                }

                Optional<HardwareDeviceConfig> configAnalyser2 = collectionPreReqDto.getHardwareConfigList().stream()
                        .filter(p -> MainApp.identityDto.getDock().getDockNo().equals(p.getDock().getDockNo()) &&
                                p.getDeviceType().equals(HardwareDeviceConfigurationController.ANALYSER2))
                        .findAny();
                if (configAnalyser2.isPresent() && !"NA".equalsIgnoreCase(configAnalyser2.get().getCommPort())) {
                    try {
                        HardwareDeviceConfig hardwareDeviceConfig2 = configAnalyser2.get();
                        LOGGER.info("Initialize Analyser2 Device with {}-{}", hardwareDeviceConfig2.getHardwareDevice().getDeviceName(), hardwareDeviceConfig2.getCommPort());
                        MainApp.analyserSerial2 = new AnalyserSerial(hardwareDeviceConfig2.getHardwareDevice(), hardwareDeviceConfig2.getCommPort(), AppConstant.DEVICE_TAG.ANALYSER2_TAG, this);
                        analyserCount = 2;
                        analyserSeq = hardwareDeviceConfig2.getAnalyserModeType() != null && hardwareDeviceConfig2.getAnalyserModeType() == 0;
                        if (!analyserSeq)
                            analyserMilkType.put("MA2", hardwareDeviceConfig2.getAnalyserMilkType());
                        else {
                            analyserMilkTypeMapping.put(0, analyserMilkTypeMapping.get(0) + ",MA2");
                        }
                    } catch (Exception e) {
                        LOGGER.error("ANALYSER2 DEVICE INIT ERROR", e);
                    }
                }

                Optional<HardwareDeviceConfig> configAnalyser3 = collectionPreReqDto.getHardwareConfigList().stream()
                        .filter(p -> MainApp.identityDto.getDock().getDockNo().equals(p.getDock().getDockNo()) &&
                                p.getDeviceType().equals(HardwareDeviceConfigurationController.ANALYSER3))
                        .findAny();
                if (configAnalyser3.isPresent() && !"NA".equalsIgnoreCase(configAnalyser3.get().getCommPort())) {
                    try {
                        HardwareDeviceConfig hardwareDeviceConfig3 = configAnalyser3.get();
                        LOGGER.info("Initialize Analyser3 Device with {}-{}", hardwareDeviceConfig3.getHardwareDevice().getDeviceName(), hardwareDeviceConfig3.getCommPort());
                        MainApp.analyserSerial3 = new AnalyserSerial(hardwareDeviceConfig3.getHardwareDevice(), hardwareDeviceConfig3.getCommPort(), AppConstant.DEVICE_TAG.ANALYSER3_TAG, this);
                        analyserCount = 3;
                        analyserSeq = hardwareDeviceConfig3.getAnalyserModeType() != null && hardwareDeviceConfig3.getAnalyserModeType() == 0;
                        if (!analyserSeq)
                            analyserMilkType.put("MA3", hardwareDeviceConfig3.getAnalyserMilkType());
                        else {
                            analyserMilkTypeMapping.put(0, analyserMilkTypeMapping.get(0) + ",MA3");
                        }
                    } catch (Exception e) {
                        LOGGER.error("ANALYSER3 DEVICE INIT ERROR", e);
                    }
                }

                Optional<HardwareDeviceConfig> configAnalyser4 = collectionPreReqDto.getHardwareConfigList().stream()
                        .filter(p -> MainApp.identityDto.getDock().getDockNo().equals(p.getDock().getDockNo()) &&
                                p.getDeviceType().equals(HardwareDeviceConfigurationController.ANALYSER4))
                        .findAny();
                if (configAnalyser4.isPresent() && !"NA".equalsIgnoreCase(configAnalyser4.get().getCommPort())) {
                    try {
                        HardwareDeviceConfig hardwareDeviceConfig4 = configAnalyser4.get();
                        LOGGER.info("Initialize Analyser4 Device with {}-{}", hardwareDeviceConfig4.getHardwareDevice().getDeviceName(), hardwareDeviceConfig4.getCommPort());
                        MainApp.analyserSerial4 = new AnalyserSerial(hardwareDeviceConfig4.getHardwareDevice(), hardwareDeviceConfig4.getCommPort(), AppConstant.DEVICE_TAG.ANALYSER4_TAG, this);
                        analyserCount = 4;
                        analyserSeq = hardwareDeviceConfig4.getAnalyserModeType() != null && hardwareDeviceConfig4.getAnalyserModeType() == 0;
                        if (!analyserSeq)
                            analyserMilkType.put("MA4", hardwareDeviceConfig4.getAnalyserMilkType());
                        else {
                            analyserMilkTypeMapping.put(0, analyserMilkTypeMapping.get(0) + ",MA4");
                        }
                    } catch (Exception e) {
                        LOGGER.error("ANALYSER4 DEVICE INIT ERROR", e);
                    }
                }

                if (!analyserSeq) {
                    analyserMilkType.forEach((k, v) -> {
                        String s = analyserMilkTypeMapping.get(v);
                        if (s == null) {
                            analyserMilkTypeMapping.put(v, k);
                            analyserMilkTypeMappingLastSavedFrom.put(v, k);
                        } else {
                            analyserMilkTypeMapping.put(v, analyserMilkTypeMapping.get(v) + "," + k);
                        }
                    });
                }

                setHardwarePanelDisable();
            } else {
                unbindFatForAuto();
                unbindSnfForAuto();
            }

            Optional<HardwareDeviceConfig> configDisplay = collectionPreReqDto.getHardwareConfigList().stream()
                    .filter(p -> MainApp.identityDto.getDock().getDockNo().equals(p.getDock().getDockNo()) &&
                            p.getDeviceType().equals(HardwareDeviceConfigurationController.DISPLAY))
                    .findAny();
            if (configDisplay.isPresent() && !"NA".equalsIgnoreCase(configDisplay.get().getCommPort())) {
                try {
                    LOGGER.info("Initialize Display device with {}-{}", configDisplay.get().getHardwareDevice().getDeviceName(), configDisplay.get().getCommPort());
                    MainApp.displaySerial = new DisplaySerial(configDisplay.get().getHardwareDevice(), configDisplay.get().getCommPort());
                } catch (Exception e) {
                    LOGGER.error("ANALYSER DEVICE INIT ERROR", e);
                }
            }

            setupAutoManualControls();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void tareWs() {
        if (weightAuto && autoTare && MainApp.wsSerial != null) {
            MainApp.wsSerial.tareWs();
        }
        if (weightAuto && autoTare && MainApp.splitterSerial != null) {
            MainApp.splitterSerial.tareWs();
        } else if (autoTare && MainApp.splitterSerial != null) {
            MainApp.splitterSerial.tareWs();
        }
    }

    protected void refreshDevice() {
        prevFat1 = BigDecimal.ZERO;
        prevFat2 = BigDecimal.ZERO;
        prevFat3 = BigDecimal.ZERO;
        prevFat4 = BigDecimal.ZERO;
        prevSnf1 = BigDecimal.ZERO;
        prevSnf2 = BigDecimal.ZERO;
        prevSnf3 = BigDecimal.ZERO;
        prevSnf4 = BigDecimal.ZERO;
    }

    @Override
    public void onResponseFromDevice(Map<String, String> resp, String tag) {
        switch (tag) {
            case AppConstant.DEVICE_TAG.WS_TAG:
                if (weightAuto) {
                    Platform.runLater(() ->
                            displayWsReading(resp));
                }
                break;
            case AppConstant.DEVICE_TAG.ANALYSER_TAG:
            case AppConstant.DEVICE_TAG.ANALYSER2_TAG:
            case AppConstant.DEVICE_TAG.ANALYSER3_TAG:
            case AppConstant.DEVICE_TAG.ANALYSER4_TAG:
                try {
                    if (qualityAuto) {
                        System.out.println("FAT READING:-----" + resp);
                        Platform.runLater(() -> displayAnalyserReading(resp, tag));

                    }
                } catch (Exception e) {
                    LOGGER.error("FROM Milkcollection base display analyser reading");
                    e.printStackTrace();
                }
                break;
            case AppConstant.DEVICE_TAG.SPLITTER_TAG:
                if (weightAuto || qualityAuto)
                    displaySplitterReading(resp);
                break;
        }
    }

    private void displaySplitterReading(Map<String, String> resp) {
        try {
            if (resp != null && !resp.isEmpty()) {
                String serialRespFat = resp.get("FAT");
                String serialRespSnf = resp.get("SNF");
                String serialRespWtr = resp.get("AWM");
                String serialRespWgt = resp.get("WGT");
                if (serialRespFat != null)
                    tempFat = CommonUtils.convertQualityValue(new BigDecimal(serialRespFat),
                            CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));
                if (serialRespSnf != null) {
                    if ("1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0"))) {
                        tempSnf = CommonUtils.convertQualityValue(new BigDecimal(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF_VALUE, "0")),
                                CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF_VALUE, "0")));
                    } else {
                        if (serialRespSnf != null)
                            tempSnf = CommonUtils.convertQualityValue(new BigDecimal(serialRespSnf),
                                    CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF_VALUE, "0")));
                    }
                } else {
                    tempSnf = CommonUtils.convertQualityValue(new BigDecimal(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF_VALUE, "0")),
                            CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));
                }
                if (serialRespWtr != null)
                    tempWater = CommonUtils.convertQualityValue(new BigDecimal(serialRespWtr),
                            CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));

                if (new BigDecimal(getFat()).compareTo(tempFat) != 0 && tempFat.doubleValue() > 2 && tempFat.doubleValue() < 15
                        && (prevFat1.compareTo(tempFat) != 0 || prevSnf1.compareTo(tempSnf) != 0)) {
                    unbindFatForAuto();
                    setFat(tempFat.toString());
                }

                if (("1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0"))) || ((new BigDecimal(getSnf()).compareTo(tempSnf) != 0
                        && (prevFat1.compareTo(tempFat) != 0 || prevSnf1.compareTo(tempSnf) != 0)))) { // if yes then go or no then must be greater than 1;
                    unbindSnfForAuto();
                    setSnf(tempSnf.toString());
                }

                if (new BigDecimal(getWater()).compareTo(tempWater) != 0)
                    setWater(tempWater.toString());

                if (serialRespWgt != null) {
                    BigDecimal wgt = weightLock.compareTo(BigDecimal.ZERO) > 0 ? weightLock.add(new BigDecimal(serialRespWgt)) : new BigDecimal(serialRespWgt);
                    tempQty = CommonUtils.convertQuantityValue(wgt,
                            CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QTY_READING_ROUND, "2")));
                    if (new BigDecimal(getQty()).compareTo(tempQty) != 0)
                        setQty(tempQty.toString());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Splitter Reading", e);
        }
    }

    private void displayAnalyserReading(Map<String, String> resp, String tag) {
        try {
            System.out.println("displayAnalyzerreading 293" + resp);
            System.out.println("displayAnalyzerreading 293 tag" + tag);
            System.out.println("MASETTING" + maSetting);
            if (resp != null && !resp.isEmpty()) {
                System.out.println("295");
                String serialRespFat = resp.get("FAT");
                String serialRespSnf = resp.get("SNF");
                String serialRespWtr = resp.get("AWM");
                if (tag.equalsIgnoreCase(AppConstant.DEVICE_TAG.ANALYSER_TAG)) {
                    System.out.println("MASETTING 1");
                    System.out.println("SERIAL RESPONSE FAT" + serialRespFat);
                    if (serialRespFat != null) {
                        tempFat = CommonUtils.convertQualityValue(new BigDecimal(serialRespFat),
                                CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));
                        System.out.println("tempdat milkcollectionbasecontroller 305" + tempFat);
                    }

                    System.out.println("Default SNF: " + (MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0")));
                    if ("1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0"))) {
                        tempSnf = CommonUtils.convertQualityValue(new BigDecimal(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF_VALUE, "0")),
                                CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));
                    } else {
                        if (serialRespSnf != null)
                            tempSnf = CommonUtils.convertQualityValue(new BigDecimal(serialRespSnf),
                                    CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));
                    }
                    System.out.println("Temp SNF: " + tempSnf);
                    if (serialRespWtr != null)
                        tempWater = CommonUtils.convertQualityValue(new BigDecimal(serialRespWtr),
                                CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));

                    if (new BigDecimal(getFat1()).compareTo(tempFat) != 0
                            && tempFat.doubleValue() > 2 && tempFat.doubleValue() < 20
                            && (prevFat1.compareTo(tempFat) != 0 || prevSnf1.compareTo(tempSnf) != 0)) {
                        setFat1(tempFat.toString());
                        System.out.println("MilkcollEction BaseController 313:" + tempFat.toString());
                    }

                    if ("1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0"))) {
                        setSnf1(tempSnf.toString());
                    } else if (new BigDecimal(getSnf1()).compareTo(tempSnf) != 0
                            && (prevFat1.compareTo(tempFat) != 0 || prevSnf1.compareTo(tempSnf) != 0)) {
                        setSnf1(tempSnf.toString());
                    }
                } else if (tag.equalsIgnoreCase(AppConstant.DEVICE_TAG.ANALYSER2_TAG)) {
                    System.out.println("MASETTING 2");
                    if (serialRespFat != null)
                        tempFat2 = CommonUtils.convertQualityValue(new BigDecimal(serialRespFat),
                                CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));

                    System.out.println("Default SNF: " + (MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0")));
                    if ("1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0"))) {
                        tempSnf2 = CommonUtils.convertQualityValue(new BigDecimal(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF_VALUE, "0")),
                                CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));
                    } else {
                        if (serialRespSnf != null)
                            tempSnf2 = CommonUtils.convertQualityValue(new BigDecimal(serialRespSnf),
                                    CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));
                    }
                    System.out.println("Temp SNF: " + tempSnf2);
                    if (serialRespWtr != null)
                        tempWater2 = CommonUtils.convertQualityValue(new BigDecimal(serialRespWtr),
                                CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));

                    if (new BigDecimal(getFat2()).compareTo(tempFat2) != 0
                            && tempFat2.doubleValue() > 2 && tempFat2.doubleValue() < 20
                            && (prevFat2.compareTo(tempFat2) != 0 || prevSnf2.compareTo(tempSnf2) != 0)) {
                        setFat2(tempFat2.toString());
                    }
                    if ("1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0"))) {
                        setSnf2(tempSnf2.toString());
                    } else if (new BigDecimal(getSnf2()).compareTo(tempSnf2) != 0
                            && (prevFat2.compareTo(tempFat2) != 0 || prevSnf2.compareTo(tempSnf2) != 0)) {
                        setSnf2(tempSnf2.toString());
                    }
                    if (new BigDecimal(getWater()).compareTo(tempWater2) != 0)
                        setWater2(tempWater2.toString());
                } else if (tag.equalsIgnoreCase(AppConstant.DEVICE_TAG.ANALYSER3_TAG)) {
                    System.out.println("MASETTING 3");
                    if (serialRespFat != null)
                        tempFat3 = CommonUtils.convertQualityValue(new BigDecimal(serialRespFat),
                                CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));
                    if ("1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0"))) {
                        tempSnf3 = CommonUtils.convertQualityValue(new BigDecimal(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF_VALUE, "0")),
                                CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));
                    } else {
                        if (serialRespSnf != null)
                            tempSnf3 = CommonUtils.convertQualityValue(new BigDecimal(serialRespSnf),
                                    CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));
                    }
                    if (serialRespWtr != null)
                        tempWater3 = CommonUtils.convertQualityValue(new BigDecimal(serialRespWtr),
                                CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));

                    if (new BigDecimal(getFat3()).compareTo(tempFat3) != 0
                            && tempFat3.doubleValue() > 2 && tempFat3.doubleValue() < 20
                            && (prevFat3.compareTo(tempFat3) != 0 || prevSnf3.compareTo(tempSnf3) != 0)) {
                        setFat3(tempFat3.toString());
                    }
                    if ("1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0"))) {
                        setSnf3(tempSnf3.toString());
                    } else if (new BigDecimal(getSnf3()).compareTo(tempSnf3) != 0
                            && (prevFat3.compareTo(tempFat3) != 0 || prevSnf3.compareTo(tempSnf3) != 0)) {
                        setSnf3(tempSnf3.toString());
                    }
                    if (new BigDecimal(getWater()).compareTo(tempWater3) != 0)
                        setWater3(tempWater3.toString());
                } else if (tag.equalsIgnoreCase(AppConstant.DEVICE_TAG.ANALYSER4_TAG)) {

                    System.out.println("MASETTING 4");
                    if (serialRespFat != null)
                        tempFat4 = CommonUtils.convertQualityValue(new BigDecimal(serialRespFat),
                                CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));
                    if ("1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0"))) {
                        tempSnf4 = CommonUtils.convertQualityValue(new BigDecimal(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF_VALUE, "0")),
                                CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));
                    } else {
                        if (serialRespSnf != null)
                            tempSnf4 = CommonUtils.convertQualityValue(new BigDecimal(serialRespSnf),
                                    CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));
                    }
                    if (serialRespWtr != null)
                        tempWater4 = CommonUtils.convertQualityValue(new BigDecimal(serialRespWtr),
                                CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QUALITY_READING_ROUND, "0")));

                    if (new BigDecimal(getFat4()).compareTo(tempFat4) != 0
                            && tempFat4.doubleValue() > 2 && tempFat4.doubleValue() < 15
                            && (prevFat4.compareTo(tempFat4) != 0 || prevSnf4.compareTo(tempSnf4) != 0)) {
                        setFat4(tempFat4.toString());
                    }
                    if ("1".equals(MainApp.getProperty(AppConstant.Props.DEFAULT_SNF, "0"))) {
                        setSnf4(tempSnf4.toString());
                    } else if (new BigDecimal(getSnf4()).compareTo(tempSnf4) != 0
                            && (prevFat4.compareTo(tempFat4) != 0 || prevSnf4.compareTo(tempSnf4) != 0)) {
                        setSnf4(tempSnf4.toString());
                    }
                    if (new BigDecimal(getWater()).compareTo(tempWater4) != 0)
                        setWater4(tempWater4.toString());
                } else {
                    System.out.println("ELSE 421");
                }
            } else {
                System.out.println("BAHAR ELSE 426");
            }
        } catch (Exception e) {
            LOGGER.error("Analyser Reading", e);
        }
    }

    private void displayWsReading(Map<String, String> resp) {
        try {
            if (resp != null && !resp.isEmpty()) {
                BigDecimal wgt = weightLock.compareTo(BigDecimal.ZERO) > 0 ? weightLock.add(new BigDecimal(resp.get("WGT"))) : new BigDecimal(resp.get("WGT"));
                tempQty = CommonUtils.convertQuantityValue(wgt, CommonUtils.strToInteger(MainApp.getProperty(AppConstant.Props.QTY_READING_ROUND, "2")));
                if (new BigDecimal(getQty()).compareTo(tempQty) != 0)
                    setQty(tempQty.toString());
            }
        } catch (Exception e) {
            LOGGER.error("WS Reading", e);
        }
    }

    protected abstract String getQty();

    protected abstract void setQty(String qty);

    protected abstract String getFat();

    protected abstract void setFat(String fat);

    protected abstract String getSnf();

    protected abstract void setSnf(String snf);

    protected abstract String getWater();

    protected abstract void setWater(String water);

    protected abstract String getClr();

    protected abstract void setClr(String clr);

    protected abstract void setRate(String rate);

    protected abstract void setAmount(String amount);

    protected abstract void setupAutoManualControls();

    protected abstract MilkType getMilkType();

    protected abstract void setMilkType(MilkType milkType);

    protected abstract String getSampleNo();

    protected abstract void setSampleNo(String sampleNo);

    protected abstract String getFat1();

    protected abstract void setFat1(String fat);

    protected abstract String getSnf1();

    protected abstract void setSnf1(String snf);

    protected abstract String getWater1();

    protected abstract void setWater1(String water);

    protected abstract String getFat2();

    protected abstract void setFat2(String fat);

    protected abstract String getSnf2();

    protected abstract void setSnf2(String snf);

    protected abstract String getWater2();

    protected abstract void setWater2(String water);

    protected abstract String getFat3();

    protected abstract void setFat3(String fat);

    protected abstract String getSnf3();

    protected abstract void setSnf3(String snf);

    protected abstract String getWater3();

    protected abstract void setWater3(String water);

    protected abstract String getFat4();

    protected abstract void setFat4(String fat);

    protected abstract String getSnf4();

    protected abstract void setSnf4(String snf);

    protected abstract String getWater4();

    protected abstract void setWater4(String water);

    protected abstract void bindFatForAuto();

    protected abstract void bindSnfForAuto();

    protected abstract void unbindFatForAuto();

    protected abstract void unbindSnfForAuto();

    protected abstract void setHardwarePanelDisable();
}