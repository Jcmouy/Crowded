package com.example.crowded.converter.base;

import java.util.List;

public interface IEntityConverter<T, D> {

	public List<D> mapListEntityToListDto(List<T> listEntity);

	public List<T> mapListDtoToListEntity(List<D> listEntityDto);

	public D mapEntityToDto(T entity);

	public T mapDtoToEntity(D entityDto);
}
