package com.eipl.amcs.setting.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.utils.CommonUtils;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SkyWayVersionOneMemberFileProcess extends Task<List<MemberDto>> {
    private Map<String, MilkType> milkTypeMap;
    private Map<String, Gender> genderMap;
    private MemberType memberType;
    private String filePath;
    private String cowRange;
    private String buffRange;

    public SkyWayVersionOneMemberFileProcess(Map<String, MilkType> milkTypeMap, Map<String, Gender> genderMap,
                                             MemberType memberType, String filePath, String cowRange, String buffRange) {
        this.milkTypeMap = milkTypeMap;
        this.genderMap = genderMap;
        this.memberType= memberType;
        this.filePath = filePath;
        this.cowRange = cowRange;
        this.buffRange = buffRange;
    }

    @Override
    protected List<MemberDto> call() throws Exception {
        List<MemberDto> list = new ArrayList<>();
        try {
            String[] cowRangeArr = cowRange.split("-");
            int cowMin = CommonUtils.strToInteger(cowRangeArr[0]);
            int cowMax = CommonUtils.strToInteger(cowRangeArr[1]);
            String[] buffRangeArr = buffRange.split("-");
            int buffMin = CommonUtils.strToInteger(buffRangeArr[0]);
            int buffMax = CommonUtils.strToInteger(buffRangeArr[1]);

            try(Stream<String> lines = Files.lines(new File(filePath).toPath(), StandardCharsets.UTF_8)) {
                lines.forEach(line -> {
                    String[] arr = line.split(",");

                    Member m = new Member();
                    int codeEx = CommonUtils.strToInteger(arr[0]);
                    m.setCodeEx(String.format("%04d", Integer.parseInt(arr[0])));
                    m.setCode(MainApp.identityDto.getSociety().getCode() + m.getCodeEx());
//                    String[] nameArr = arr[17].trim().replace("'", "").split("\\s+");
//                    m.setLastName(nameArr[0].isEmpty() ? m.getCodeEx() : nameArr[0] );
//                    m.setFirstName(nameArr.length > 1 ? nameArr[1] : "Member");
//                    m.setMiddleName(nameArr.length > 2 ? nameArr[2] : "");


                    m.setFirstName(arr[17]!=null?arr[17]:"Member");
                    m.setLastName(".");
//                    String[] nameLocalArr = arr[1].trim().replace("'", "").split("\\s+");
                    m.setFirstNameLocal(arr[1]);
//                    m.setLastNameLocal(nameLocalArr[0]);
//                    m.setFirstNameLocal(nameLocalArr.length > 1 ? nameLocalArr[1] : "");
//                    m.setMiddleNameLocal(nameLocalArr.length > 2 ? nameLocalArr[2] : "");
                    m.setActive(true);
                    m.setMemberType(memberType);
                    m.setSociety(MainApp.identityDto.getSociety());
                    m.setMobileNo("0000000000");
                    if (codeEx >= cowMin && codeEx <= cowMax)
                        m.setMilkType(milkTypeMap.get("c"));
                    else if (codeEx >= buffMin && codeEx <= buffMax)
                        m.setMilkType(milkTypeMap.get("b"));
                    else
                        m.setMilkType(milkTypeMap.get("c"));

                    // Member Details
                    MemberDetail md = new MemberDetail();
                    md.setGender(genderMap.get("m"));
                    md.setUnionCode(MainApp.identityDto.getUnion().getCode());
                    md.setNumberOfCow((short) 0);
                    md.setNumberOfBuffalo((short) 0);
                    md.setMember(m);
                    md.setAccountNo(arr.length > 18 ? arr[18].trim().replace("'", ""): null);
                    if(md.getAccountNo()==null || md.getAccountNo().isEmpty() || md.getAccountNo().equalsIgnoreCase("0"))
                        md.setPaymentMode((short)0);
                    else{
                        md.setPaymentMode((short)1);
                    }
                    list.add(new MemberDto(m, md));

                });
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
