package com.eipl.amcs.utils;

public class Number2Word {
	public static String convertToWord(String number) {
		String twodigitword = "";
		String word = "";
		String[] HTLC = { "", "Hundred", "Thousand", "Lakh", "Crore" }; // H-hundread
		int split[] = { 0, 2, 3, 5, 7, 9 };
		String[] temp = new String[split.length];
		boolean addzero = true;
		int len1 = number.length();
		if (len1 > split[split.length - 1]) {
			System.out.println("Error. Maximum Allowed digits " + split[split.length - 1]);
			System.exit(0);
		}
		if (Integer.parseInt(number) == 0)
			return "Zero";
		for (int l = 1; l < split.length; l++)
			if (number.length() == split[l])
				addzero = false;
		if (addzero == true)
			number = "0" + number;
		int len = number.length();
		int j = 0;
		// spliting & putting numbers in temp array.
		while (split[j] < len) {
			int beg = len - split[j + 1];
			int end = beg + split[j + 1] - split[j];
			temp[j] = number.substring(beg, end);
			j = j + 1;
		}
		for (int k = 0; k < j; k++) {
			twodigitword = ConvertOnesTwos(temp[k]);
			if (k >= 1) {
				if (twodigitword.trim().length() != 0)
					word = twodigitword + " " + HTLC[k] + " " + word;
			} else
				word = twodigitword;
		}
		return (word);
	}

	private static String ConvertOnesTwos(String t) {
		final String[] ones = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
				"Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen" };
		final String[] tens = { "", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty",
				"Ninety" };
		String word = "";
		int num = Integer.parseInt(t);
		if (num % 10 == 0)
			word = tens[num / 10] + " " + word;
		else if (num < 20)
			word = ones[num] + " " + word;
		else {
			word = tens[(num - (num % 10)) / 10] + word;
			word = word + " " + ones[num % 10];
		}
		return word;
	}
	// private static final String[] lowNames = { "Zero", "One", "Two", "Three",
	// "Four", "Five", "Six", "Seven", "Eight",
	// "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen",
	// "Sixteen", "Seventeen", "Eighteen",
	// "Nineteen" };
	//
	// private static final String[] tensNames = { "Twenty", "Thirty", "Forty",
	// "Fifty", "Sixty", "Seventy", "Eighty",
	// "Ninety" };
	//
	// private static final String[] bigNames = { "Thousand", "Million", "Billion"
	// };
	//
	// public static String convertNumberToWords(int n) {
	// if (n < 0) {
	// return "minus " + convertNumberToWords(-n);
	// }
	// if (n <= 999) {
	// return convert999(n);
	// }
	// String s = null;
	// int t = 0;
	// while (n > 0) {
	// if (n % 1000 != 0) {
	// String s2 = convert999(n % 1000);
	// if (t > 0) {
	// s2 = s2 + " " + bigNames[t - 1];
	// }
	// if (s == null) {
	// s = s2;
	// } else {
	// s = s2 + ", " + s;
	// }
	// }
	// n /= 1000;
	// t++;
	// }
	// return s;
	// }
	//
	// // Range 0 to 999.
	// private static String convert999(int n) {
	// String s1 = lowNames[n / 100] + " Hundred";
	// String s2 = convert99(n % 100);
	// if (n <= 99) {
	// return s2;
	// } else if (n % 100 == 0) {
	// return s1;
	// } else {
	// return s1 + " " + s2;
	// }
	// }
	//
	// // Range 0 to 99.
	// private static String convert99(int n) {
	// if (n < 20) {
	// return lowNames[n];
	// }
	// String s = tensNames[n / 10 - 2];
	// if (n % 10 == 0) {
	// return s;
	// }
	// return s + "-" + lowNames[n % 10];
	// }
}