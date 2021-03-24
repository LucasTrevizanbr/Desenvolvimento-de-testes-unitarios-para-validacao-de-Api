package com.github.trevizanlucas.beerapi.service;


import com.github.trevizanlucas.beerapi.builder.BeerBuilderDTO;
import com.github.trevizanlucas.beerapi.dto.BeerDTO;
import com.github.trevizanlucas.beerapi.exception.BeerAlreadyRegisteredException;
import com.github.trevizanlucas.beerapi.mapper.BeerMapper;
import com.github.trevizanlucas.beerapi.model.Beer;
import com.github.trevizanlucas.beerapi.repository.BeerRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {
    private static final long INVALID_BEER_ID = 1l;

    @Mock
    private BeerRepository beerRepository;

    private BeerMapper beerMapper = BeerMapper.INSTANCE;

    @InjectMocks
    private BeerService beerService;

    @Test
    void quandoInformadaCervejaEntaoDeveSerCriada() throws BeerAlreadyRegisteredException {
//     given/dado (dados de entrada)
       BeerDTO expectedBeerDTO = BeerBuilderDTO.builder().build().toBeerDTO();//criou
       Beer expectedSavedBeer = beerMapper.toModel(expectedBeerDTO);//converteu para entidade

//     when/quando(simular as chamadas do método de criação de cerveja)
//     quando passar na verificação de nome, vai retornar um vazio para nao gerar excessão
       when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.empty());

//     salvando a cerveja esperada e retornando ela
       when(beerRepository.save(expectedSavedBeer)).thenReturn(expectedSavedBeer);

//     then/então
//     cria essa cerveja dto passando a beerdto e compara elas
       BeerDTO createdBeerDTO = beerService.createBeer(expectedBeerDTO);

//       compara se o id da cervejaDTO é igual ao id da cerveja criada, mesmo com o nome usando o junit
//       assertEquals(beerDTO.getId(),createdBeerDTO.getId());
//       assertEquals(beerDTO.getName(), createdBeerDTO.getName());

        //asserThat do hamcrest para confirmar se os dados da cerveja criada é igual ao do dtopassado
        assertThat(createdBeerDTO.getId(), is(equalTo(expectedBeerDTO.getId())));
        assertThat(createdBeerDTO.getName(), is(equalTo(expectedBeerDTO.getName())));
        assertThat(createdBeerDTO.getQuantity(), is(equalTo(expectedBeerDTO.getQuantity())));

        //Existem variados asserts para fazer diversas validações
        assertThat(createdBeerDTO.getQuantity(), is(greaterThan(2)));
    }

    @Test
    void quandoUmaCervejaJaCriadaInformadaEntaoLancarUmaExcessao() throws BeerAlreadyRegisteredException {
        //given
        BeerDTO expectedBeerDTO = BeerBuilderDTO.builder().build().toBeerDTO();
        Beer duplicatedBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(Optional.of(duplicatedBeer));

        //Verificando se lançou a excessão
        assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(expectedBeerDTO));


    }
}
