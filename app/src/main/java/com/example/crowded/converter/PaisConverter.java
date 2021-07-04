package com.example.crowded.converter;

import com.example.crowded.converter.base.AbstractEntityConverter;
import com.example.crowded.database.entity.Pais;
import com.example.crowded.dto.PaisDto;

public class PaisConverter extends AbstractEntityConverter<Pais, PaisDto> {

	@Override
	protected boolean isAmbiguousSupported() {
		return true;
	}

}
