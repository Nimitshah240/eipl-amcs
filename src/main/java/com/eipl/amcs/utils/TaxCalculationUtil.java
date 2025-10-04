package com.eipl.amcs.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.model.TaxDetail;

public class TaxCalculationUtil {

//	private TaxDetailService taxDetailService;
	Tax tax = null;
	BigDecimal grossAmount = BigDecimal.valueOf(0);

	public TaxCalculationUtil(Tax tax, BigDecimal grossAmount) {
		this.tax = tax;
		this.grossAmount = grossAmount;
//		taxDetailService = SpringBootstrap.getContext().getBean(TaxDetailService.class);
	}

	public BigDecimal getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(BigDecimal grossAmount) {
		this.grossAmount = grossAmount;
	}

	public String calculateTaxReverese() {

//		double originalAmountAddition = 0;
//		double originalAmountDeduction = 0;
//
//		List<TaxDetail> taxDetail = null;
//
//		taxDetail = taxDetailService.fetchAllByTax(tax);
//
//		Map<TaxDetail, Double> taxes = new HashMap<>();
//
//		double baseAmount = 100;
//
//		for (TaxDetail td : taxDetail) {
//			if (td.getBasicTax().getCode() == 1) {
//				taxes.put(td, baseAmount);
//			}
//		}
//
//		for (int i = 0; i < taxDetail.size(); i++) {
//			double val = 0;
//
//			if (taxDetail.get(i).getBasicTax().getCode() != 1) {
//
//				List<TaxDepend> listDepends = taxDetailService.fetchAllDependencies(taxDetail.get(i));
//				double percent = taxDetail.get(i).getPercentage();
//				double amount = 0;
//
//				for (int j = 0; j < listDepends.size(); j++) {
//
//					TaxDepend td = listDepends.get(j);
//					if (td.getSteps() != 0) {
//						for (TaxDetail a : taxes.keySet()) {
//							if (a.getBasicTax().getCode() == td.getTaxDetail().getBasicTax()
//									.getCode()) {
//								Double value = taxes.get(a);
//								amount = amount + value;
//							}
//						}
//
//					}
//				}
//
//				if (taxDetail.get(i).getType() == 1) {
//					taxes.put(taxDetail.get(i), -(amount * percent) / 100);
//				} else {
//					taxes.put(taxDetail.get(i), (amount * percent) / 100);
//				}
//
//				val = percent;
//			}
//			taxDetail.get(i).setCalculatedTax(val);
//		}
//
//		for (Map.Entry<TaxDetail, Double> entry : taxes.entrySet()) {
//
//			if (entry.getKey().getType() == 0)
//				originalAmountAddition = originalAmountAddition + entry.getValue();
//			else
//				originalAmountDeduction = originalAmountDeduction + entry.getValue();
//
//		}
//		if (originalAmountAddition != 0)
//			originalAmountAddition = (grossAmount * 100) / originalAmountAddition;
//		if (originalAmountDeduction != 0)
//			originalAmountDeduction = (grossAmount * 100) / originalAmountDeduction;

//		return String.valueOf(originalAmountAddition - originalAmountDeduction);
		return "0";
	}

	public String calculateTax() {

//		double taxAmount = 0;
//		List<TaxDetail> taxDetail = null;
//
//		taxDetail = taxDetailService.fetchAllByTax(tax);
//
//		Map<Integer, Double> taxes = new HashMap<>();
//
//		taxes.put(1, grossAmount);
//
//		for (int i = 0; i < taxDetail.size(); i++) {
//			double val = 0;
//
//			if (taxDetail.get(i).getBasicTax().getCode() != 1) {
//
//				List<TaxDepend> listDepends = taxDetailService.fetchAllDependencies(taxDetail.get(i));
//				double percent = taxDetail.get(i).getPercentage();
//				double amount = 0;
//
//				for (int j = 0; j < listDepends.size(); j++) {
//
//					TaxDepend td = listDepends.get(j);
//					if (td.getSteps() != 0) {
//						Double value = taxes.get(td.getTaxDetails().getBasicTax().getCode());
//						amount = amount + value;
//					}
//				}
//				if (taxDetail.get(i).getType() == 1) {
//					taxes.put(taxDetail.get(i).getBasicTax().getCode(), -(amount * percent) / 100);
//				} else {
//					taxes.put(taxDetail.get(i).getBasicTax().getCode(), (amount * percent) / 100);
//				}
//
//				val = (amount * percent) / 100;
//			}
//			taxDetail.get(i).setCalculatedTax(val);
//		}
//
//		for (Map.Entry<Integer, Double> entry : taxes.entrySet()) {
//			taxAmount = taxAmount + entry.getValue();
//		}
//		return MainApp.DECIMAL_FORMAT_3_DIGIT.format(taxAmount - grossAmount);
		return "0";
	}

	public Map<TaxDetail, Double> calculateTaxWithDetail() {

//		List<TaxDetail> taxDetail = null;
//
//		taxDetail = taxDetailService.fetchAllByTax(tax);
//
//		Map<Integer, Double> taxes = new HashMap<>();
		Map<TaxDetail, Double> taxDetailValue = new HashMap<>();
//
//		taxes.put(1, grossAmount);
//
//		for (int i = 0; i < taxDetail.size(); i++) {
//			double val = 0;
//
//			if (taxDetail.get(i).getBasicTaxCode().getBasicTaxCode() != 1) {
//
//				List<TaxDepends> listDepends = taxDetailService.fetchAllDependencies(taxDetail.get(i));
//				double percent = taxDetail.get(i).getPercentage();
//				double amount = 0;
//
//				for (int j = 0; j < listDepends.size(); j++) {
//
//					TaxDepends td = listDepends.get(j);
//					if (td.getSteps() != 0) {
//						Double value = taxes.get(td.getTaxDetailsId().getBasicTaxCode().getBasicTaxCode());
//						amount = amount + value;
//					}
//				}
//				val = NumberUtil.round((amount * percent) / 100, 2);
//				if (taxDetail.get(i).getType() == 1) {
//					taxes.put(taxDetail.get(i).getBasicTaxCode().getBasicTaxCode(), -val);
//				} else {
//					taxes.put(taxDetail.get(i).getBasicTaxCode().getBasicTaxCode(), val);
//				}
//
//			}
//			taxDetailValue.put(taxDetail.get(i), val);
//			taxDetail.get(i).setCalculatedTax(val);
//		}
//		return taxDetailValue;

		return taxDetailValue;
	}

}
