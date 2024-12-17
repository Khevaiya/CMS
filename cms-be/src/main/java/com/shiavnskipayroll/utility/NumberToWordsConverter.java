package com.shiavnskipayroll.utility;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;

@UtilityClass
public class NumberToWordsConverter {

	private static final String[] tensNames = { "", " Ten", " Twenty", " Thirty", " Forty", " Fifty", " Sixty",
			" Seventy", " Eighty", " Ninety" };

	private static final String[] numNames = { "", " One", " Two", " Three", " Four", " Five", " Six", " Seven",
			" Eight", " Nine", " Ten", " Eleven", " Twelve", " Thirteen", " Fourteen", " Fifteen", " Sixteen",
			" Seventeen", " Eighteen", " Nineteen" };

	private static String convertLessThanOneThousand(int number) {
		String current;

		if (number % 100 < 20) {
			current = numNames[number % 100];
			number /= 100;
		} else {
			current = numNames[number % 10];
			number /= 10;

			current = tensNames[number % 10] + current;
			number /= 10;
		}
		if (number == 0)
			return current;
		return numNames[number] + " Hundred" + current;
	}

	public static String convert(long number) {

		if (number == 0) {
			return "Zero";
		}

		String snumber = Long.toString(number);

		String mask = "000000000";
		DecimalFormat df = new DecimalFormat(mask);
		snumber = df.format(number);

		int millions = Integer.parseInt(snumber.substring(0, 3));

		int hundredThousands = Integer.parseInt(snumber.substring(3, 6));

		int thousands = Integer.parseInt(snumber.substring(6, 9));

		String tradMillions;
		switch (millions) {
		case 0:
			tradMillions = "";
			break;
		case 1:
			tradMillions = " One Million ";
			break;
		default:
			tradMillions = convertLessThanOneThousand(millions) + " Million ";
		}

		String result = tradMillions;

		String tradHundredThousands;
		switch (hundredThousands) {
		case 0:
			tradHundredThousands = "";
			break;
		case 1:
			tradHundredThousands = " One thousand ";
			break;
		default:
			tradHundredThousands = convertLessThanOneThousand(hundredThousands) + " Thousand ";
		}

		result += tradHundredThousands;

		String tradThousand;
		tradThousand = convertLessThanOneThousand(thousands);
		result += tradThousand;

		return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ") + " Only";
	}
}
