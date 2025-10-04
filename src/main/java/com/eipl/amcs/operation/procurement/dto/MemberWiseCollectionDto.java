package com.eipl.amcs.operation.procurement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.eipl.amcs.operation.procurement.model.MilkCollection;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MemberWiseCollectionDto {
	private Map<String, BigDecimal> avg;
	private Map<String, BigDecimal> total;
	private List<MilkCollection> collectionList;

//	public MemberWiseCollectionDto() {
//	}
//
//	public MemberWiseCollectionDto(Map<String, BigDecimal> avg, Map<String, BigDecimal> total, List<MilkCollection> collectionList) {
//		this.avg = avg;
//		this.total = total;
//		this.collectionList = collectionList;
//	}
//
//	public Map<String, BigDecimal> getAvg() {
//		return avg;
//	}
//
//	public void setAvg(Map<String, BigDecimal> avg) {
//		this.avg = avg;
//	}
//
//	public Map<String, BigDecimal> getTotal() {
//		return total;
//	}
//
//	public void setTotal(Map<String, BigDecimal> total) {
//		this.total = total;
//	}
//
//	public List<MilkCollection> getCollectionList() {
//		return collectionList;
//	}
//
//	public void setCollectionList(List<MilkCollection> collectionList) {
//		this.collectionList = collectionList;
//	}
}
