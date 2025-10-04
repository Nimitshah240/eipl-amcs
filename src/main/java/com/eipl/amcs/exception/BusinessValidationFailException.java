package com.eipl.amcs.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.validation.FieldError;

@SuppressWarnings("serial")
public class BusinessValidationFailException extends RuntimeException {
	private static List<FieldError> listFieldErrors;

	public List<FieldError> getListFieldErrors() {
		return listFieldErrors;
	}
	
	public BusinessValidationFailException(Class<?> clazz, FieldError... fieldError) {
		super(BusinessValidationFailException.generateMessage(clazz.getSimpleName(), toList(fieldError)));
	}

	private static String generateMessage(String entity, List<FieldError> listFieldErrors) {
		return capitalize(entity) + " FieldErrors " + listFieldErrors.toString();
	}

	private static List<FieldError> toList(FieldError... entries) {
		listFieldErrors = Arrays.asList(entries);
		return listFieldErrors;
	}

	private static String capitalize(final String str) {
		final int strLen = str.length();
		if (strLen == 0) {
			return str;
		}

		final int firstCodepoint = str.codePointAt(0);
		final int newCodePoint = Character.toTitleCase(firstCodepoint);
		if (firstCodepoint == newCodePoint) {
			return str;
		}

		final int[] newCodePoints = new int[strLen];
		int outOffset = 0;
		newCodePoints[outOffset++] = newCodePoint;
		for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen;) {
			final int codepoint = str.codePointAt(inOffset);
			newCodePoints[outOffset++] = codepoint;
			inOffset += Character.charCount(codepoint);
		}
		return new String(newCodePoints, 0, outOffset);
	}
}
