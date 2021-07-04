package com.example.crowded.converter.base;

import org.modelmapper.ModelMapper;
import org.modelmapper.spi.ConditionalConverter;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;


public abstract class AbstractEntityConverter<T, D> implements IEntityConverter<T, D> {

	private ModelMapper modelMapper = new ModelMapper();

	protected ModelMapper getModelMapper() {
		return modelMapper;
	}

	protected boolean isAmbiguousSupported() {
		return false;
	}

	@PostConstruct
	private void init() {
		eliminarBooleanConverter(modelMapper.getConfiguration().getConverters());
		modelMapper.getConfiguration().getConverters().add(new CustomBooleanConverter());
		modelMapper.getConfiguration().setAmbiguityIgnored(isAmbiguousSupported());
	}

	public List<D> mapListEntityToListDto(List<T> listEntity) {
		List<D> result = new ArrayList<>();
		for (int i=0;i<listEntity.size();i++) {
			result.add(mapEntityToDto(listEntity.get(i)));
		}
		return result;
	}

	public List<T> mapListDtoToListEntity(List<D> listEntidadDto) {
		List<T> result = new ArrayList<>();
		for (int i=0;i<listEntidadDto.size();i++) {
			result.add(mapDtoToEntity(listEntidadDto.get(i)));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public D mapEntityToDto(T entidad) {
		modelMapper.getConfiguration().setAmbiguityIgnored(isAmbiguousSupported());
		return modelMapper.map(entidad,
				((Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]));
	}

	@SuppressWarnings("unchecked")
	public T mapDtoToEntity(D entidadDto) {
		modelMapper.getConfiguration().setAmbiguityIgnored(isAmbiguousSupported());
		T t = (T) modelMapper.map(entidadDto,
				((Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]));
		return t;
	}

	private static final String BOOLEAN_CONVERTER = "BooleanConverter";
	public static void eliminarBooleanConverter(List<ConditionalConverter<?, ?>> list) {
		for (int i=0;i<list.size();i++){
			removeConverterIfBoolean(list, list.get(i));
		}
	}

	private static void removeConverterIfBoolean(List<ConditionalConverter<?, ?>> converters,
                                                 ConditionalConverter<?, ?> converterItem) {
		if (converterItem.getClass().getSimpleName().equals(BOOLEAN_CONVERTER))
			converters.remove(converterItem);
	}
}
