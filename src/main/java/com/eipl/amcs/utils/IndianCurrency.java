
package com.eipl.amcs.utils;

public class IndianCurrency {

	public static String getIndianCurrencyFormat(double amount) {

		StringBuilder stringBuilder = new StringBuilder();
		String temp = String.format("%.2f", amount);
		char[] amountArray = temp.toCharArray();
		int a = 0, b = 0, c = 0;

		for (int i = amountArray.length - 1; i >= 0; i--) {
			if (c < 3) {
				stringBuilder.append(amountArray[i]);
				c++;
			} else if (a < 3) {
				stringBuilder.append(amountArray[i]);
				a++;
			} else if (b < 2) {
				if (b == 0) {
					stringBuilder.append(",");
					stringBuilder.append(amountArray[i]);
					b++;
				} else {
					stringBuilder.append(amountArray[i]);
					b = 0;
				}
			}
		}

		return stringBuilder.reverse().toString().replaceAll("-,", "-");
	}

}
