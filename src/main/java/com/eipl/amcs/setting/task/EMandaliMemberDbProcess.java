package com.eipl.amcs.setting.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EMandaliMemberDbProcess extends Task<List<MemberDto>> {
    private final Map<String, MilkType> milkTypeMap;
    private final Map<String, Gender> genderMap;
    private final MemberType memberType;
    private final String cowRange;
    private final String buffRange;
    private final String dbName;

    public EMandaliMemberDbProcess(Map<String, MilkType> milkTypeMap, Map<String, Gender> genderMap, MemberType memberType,
                                   String cowRange, String buffRange, String dbName) {
        this.milkTypeMap = milkTypeMap;
        this.genderMap = genderMap;
        this.memberType = memberType;
        this.cowRange = cowRange;
        this.buffRange = buffRange;
        this.dbName = dbName;
    }

    @Override
    protected List<MemberDto> call() throws Exception {
        List<MemberDto> list = new ArrayList<>();
        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=" + dbName + ";user=sa;password=everest;integretedSecurity=false";
            String[] cowRangeArr = cowRange.split("-");
            int cowMin = CommonUtils.strToInteger(cowRangeArr[0]);
            int cowMax = CommonUtils.strToInteger(cowRangeArr[1]);
            String[] buffRangeArr = buffRange.split("-");
            int buffMin = CommonUtils.strToInteger(buffRangeArr[0]);
            int buffMax = CommonUtils.strToInteger(buffRangeArr[1]);
            try (Connection connection = DriverManager.getConnection(connectionUrl); Statement stmt = connection.createStatement()) {
//            try (Connection connection = DriverManager.getConnection(urlDb, "", AppConstant.PROMPT_DB_PASS)) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from MemberMaster");

                while (resultSet.next()) {
                    // Member
                    Member m = new Member();
                    int codeEx = CommonUtils.strToInteger(resultSet.getString("Code"));
                    m.setCodeEx(String.format("%04d", codeEx));
                    m.setCode(MainApp.identityDto.getSociety().getCode() + m.getCodeEx());
//                    String[] nameArr = resultSet.getString("NameEng") != null ?
//                            resultSet.getString("NameEng").trim().split("\\s+") : null;
//                    if (nameArr != null) {
//                        m.setLastName(nameArr[0]);
//                        m.setFirstName(nameArr.length > 1 ? nameArr[1] : "Member");
//                        m.setMiddleName(nameArr.length > 2 ? nameArr[2] : "");
//                        if (nameArr[0].equalsIgnoreCase("")) {
//                            m.setFirstName("Member");
//                            m.setLastName(m.getCodeEx());
//                        }
//                    }

                    m.setFirstName(resultSet.getString("NameEng") != null ? resultSet.getString("NameEng") : "Member");
                    m.setLastName(".");

//                    String[] nameLocalArr = resultSet.getString("NameGuj") != null ?
//                            resultSet.getString("NameGuj").split("\\s+") : null;
//                    if (nameLocalArr != null) {
//                        m.setLastNameLocal(nameLocalArr[0]);
//                        m.setFirstNameLocal(nameLocalArr.length > 1 ? nameLocalArr[1] : "");
//                        m.setMiddleNameLocal(nameLocalArr.length > 2 ? nameLocalArr[2] : "");
//                    }
                    m.setFirstNameLocal(resultSet.getString("NameGuj") != null ? resultSet.getString("NameGuj") : "Member");
                    m.setMemberType(memberType);
                    m.setSociety(MainApp.identityDto.getSociety());
                    m.setActive(true);
                    m.setMobileNo("0000000000");
                    if (codeEx >= cowMin && codeEx <= cowMax)
                        m.setMilkType(milkTypeMap.get("c"));
                    else if (codeEx >= buffMin && codeEx <= buffMax)
                        m.setMilkType(milkTypeMap.get("b"));
                    else
                        m.setMilkType(milkTypeMap.get("c"));

                    String genderStr = Short.parseShort(resultSet.getString("Gender")) == 0 ? "M" : "F";

                    // Member details
                    MemberDetail md = new MemberDetail();
                    md.setGender(genderMap.get(genderStr));
                    md.setUnionCode(MainApp.identityDto.getUnion().getCode());
                    md.setMember(m);
                    md.setAccountNo(resultSet.getString("BankAcNo"));
                    if (md.getAccountNo() == null || md.getAccountNo().isEmpty() || md.getAccountNo().equalsIgnoreCase("0"))
                        md.setPaymentMode((short) 0);
                    else {
                        md.setPaymentMode((short) 1);
                    }
                    list.add(new MemberDto(m, md));


                }
                resultSet.close();
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
