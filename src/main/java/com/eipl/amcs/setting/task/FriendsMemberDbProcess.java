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

public class FriendsMemberDbProcess extends Task<List<MemberDto>> {
    private final Map<String, MilkType> milkTypeMap;
    private final Map<String, Gender> genderMap;
    private final MemberType memberType;
    private final String filePath;
    private final String cowRange;
    private final String buffRange;

    public FriendsMemberDbProcess(Map<String, MilkType> milkTypeMap, Map<String, Gender> genderMap, MemberType memberType,
                                  String filePath, String cowRange, String buffRange) {
        this.milkTypeMap = milkTypeMap;
        this.genderMap = genderMap;
        this.memberType = memberType;
        this.filePath = filePath;
        this.cowRange = cowRange;
        this.buffRange = buffRange;
    }

    @Override
    protected List<MemberDto> call() throws Exception {
        List<MemberDto> list = new ArrayList<>();
        try {
            String urlDb = "jdbc:ucanaccess://" + filePath;
            String[] cowRangeArr = cowRange.split("-");
            int cowMin = CommonUtils.strToInteger(cowRangeArr[0]);
            int cowMax = CommonUtils.strToInteger(cowRangeArr[1]);
            String[] buffRangeArr = buffRange.split("-");
            int buffMin = CommonUtils.strToInteger(buffRangeArr[0]);
            int buffMax = CommonUtils.strToInteger(buffRangeArr[1]);

            try (Connection connection = DriverManager.getConnection(urlDb, "", "")) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from mstMember");

                while (resultSet.next()) {
                    // Member
                    Member m = new Member();
                    int codeEx = CommonUtils.strToInteger(resultSet.getString("memCode"));
                    m.setCodeEx(String.format("%04d", codeEx));
                    m.setCode(MainApp.identityDto.getSociety().getCode() + m.getCodeEx());

                    String nameArr = resultSet.getString("memName");
                    m.setFirstName(nameArr);
                    m.setLastName(".");
                    m.setMemberType(memberType);
                    m.setFirstNameLocal("");
                    m.setMiddleNameLocal("");
                    m.setLastNameLocal("");
                    m.setSociety(MainApp.identityDto.getSociety());
                    m.setMobileNo("0000000000");
                    if (codeEx >= cowMin && codeEx <= cowMax)
                        m.setMilkType(milkTypeMap.get("c"));
                    else if (codeEx >= buffMin && codeEx <= buffMax)
                        m.setMilkType(milkTypeMap.get("b"));
                    else
                        m.setMilkType(milkTypeMap.get("c"));
                    m.setActive(true);

                    // Member details
                    MemberDetail md = new MemberDetail();
                    md.setGender(genderMap.get("m"));
                    md.setUnionCode(MainApp.identityDto.getUnion().getCode());
                    md.setMember(m);
                    md.setAccountNo(resultSet.getString("bankcode"));
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
