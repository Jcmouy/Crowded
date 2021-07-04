package com.example.crowded.converter.base;

import org.modelmapper.internal.Errors;
import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;

public class CustomBooleanConverter implements ConditionalConverter<Object, Boolean> {
	private static final String[] TRUE_STRINGS = { "s", "true", "yes", "y", "on", "1" };
	private static final String[] FALSE_STRINGSS = { "false", "no", "n", "off", "0" };

	public Boolean convert(MappingContext<Object, Boolean> context) {
		Object source = context.getSource();
		if (source == null)
			return null;

		String stringValue = source.toString().toLowerCase();
		if (stringValue.length() == 0)
			return null;

		for (int i = 0; i < TRUE_STRINGS.length; i++)
			if (TRUE_STRINGS[i].equals(stringValue))
				return Boolean.TRUE;

		for (int i = 0; i < FALSE_STRINGSS.length; i++)
			if (FALSE_STRINGSS[i].equals(stringValue))
				return Boolean.FALSE;

		throw new Errors().errorMapping(context.getSource(), context.getDestinationType()).toMappingException();
	}

	public MatchResult match(Class<?> sourceType, Class<?> destinationType) {
		boolean destMatch = destinationType == Boolean.class || destinationType == Boolean.TYPE;

		return destMatch ? matchSource(sourceType) : MatchResult.NONE;
	}

	private MatchResult matchSource(Class<?> sourceType) {
		boolean sourceMatch = sourceType == Boolean.class || sourceType == Boolean.TYPE;
		return sourceMatch ? MatchResult.FULL : MatchResult.PARTIAL;
	}
}
