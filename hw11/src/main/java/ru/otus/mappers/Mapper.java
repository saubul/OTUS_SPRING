package ru.otus.mappers;

public interface Mapper<E, D> {

    E toEntity(D dto);

    D toDto(E entity);

}
