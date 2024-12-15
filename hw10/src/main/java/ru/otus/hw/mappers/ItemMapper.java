package ru.otus.hw.mappers;

import ru.otus.hw.dto.ItemDto;

public interface ItemMapper<E> {

    ItemDto toItem(E e);

}
