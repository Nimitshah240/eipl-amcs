package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.deserialize.BmcDeserializer;
import com.eipl.amcs.deserialize.UnionDeserializer;
import com.eipl.amcs.serialize.BmcSerialize;
import com.eipl.amcs.serialize.UnionSerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "routes")
public class Route extends BaseModel {
    @Id
    @Size(max = 10)
    private String code;
    @Size(max = 10)
    private String codeEx;
    @Size(max = 200)
    private String name;
    @Size(max = 255)
    private String nameLocal;

    private Integer capacity;
    private Integer lengthKms;
    private LocalTime startTime;
    private LocalTime returnTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_routes_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BmcSerialize.class)
    @JsonDeserialize(using = BmcDeserializer.class)
    @JoinColumn(name = "bmc_code", foreignKey = @ForeignKey(name = "fk_routes_bmc_code"))
    @JsonIgnoreProperties(value = {"union", "mcc", "state", "district", "subDistrict", "village", "hamlet"})
    private Bmc bmc;

    @Override
    public String getTableName() {
        return "routes";
    }
}
