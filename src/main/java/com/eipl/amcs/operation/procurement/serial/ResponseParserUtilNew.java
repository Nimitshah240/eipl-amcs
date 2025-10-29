package com.eipl.amcs.operation.procurement.serial;

import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ResponseParserUtilNew {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseParserUtilNew.class);
    static String serialRespAdtParam;
    static String adtParam;

    public synchronized static Map<String, String> parse(String data, String regExpression) {
        if (data == null || data.isEmpty())
            return null;
        if (regExpression == null || regExpression.isEmpty())
            return null;

        // temp
        data = data.replaceAll("-", "0");

        int startIndex = 0, endIndex = 0;
        Map<String, String> resp = new HashMap<>();
        String tempRe = regExpression;

        // setting up for calculations through regular expression
        try {
//            FAT(4,2)SNF(4,2)PRT(4,2)AWM(4,2)DEN(4,2)CLK(4,2)CHK(5,0)
            while (regExpression.length() > 1) {
                try {
                    endIndex += CommonUtils.strToInteger(regExpression.substring(regExpression.indexOf("(") + 1, regExpression.indexOf(",")));
                    resp.put(regExpression.substring(0, 3), endIndex > 0 ? data.substring(startIndex, endIndex) : data);
                    startIndex = startIndex + CommonUtils.strToInteger(regExpression.substring(regExpression.indexOf("(") + 1, regExpression.indexOf(",")));
                    regExpression = regExpression.substring(regExpression.indexOf(")") + 1);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (Exception e) {
            LOGGER.error("PARSE ERROR", e);
        }

        // validate checksum if available
        boolean checksum = resp.containsKey(AppConstant.PARAM_CHK);

        if (checksum) {
            int chksum = 0, actual = 0;
            for (String key : resp.keySet()) {
                try {
                    if (key.equals(AppConstant.PARAM_CHK))
                        chksum = Integer.parseInt(resp.get(key).trim());
                    else
                        actual += Integer.parseInt(resp.get(key).trim());
                } catch (NumberFormatException e) {

                }
            }
            if (chksum != actual)
                return null;
        }

        // setting up decimal point
        while (tempRe.length() > 1) {
            for (Map.Entry<String, String> entry : resp.entrySet()) {
                try {
                    if (entry.getKey().equals(tempRe.substring(0, 3)) && !entry.getKey().equals("CHK") && !entry.getKey().equals("COR")) {
                        int si = tempRe.indexOf(",") + 1;
                        int ei = tempRe.indexOf(",") + 2;
                        if (Integer.parseInt(tempRe.substring(si, ei)) > 0) {
                            entry.setValue(new StringBuffer(entry.getValue())
                                    .insert(Integer.parseInt(tempRe.substring(si, ei)), ".").toString());
                            break;
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("PARSE ERROR", e);
                }
            }
            tempRe = tempRe.substring(tempRe.indexOf(")") + 1);
        }

        // Prepare response for adt param if available
        adtParam = "";
        if (resp.containsKey("AD1")) {
            serialRespAdtParam = resp.get("AD1").trim();
            if (serialRespAdtParam != null && !serialRespAdtParam.isEmpty()) {
                adtParam += serialRespAdtParam;
            }
            if (/*!adtParam.isEmpty() &&*/ resp.containsKey("AD2")) {
                serialRespAdtParam = resp.get("AD2").trim();
                if (serialRespAdtParam != null && !serialRespAdtParam.isEmpty()) {
                    adtParam += getAdtSeperator(adtParam) + serialRespAdtParam;
                }
                serialRespAdtParam = resp.get("AD3").trim();
                if (serialRespAdtParam != null && !serialRespAdtParam.isEmpty()) {
                    adtParam += getAdtSeperator(adtParam) + serialRespAdtParam;
                }
                serialRespAdtParam = resp.get("AD4").trim();
                if (serialRespAdtParam != null && !serialRespAdtParam.isEmpty()) {
                    adtParam += getAdtSeperator(adtParam) + serialRespAdtParam;
                }
                serialRespAdtParam = resp.get("AD5").trim();
                if (serialRespAdtParam != null && !serialRespAdtParam.isEmpty()) {
                    adtParam += getAdtSeperator(adtParam) + serialRespAdtParam;
                }
                serialRespAdtParam = resp.get("AD6").trim();
                if (serialRespAdtParam != null && !serialRespAdtParam.isEmpty()) {
                    adtParam += getAdtSeperator(adtParam) + serialRespAdtParam;
                }
            }
        }
        if (!adtParam.isEmpty()) {
            resp.put("ADP", adtParam);
        }
        LOGGER.info("PARSE RESPONSE: {}", resp);
        return resp;
    }

    private static String getAdtSeperator(String adtParam) {
        return adtParam.trim().length() == 0 ? "" : ", ";
    }
}