package com.example.crowded.converter;

import com.example.crowded.converter.base.AbstractEntityConverter;
import com.example.crowded.database.entity.Usuario;
import com.example.crowded.dto.UsuarioDto;

public class UsuarioConverter extends AbstractEntityConverter<Usuario, UsuarioDto> {

	@Override
	protected boolean isAmbiguousSupported() {
		return true;
	}

}
