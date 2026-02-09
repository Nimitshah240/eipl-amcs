package com.eipl.amcs.master.operation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) //
public class MemberDownloadDto {
    private String memberTypeCode;
    private String pincode;
    private String mobileNo;
    private String fatherName;
    private String localAddress;
    private String noOfCowCross;
    private String localNomineeName;
    private String totalAnimals;
    private String hamletCode;
    private String originatingOrgType;
    private String localName;
    private String districtCode;
    private String email;
    private String stateCode;
    private String refCode;
    private String dataPostId;
    private String memberCode;
    private String xcol1;
    private String xcol2;
    private String xcol3;
    private String xcol4;
    private String ifsc;
    private String branchCode;
    private String casteCategoryCode;
    private String dob;
    private String villageCode;
    private String exMemberCode;
    private String createdAt;
    private String panNo;
    private String downloadDateTime;
    private String memberName;
    private String originatingOrgCode;
    private String bankCode;
    private String landClass;
    private String genderCode;
    private String totalLand;
    private String federationCode;
    private String bloodgroupCode;
    private String noOfBuffalo;
    private String surname;
    private String unionCode;
    private String createdBy;
    private String dcsCode;
    private String respStatus;
    private String animalTypeCode;
    private String bankName;
    private String annualIncome;
    private String noOfCowInd;
    private String nomineeRelation;
    private String localSurname;
    private String bankAccountNo;
    private String address;
    private String adharNo;
    private String subDistrictCode;
    private String nomineeName;
    private String memberClass;
    private String upload;
    private String voterId;
    private String registrationDate;
    private String pickedDatetime;
    private String xcol5;
    private String religionCode;
    private String originatingType;
    private String paymentMode;
    private String branchName;
    private String updatedAt;
    private String isActive;
    private String localFatherNamae;
    private String updatedBy;
    private String qualificationCode;
}
