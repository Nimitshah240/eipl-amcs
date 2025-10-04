package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelAudit;
import com.eipl.amcs.master.org.model.Route;
import com.eipl.amcs.master.org.model.Society;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_dispatch")
public class ProductDispatchAudit extends BaseModelAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String challanNo;
    private Boolean challanVerified;
    private LocalDateTime requisitionDate;
    private LocalDate dispatchDate;
    private String referenceNo;
    private String vehicleNo;
    private String unionCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
//	@JsonIgnoreProperties(value = { "society", "union" })
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
//	@JsonIgnoreProperties(value = { "society", "union" })
    private Route route;

}
