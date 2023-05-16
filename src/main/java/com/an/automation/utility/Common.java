package com.an.automation.utility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.an.automation.report.Log;

public class Common {
	private static final String LOWER_ALPHA = "abcdefghijklmnopqrstuvwxyz";
	private static final String UPPER_ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMERIC = "0123456789";

	public static String randomAlphaNumeric(int stringLength) {
		String base = LOWER_ALPHA + UPPER_ALPHA + NUMERIC;
		StringBuilder builder = new StringBuilder();
		while (stringLength-- != 0) {
			int character = (int) (Math.random() * base.length());
			builder.append(base.charAt(character));
		}
		return builder.toString();
	}

	public static String randomLowerAlpha(int stringLength) {
		StringBuilder builder = new StringBuilder();
		while (stringLength-- != 0) {
			int character = (int) (Math.random() * LOWER_ALPHA.length());
			builder.append(LOWER_ALPHA.charAt(character));
		}
		return builder.toString();
	}

	public static String randomNumeric(int stringLength) {
		StringBuilder builder = new StringBuilder();
		while (stringLength-- != 0) {
			int character = (int) (Math.random() * NUMERIC.length());
			builder.append(NUMERIC.charAt(character));
		}
		return builder.toString();
	}

	public static String throwableToString(Throwable e) {
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw);) {
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception exception) {
			Log.error("Cannot convert Throwable to String");
			e.printStackTrace();
		}
		return "";
	}

	public static String generateEmail() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss");
		String date = simpleDateFormat.format(new Date());
		String email = "AutoFranki_" + date + "@gmail.com";
		return email;
	}

	public static <T> boolean compareList(ArrayList<T> list1, ArrayList<T> list2) {
		if (list1.size() != list2.size()) {
			return false;
		}

		for (int i = 0; i < list1.size(); i++) {
			T item1 = list1.get(i);
			if (!list2.stream().anyMatch(item2 -> item2.equals(item1))) {
				return false;
			}
		}

		return true;
	}

	public static List<Integer> extractNumberFromString(String str) {
		List<Integer> numbers = new ArrayList<Integer>();
		Pattern p = Pattern.compile("\\d+");
		Matcher matcher = p.matcher(str);
		while (matcher.find()) {
			String num = matcher.group();
			System.out.print(num);
			numbers.add(Integer.valueOf(num));
		}
		
		return numbers;
	}
	
}
