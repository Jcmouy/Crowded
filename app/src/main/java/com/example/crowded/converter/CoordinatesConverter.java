package com.example.crowded.converter;

import com.example.crowded.converter.base.AbstractEntityConverter;
import com.example.crowded.database.entity.Coordinates;
import com.example.crowded.dto.CoordinatesDto;

public class CoordinatesConverter extends AbstractEntityConverter<Coordinates, CoordinatesDto> {

	@Override
	protected boolean isAmbiguousSupported() {
		return true;
	}

}
