package com.eipl.amcs.report.dto;

import com.eipl.amcs.report.annotation.ReportDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

@NoArgsConstructor
@Getter
@Setter
//@SqlResultSetMapping(name = "ShiftReportCode", entities = { 
//		@EntityResult(entityClass = ShiftReportCode.class, fields = {
//				@FieldResult(name = "sr", column = "sr"),
//				@FieldResult(name = "societyCode", column = "society_code"),
//				@FieldResult(name = "memberCode", column = "member_code"),
//				@FieldResult(name = "collectionDate", column = "collection_date"),
//				@FieldResult(name = "shiftName", column = "shift_name"),
//				@FieldResult(name = "milkTypeName", column = "milk_type_name"),
//				@FieldResult(name = "sampleNo", column = "sample_no"),
//				@FieldResult(name = "qty", column = "qty"),
//				@FieldResult(name = "fat", column = "fat"),
//				@FieldResult(name = "snf", column = "snf"),
//				@FieldResult(name = "amount", column = "amount")
//		}) })
@ReportDto
public class ShiftReportCode {
//	private int sr;
//	private String societyCode;
//	private String societyName;
//	private String memberCode;
//	private LocalDate collectionDate;
//	private String shiftName;
//	private String milkTypeName;
//	private int sampleNo;
//	private BigDecimal qty;
//	private BigDecimal fat;
//	private BigDecimal snf;
//	private BigDecimal amount;
//	private int cowMemberCount;
//	private int buffaloMemberCount;
//	private int mixMemberCount;
//	private BigDecimal cowQty;
//	private BigDecimal buffaloQty;
//	private BigDecimal mixQty;
//	private BigDecimal cowAmount;
//	private BigDecimal buffaloAmount;
//	private BigDecimal mixAmount;
//
//	private BigDecimal localCowQty;
//	private BigDecimal localBuffaloQty;
//	private BigDecimal localMixQty;
//	private BigDecimal localCowAmount;
//	private BigDecimal localBuffaloAmount;
//	private BigDecimal localMixAmount;


    private BigInteger sr;
    private String society_code;
    private String society_name;
    private String member_code;
    private Date collection_date;
    private String shift_name;
    private String milk_type_name;
    private BigInteger sample_no;
    private BigDecimal qty;
    private BigDecimal fat;
    private BigDecimal snf;
    private BigDecimal amount;
    private BigInteger cow_member_count;
    private BigInteger buffalo_member_count;
    private BigInteger mix_member_count;
    private BigDecimal cow_qty;
    private BigDecimal buffalo_qty;
    private BigDecimal mix_qty;
    private BigDecimal cow_amount;
    private BigDecimal buffalo_amount;
    private BigDecimal mix_amount;

    private BigDecimal local_cow_qty;
    private BigDecimal local_buffalo_qty;
    private BigDecimal local_mix_qty;
    private BigDecimal local_cow_amount;
    private BigDecimal local_buffalo_amount;
    private BigDecimal local_mix_amount;
}
