package com.example.crowded.converter;

import com.example.crowded.converter.base.AbstractEntityConverter;
import com.example.crowded.database.entity.Persona;
import com.example.crowded.dto.PersonaDto;

public class PersonaConverter extends AbstractEntityConverter<Persona, PersonaDto> {

	@Override
	protected boolean isAmbiguousSupported() {
		return true;
	}

}
