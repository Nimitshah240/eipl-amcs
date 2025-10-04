package com.eipl.amcs.report.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MemberCollection {
    private Timestamp from_date;
    private Timestamp to_date;
    private String society_code;
    private String society_name;
    private String union_code;
    private String union_name;
    private String village_code;
    private String village_name;
    private Timestamp collection_date;
    private String qty_mode;
    private String member_code;
    private String member_name;
    private String milk_type;
    private BigDecimal installment_amount;
    private BigDecimal local_sale_credit;
    private BigDecimal netpayble;
    private BigDecimal deduction;
    private BigDecimal avg_rate;
    private BigDecimal amount;
    private BigDecimal qty;
    private BigDecimal avg_snf;
    private BigDecimal kg_snf;
    private BigDecimal avg_fat;
    private BigDecimal kg_fat;
    private BigDecimal avg_clr;
    private BigDecimal kg_clr;

    public MemberCollection(){
    }

    public Timestamp getFrom_date() {
        return from_date;
    }

    public void setFrom_date(Timestamp from_date) {
        this.from_date = from_date;
    }

    public Timestamp getTo_date() {
        return to_date;
    }

    public void setTo_date(Timestamp to_date) {
        this.to_date = to_date;
    }

    public String getSociety_code() {
        return society_code;
    }

    public void setSociety_code(String society_code) {
        this.society_code = society_code;
    }

    public String getSociety_name() {
        return society_name;
    }

    public void setSociety_name(String society_name) {
        this.society_name = society_name;
    }

    public String getUnion_code() {
        return union_code;
    }

    public void setUnion_code(String union_code) {
        this.union_code = union_code;
    }

    public String getUnion_name() {
        return union_name;
    }

    public void setUnion_name(String union_name) {
        this.union_name = union_name;
    }

    public String getVillage_code() {
        return village_code;
    }

    public void setVillage_code(String village_code) {
        this.village_code = village_code;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public Timestamp getCollection_date() {
        return collection_date;
    }

    public void setCollection_date(Timestamp collection_date) {
        this.collection_date = collection_date;
    }

    public String getQty_mode() {
        return qty_mode;
    }

    public void setQty_mode(String qty_mode) {
        this.qty_mode = qty_mode;
    }

    public String getMember_code() {
        return member_code;
    }

    public void setMember_code(String member_code) {
        this.member_code = member_code;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMilk_type() {
        return milk_type;
    }

    public void setMilk_type(String milk_type) {
        this.milk_type = milk_type;
    }

    public BigDecimal getInstallment_amount() {
        return installment_amount;
    }

    public void setInstallment_amount(BigDecimal installment_amount) {
        this.installment_amount = installment_amount;
    }

    public BigDecimal getLocal_sale_credit() {
        return local_sale_credit;
    }

    public void setLocal_sale_credit(BigDecimal local_sale_credit) {
        this.local_sale_credit = local_sale_credit;
    }

    public BigDecimal getNetpayble() {
        return netpayble;
    }

    public void setNetpayble(BigDecimal netpayble) {
        this.netpayble = netpayble;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public BigDecimal getAvg_rate() {
        return avg_rate;
    }

    public void setAvg_rate(BigDecimal avg_rate) {
        this.avg_rate = avg_rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getAvg_snf() {
        return avg_snf;
    }

    public void setAvg_snf(BigDecimal avg_snf) {
        this.avg_snf = avg_snf;
    }

    public BigDecimal getKg_snf() {
        return kg_snf;
    }

    public void setKg_snf(BigDecimal kg_snf) {
        this.kg_snf = kg_snf;
    }

    public BigDecimal getAvg_fat() {
        return avg_fat;
    }

    public void setAvg_fat(BigDecimal avg_fat) {
        this.avg_fat = avg_fat;
    }

    public BigDecimal getKg_fat() {
        return kg_fat;
    }

    public void setKg_fat(BigDecimal kg_fat) {
        this.kg_fat = kg_fat;
    }

    public BigDecimal getAvg_clr() {
        return avg_clr;
    }

    public void setAvg_clr(BigDecimal avg_clr) {
        this.avg_clr = avg_clr;
    }

    public BigDecimal getKg_clr() {
        return kg_clr;
    }

    public void setKg_clr(BigDecimal kg_clr) {
        this.kg_clr = kg_clr;
    }
}
