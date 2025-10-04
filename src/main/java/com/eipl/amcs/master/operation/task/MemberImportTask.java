package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.dto.MemberDetail;
import com.eipl.amcs.master.operation.dto.MemberDto;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MemberImportTask extends Task<List<MemberDto>> {

    private File file;
    private List<MilkType> milkTypeList;
    private List<Gender> genderList;
    private List<MemberType> memberTypeList;
    private List<Bank> bankList;

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberImportTask.class);

    public MemberImportTask(File file, List<MilkType> milkTypeList, List<Gender> genderList, List<MemberType> memberTypeList, List<Bank> bankList) {
        this.file = file;
        this.milkTypeList = milkTypeList;
        this.genderList = genderList;
        this.memberTypeList = memberTypeList;
        this.bankList = bankList;
    }

    @Override
    protected List<MemberDto> call() throws Exception {
        try {
            Workbook workbook = new HSSFWorkbook(new FileInputStream(file));
            Sheet dataSheet = workbook.getSheetAt(0); // use first sheet for member data
            Iterator<Row> iterator = dataSheet.iterator();
            boolean firstRow = true;
            List<MemberDto> list = new ArrayList<>();
            BigDecimal creditLimit = new BigDecimal(MainApp.getProperty(AppConstant.Props.DEFAULT_CREDIT_LIMIT, "0"));
            int i = 1;
            while (iterator.hasNext()) {
                try {

                    Row row = iterator.next();
                    if (firstRow) {
                        firstRow = false;
                        continue;
                    }

                    DataFormatter formatter = new DataFormatter();
                    String val = formatter.formatCellValue(dataSheet.getRow(i).getCell(0));
//                Cell cellCode = row.getCell(0);
                    String code = val;
                    if (code == null && code.isEmpty()) {
                        i+=1;
                        continue;
                    }
                    String exCode = CommonUtils.getMemberShortCode(code);
                    code = MainApp.identityDto.getSociety().getCode() + exCode;
                    if (code.equalsIgnoreCase(MainApp.identityDto.getSociety().getCode() + "0000")) {
                        i+=1;
                        continue;
                    }

                    // First name
//                Cell cellFirstName = row.getCell(1);
                    String cellFirstName = formatter.formatCellValue(dataSheet.getRow(i).getCell(1));

                    String firstName = cellFirstName;
                    if (firstName == null || firstName.isEmpty()) {
                        firstName = "";
//                        continue;
                    }

                    // Middle name
//                Cell cellMiddleName = row.getCell(2);
                    String cellMiddleName = formatter.formatCellValue(dataSheet.getRow(i).getCell(2));

                    String middleName = null;
                    try {
                        middleName = cellMiddleName;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    // Last name
//                Cell cellLastName = row.getCell(3);
                    String cellLastName = formatter.formatCellValue(dataSheet.getRow(i).getCell(3));

                    String lastName = null;
                    try {
                        lastName = cellLastName;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
//                if (lastName == null || lastName.isEmpty()) {
//                    continue;
//                }

                    // First name local
//                Cell cellFirstNameLocal = row.getCell(4);
                    String cellFirstNameLocal = formatter.formatCellValue(dataSheet.getRow(i).getCell(4));
                    String firstNameLocal = null;
                    try {
                        firstNameLocal = cellFirstNameLocal;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    // Middle name local
//                Cell cellMiddleNameLocal = row.getCell(5);
                    String cellMiddleNameLocal = formatter.formatCellValue(dataSheet.getRow(i).getCell(5));
                    String middleNameLocal = null;
                    try {
                        middleNameLocal = cellMiddleNameLocal;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    // Last name local

//                Cell cellLastNameLocal = row.getCell(6);
                    String cellLastNameLocal = formatter.formatCellValue(dataSheet.getRow(i).getCell(6));
                    String lastNameLocal = null;
                    try {
                        lastNameLocal = cellLastNameLocal;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    // Member type
                    Cell cellMemberType = row.getCell(7);
                    String memberTypeStr;
                    if (cellMemberType != null)
                        memberTypeStr = cellMemberType.getStringCellValue() != null ? cellMemberType.getStringCellValue() : "M";
                    else
                        memberTypeStr = "M";
                    MemberType memberType = null;
                    if (memberTypeStr == null || memberTypeStr.isEmpty()) {
                        memberType = memberTypeList.get(0);
                    } else {
                        memberType = memberTypeList.stream().filter(p -> memberTypeStr.equalsIgnoreCase(p.getName()) || memberTypeStr.charAt(0) == p.getName().charAt(0))
                                .findAny().orElse(null);
                        if (memberType == null)
                            memberType = memberTypeList.get(0);
                    }

                    // MilkType
                    Cell cellMilkType = row.getCell(8);
                    String milkTypeStr;
                    if (cellMilkType != null)
                        milkTypeStr = cellMilkType.getStringCellValue() != null ? cellMilkType.getStringCellValue() : "B";
                    else
                        milkTypeStr = "B";
                    MilkType milkType = null;
                    if (milkTypeStr == null || milkTypeStr.isEmpty())
                        milkType = milkTypeList.get(0);
                    else {
                        milkType = milkTypeList.stream().filter(p -> milkTypeStr.equalsIgnoreCase(p.getName()) || milkTypeStr.charAt(0) == p.getName().charAt(0))
                                .findAny().orElse(null);
                        if (milkType == null)
                            milkType = milkTypeList.get(0);
                    }

                    // Mobile no
//                Cell cellMobile = row.getCell(9);
                    String cellMobile = formatter.formatCellValue(dataSheet.getRow(i).getCell(9));
                    String mobile = null;
                    try {
                        mobile = cellMobile != null ? cellMobile : null;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
//                    if (mobile == null || mobile.isEmpty()) {
//                        continue;
//                    }

                    // Gender
                    Cell cellGender = row.getCell(10);
                    String genderStr;
                    if (cellGender != null)
                        genderStr = cellGender.getStringCellValue() != null ? cellGender.getStringCellValue() : "M";
                    else genderStr = "M";
//                    String genderStr = cellGender.getStringCellValue() != null ? cellGender.getStringCellValue() : "M";
                    Gender gender = null;
                    if (genderStr == null || genderStr.isEmpty())
                        gender = genderList.get(0);
                    else {
                        gender = genderList.stream().filter(p -> genderStr.equalsIgnoreCase(p.getName()) || genderStr.charAt(0) == p.getName().charAt(0))
                                .findAny().orElse(null);
                        if (gender == null)
                            gender = genderList.get(0);
                    }

                    MemberDetail memberDetail = new MemberDetail();
                    // Bank
                    Cell cellBank = row.getCell(12);
                    Bank bank = null;
                    try {
                        if (bankList != null) {
                            if (cellBank != null) {
                                String bankStr = cellBank.getStringCellValue() != null ? cellBank.getStringCellValue() : null;
                                if (bankStr == null || bankStr.isEmpty())
//                                bank = bankList.get(0);
                                {
                                } else {
                                    bank = bankList.stream().filter(p -> bankStr.equalsIgnoreCase(p.getName()))
                                            .findAny().orElse(bankList.get(0));
                                    if (bank == null)
                                        bank = bankList.get(0);
                                    memberDetail.setBank(bank);
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String cellAccNo = formatter.formatCellValue(dataSheet.getRow(i).getCell(11));


//                Cell cellAccNo = row.getCell(11);
                    String AccNo = cellAccNo;

                    Cell cellIfsc = row.getCell(13);
                    String ifsc = "";
                    if (cellIfsc != null)
                        ifsc = cellIfsc.getStringCellValue() != null ? cellIfsc.getStringCellValue() : null;


                    Member member = new Member();
                    member.setCreditLimit(creditLimit);
                    member.setActive(true);
                    member.setCode(code);
                    member.setCodeEx(exCode);
                    member.setSociety(MainApp.identityDto.getSociety());
                    member.setMemberType(memberType);
                    member.setMilkType(milkType);
                    member.setMobileNo(mobile);
                    member.setFirstName(firstName);
                    member.setMiddleName(middleName);
                    member.setLastName(lastName);
                    member.setFirstNameLocal(firstNameLocal);
                    member.setMiddleNameLocal(middleNameLocal);
                    member.setLastNameLocal(lastNameLocal);


                    memberDetail.setUnionCode(MainApp.identityDto.getUnion().getCode());
                    memberDetail.setGender(gender);
                    memberDetail.setDistrict(MainApp.identityDto.getSociety().getDistrict());
                    memberDetail.setSubDistrict(MainApp.identityDto.getSociety().getSubDistrict());
                    memberDetail.setState(MainApp.identityDto.getSociety().getState());
                    memberDetail.setVillage(MainApp.identityDto.getSociety().getVillage());
                    memberDetail.setHamlet(MainApp.identityDto.getSociety().getHamlet());
                    memberDetail.setMember(member);
                    if (cellAccNo == null || cellAccNo.equalsIgnoreCase(""))
                        memberDetail.setPaymentMode((short) 0);
                    else {
                        memberDetail.setPaymentMode((short) 1);
                    }
                    memberDetail.setNumberOfCow((short) 0);
                    memberDetail.setNumberOfBuffalo((short) 0);
                    memberDetail.setCode(member.getCode());

                    if (AccNo != null)
                        memberDetail.setAccountNo(AccNo);

                    if (ifsc != null)
                        memberDetail.setIfsc(ifsc);

                    list.add(new MemberDto(member, memberDetail));
                    i += 1;
                } catch (Exception e) {
                    i += 1;
                    e.printStackTrace();
                }
            }
            return list;
        } catch (Exception e) {
            LOGGER.error("Member Import Error", e);
            e.printStackTrace();
        }
        return null;
    }
}
