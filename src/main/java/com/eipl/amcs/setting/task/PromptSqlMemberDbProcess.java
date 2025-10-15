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

public class PromptSqlMemberDbProcess extends Task<List<MemberDto>> {
    private Map<String, MilkType> milkTypeMap;
    private Map<String, Gender> genderMap;
    private MemberType memberType;
    private String cowRange;
    private String buffRange;
    private String dbName;

    public PromptSqlMemberDbProcess(Map<String, MilkType> milkTypeMap, Map<String, Gender> genderMap, MemberType memberType,
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
            String connectionUrl = "jdbc:sqlserver://IT40\\EIPL;databaseName=" + dbName + ";integratedSecurity=false;encrypt=true;trustServerCertificate=true;user=sa;password=eipl";

          //  String connectionUrl = "jdbc:sqlserver://KHODAL-PC\\AMCSSERVER:1433;databaseName=" + dbName + ";integretedSecurity=false;user=dev;password=dev@123";
//            String connectionUrl = "jdbc:sqlserver://KHODAL-PC\\AMCSSERVER:1433;databaseName=" + dbName + ";integretedSecurity=true;";
//            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=" + dbName + ";user=sa;password=everest;integretedSecurity=false";
            String[] cowRangeArr = cowRange.split("-");
            int cowMin = CommonUtils.strToInteger(cowRangeArr[0]);
            int cowMax = CommonUtils.strToInteger(cowRangeArr[1]);
            String[] buffRangeArr = buffRange.split("-");
            int buffMin = CommonUtils.strToInteger(buffRangeArr[0]);
            int buffMax = CommonUtils.strToInteger(buffRangeArr[1]);
            try (Connection connection = DriverManager.getConnection(connectionUrl); Statement stmt = connection.createStatement();) {
//            try (Connection connection = DriverManager.getConnection(urlDb, "", AppConstant.PROMPT_DB_PASS)) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from tblSabhasad");

                while (resultSet.next()) {
                    // Member
                    Member m = new Member();
                    int codeEx = CommonUtils.strToInteger(resultSet.getString("SabhasadId"));
                    m.setCodeEx(String.format("%04d", codeEx));
                    m.setCode(MainApp.identityDto.getSociety().getCode() + m.getCodeEx());
//                    String[] nameArr = resultSet.getString("SName") != null ?
//                            resultSet.getString("SName").split("\\s+") : null;
//                    if (nameArr != null) {
//                        m.setLastName(nameArr[0]);
//                        m.setFirstName(nameArr.length > 1 ? nameArr[1] : "Member");
//                        m.setMiddleName(nameArr.length > 2 ? nameArr[2] : "");
//                    }
                    String nameArr = resultSet.getString("SName");
                    m.setFirstName(nameArr);
                    m.setLastName(".");
//                    String[] nameLocalArr = resultSet.getString("SNameG") != null ?
//                            resultSet.getString("SNameG").split("\\s+") : null;
//                    if (nameLocalArr != null) {
//                        m.setLastNameLocal(nameLocalArr[0]);
//                        m.setFirstNameLocal(nameLocalArr.length > 1 ? nameLocalArr[1] : "");
//                        m.setMiddleNameLocal(nameLocalArr.length > 2 ? nameLocalArr[2] : "");
//                    }
                    String nameArrLocal = resultSet.getString("SNameG");
                    m.setFirstNameLocal(nameArrLocal);
                    m.setMemberType(memberType);
                    m.setActive(true);
                    m.setSociety(MainApp.identityDto.getSociety());
                    m.setMobileNo(resultSet.getString("Phone") == null || resultSet.getString("Phone").isEmpty() || resultSet.getString("Phone").equalsIgnoreCase("0") ?
                            "0000000000" : resultSet.getString("Phone"));
                    if (codeEx >= cowMin && codeEx <= cowMax)
                        m.setMilkType(milkTypeMap.get("c"));
                    else if (codeEx >= buffMin && codeEx <= buffMax)
                        m.setMilkType(milkTypeMap.get("b"));
                    else
                        m.setMilkType(milkTypeMap.get("c"));

                    String genderStr = (resultSet.getString("Sex")) == null ? "M" : resultSet.getString("Sex");

                    // Member details
                    MemberDetail md = new MemberDetail();
                    md.setGender(genderMap.get(genderStr));
                    md.setUnionCode(MainApp.identityDto.getUnion().getCode());
                    md.setMember(m);
                    md.setAccountNo(resultSet.getString("AcNo"));

                    if(md.getAccountNo()==null || md.getAccountNo().isEmpty() || md.getAccountNo().equalsIgnoreCase("0"))
                        md.setPaymentMode((short)0);
                    else{
                        md.setPaymentMode((short)1);
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
