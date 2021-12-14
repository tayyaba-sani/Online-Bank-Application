package com.cards.cards.mappers;

import com.cards.cards.dtos.CardDetails;
import com.cards.cards.model.Cards;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    @Named("entityToDto")
    CardDetails entityToDto(Cards cards);

    @IterableMapping(qualifiedByName = "entityToDto")
    List<CardDetails> entityListToDtoList(List<Cards> cardsList);
}
