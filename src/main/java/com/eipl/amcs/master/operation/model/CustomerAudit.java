package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.model.BaseModelAudit;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.UnionDeserializer;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.UnionSerialize;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customers_audit")
public class CustomerAudit extends BaseModelAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String nameLocal;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal creditLimit;
    private String mobileNo;
    private Integer paymentMode;
    private LocalDate registrationDate;
    private String registrationNo;
    private Integer type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Union union;

    @Override
    public String getTableName() {
        return "customers_audit";
    }

}
