package com.eipl.amcs.report.dto;

import com.eipl.amcs.report.annotation.ReportDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Time;

@NoArgsConstructor
@Getter
@Setter
@ReportDto
public class MilkDispatchChallan {

    private String society_code;
    private String society_name;
    //    private String destination;
//    private BigInteger destination_type;
    private String challan_no;
    //    private Timestamp challan_date;
    private String challan_date;
    //    private String milk_quality_type_name;
//    private String milk_type_name;
//    private Integer dispatch_type;
    private String from_date;
    //    private Timestamp from_date;
//    private String from_shift;
    private String to_date;
    //    private Timestamp to_date;
//    private String to_shift;
    private String vehicle_no;
    private String route_no;
    private String route_name;
    private Time vehicle_in_time;
    private Time vehicle_out_time;
    private String collection_date;

    private BigDecimal milk_collection_cow_qty;
    private BigDecimal milk_collection_buffalo_qty;
    private BigDecimal milk_collection_mix_qty;
    private BigDecimal local_sale_cow_qty;
    private BigDecimal local_sale_buffalo_qty;
    private BigDecimal local_sale_mix_qty;
    private BigDecimal milk_dispatch_cow_qty;
    private BigDecimal milk_dispatch_buffalo_qty;
    private BigDecimal milk_dispatch_mix_qty;
    private BigDecimal milk_dispatch_cow_fat;
    private BigDecimal milk_dispatch_buffalo_fat;
    private BigDecimal milk_dispatch_mix_fat;

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

    public String getChallan_no() {
        return challan_no;
    }

    public void setChallan_no(String challan_no) {
        this.challan_no = challan_no;
    }

    public String getChallan_date() {
        return challan_date;
    }

    public void setChallan_date(String challan_date) {
        this.challan_date = challan_date;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getRoute_no() {
        return route_no;
    }

    public void setRoute_no(String route_no) {
        this.route_no = route_no;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public Time getVehicle_in_time() {
        return vehicle_in_time;
    }

    public void setVehicle_in_time(Time vehicle_in_time) {
        this.vehicle_in_time = vehicle_in_time;
    }

    public Time getVehicle_out_time() {
        return vehicle_out_time;
    }

    public void setVehicle_out_time(Time vehicle_out_time) {
        this.vehicle_out_time = vehicle_out_time;
    }

    public String getCollection_date() {
        return collection_date;
    }

    public void setCollection_date(String collection_date) {
        this.collection_date = collection_date;
    }

    public BigDecimal getMilk_collection_cow_qty() {
        return milk_collection_cow_qty;
    }

    public void setMilk_collection_cow_qty(BigDecimal milk_collection_cow_qty) {
        this.milk_collection_cow_qty = milk_collection_cow_qty;
    }

    public BigDecimal getMilk_collection_buffalo_qty() {
        return milk_collection_buffalo_qty;
    }

    public void setMilk_collection_buffalo_qty(BigDecimal milk_collection_buffalo_qty) {
        this.milk_collection_buffalo_qty = milk_collection_buffalo_qty;
    }

    public BigDecimal getMilk_collection_mix_qty() {
        return milk_collection_mix_qty;
    }

    public void setMilk_collection_mix_qty(BigDecimal milk_collection_mix_qty) {
        this.milk_collection_mix_qty = milk_collection_mix_qty;
    }

    public BigDecimal getLocal_sale_cow_qty() {
        return local_sale_cow_qty;
    }

    public void setLocal_sale_cow_qty(BigDecimal local_sale_cow_qty) {
        this.local_sale_cow_qty = local_sale_cow_qty;
    }

    public BigDecimal getLocal_sale_buffalo_qty() {
        return local_sale_buffalo_qty;
    }

    public void setLocal_sale_buffalo_qty(BigDecimal local_sale_buffalo_qty) {
        this.local_sale_buffalo_qty = local_sale_buffalo_qty;
    }

    public BigDecimal getLocal_sale_mix_qty() {
        return local_sale_mix_qty;
    }

    public void setLocal_sale_mix_qty(BigDecimal local_sale_mix_qty) {
        this.local_sale_mix_qty = local_sale_mix_qty;
    }

    public BigDecimal getMilk_dispatch_cow_qty() {
        return milk_dispatch_cow_qty;
    }

    public void setMilk_dispatch_cow_qty(BigDecimal milk_dispatch_cow_qty) {
        this.milk_dispatch_cow_qty = milk_dispatch_cow_qty;
    }

    public BigDecimal getMilk_dispatch_buffalo_qty() {
        return milk_dispatch_buffalo_qty;
    }

    public void setMilk_dispatch_buffalo_qty(BigDecimal milk_dispatch_buffalo_qty) {
        this.milk_dispatch_buffalo_qty = milk_dispatch_buffalo_qty;
    }

    public BigDecimal getMilk_dispatch_mix_qty() {
        return milk_dispatch_mix_qty;
    }

    public void setMilk_dispatch_mix_qty(BigDecimal milk_dispatch_mix_qty) {
        this.milk_dispatch_mix_qty = milk_dispatch_mix_qty;
    }

    public BigDecimal getMilk_dispatch_cow_fat() {
        return milk_dispatch_cow_fat;
    }

    public void setMilk_dispatch_cow_fat(BigDecimal milk_dispatch_cow_fat) {
        this.milk_dispatch_cow_fat = milk_dispatch_cow_fat;
    }

    public BigDecimal getMilk_dispatch_buffalo_fat() {
        return milk_dispatch_buffalo_fat;
    }

    public void setMilk_dispatch_buffalo_fat(BigDecimal milk_dispatch_buffalo_fat) {
        this.milk_dispatch_buffalo_fat = milk_dispatch_buffalo_fat;
    }

    public BigDecimal getMilk_dispatch_mix_fat() {
        return milk_dispatch_mix_fat;
    }

    public void setMilk_dispatch_mix_fat(BigDecimal milk_dispatch_mix_fat) {
        this.milk_dispatch_mix_fat = milk_dispatch_mix_fat;
    }
}
