package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.MilkClassDeserializer;
import com.eipl.amcs.json.deserialize.MilkTypeDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.MilkClassSerialize;
import com.eipl.amcs.json.serialize.MilkTypeSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "local_milk_sale_rate_audit")
public class LocalMilkSaleRateAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Digits(integer = 4, fraction = 2)
    private BigDecimal rate;
    private String unionCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate wefDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private MilkType milkType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkClassSerialize.class)
    @JsonDeserialize(using = MilkClassDeserializer.class)
    @JoinColumn(name = "milk_class_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private MilkClass milkClass;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Society society;

    @Override
    public String getTableName() {
        return "local_milk_sale_rate_audit";
    }
}
