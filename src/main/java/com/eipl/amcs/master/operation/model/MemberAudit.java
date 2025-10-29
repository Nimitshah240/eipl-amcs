package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.BaseModelAudit;
import com.eipl.amcs.deserialize.MemberTypeDeserializer;
import com.eipl.amcs.deserialize.MilkTypeDeserializer;
import com.eipl.amcs.deserialize.SocietyDeserializer;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.serialize.MemberTypeSerialize;
import com.eipl.amcs.serialize.MilkTypeSerialize;
import com.eipl.amcs.serialize.SocietySerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "members_audit")
public class MemberAudit extends BaseModelAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 20)
    private String code;
    @Size(max = 4)
    private String codeEx;
    @Size(max = 100)
    private String firstName;
    @Size(max = 100)
    private String middleName;
    @Size(max = 100)
    private String lastName;
    @Size(max = 255)
    private String firstNameLocal;
    @Size(max = 255)
    private String middleNameLocal;
    @Size(max = 255)
    private String lastNameLocal;
    @Size(max = 255)
    private String mobileNo;
    private BigDecimal creditLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private MilkType milktype;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberTypeSerialize.class)
    @JsonDeserialize(using = MemberTypeDeserializer.class)
    @JoinColumn(name = "member_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private MemberType memberType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Society society;

    @Override
    public String getTableName() {
        return "members_audit";
    }

}
