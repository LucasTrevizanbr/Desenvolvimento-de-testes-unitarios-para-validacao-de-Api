package com.github.trevizanlucas.beerapi.mapper;

import com.github.trevizanlucas.beerapi.dto.BeerDTO;
import com.github.trevizanlucas.beerapi.model.Beer;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);

    Beer toModel(BeerDTO beerDTO);

    BeerDTO toDTO(Beer beer);
}
