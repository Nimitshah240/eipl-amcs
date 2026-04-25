package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_contact_details")
@Getter
@Setter
public class ContactDetails extends BaseModel {

    @Id
    @Column(name = "detail_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detailCode;
    private String moduleName;
    private String moduleCode;
    private String contactPerson;
    private String email;
    private String mobileNo;
    private String localContactPerson;
    private String department;
    private String firstname;
    private String lastname;
    private String surname;
    private String localFirstname;
    private String localLastname;
    private String localSurname;
    private Boolean isDefault;
    private Boolean isVerified;
    private String isContactVerified;
    private String remarks;
    private String emailTo;
    private String emailCc;
    private String emailBcc;
    private String employeeCode;
    private String unionCode;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String primaryParent;
    private String secondaryParent;
}