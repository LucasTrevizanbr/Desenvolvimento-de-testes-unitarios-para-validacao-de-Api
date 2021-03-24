package com.github.trevizanlucas.beerapi.controller;

import com.github.trevizanlucas.beerapi.builder.BeerBuilderDTO;
import com.github.trevizanlucas.beerapi.dto.BeerDTO;
import com.github.trevizanlucas.beerapi.exception.BeerAlreadyRegisteredException;
import com.github.trevizanlucas.beerapi.service.BeerService;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static com.github.trevizanlucas.beerapi.utils.JsonConvertionUtils.asJsonString;

import org.springframework.data.web.JsonPath;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class BeerControllerTest {

    private static final String BEER_API_URL_PATH = "/api/v1/beers";
    private static final long VALID_BEER_ID = 1L;
    private static final long INVALID_BEER_ID = 2l;
    private static final String BEER_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String BEER_API_SUBPATH_DECREMENT_URL = "/decrement";

    private MockMvc mockMvc;

    @Mock
    private BeerService beerService;

    @InjectMocks
    private BeerController beerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(beerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void quandoPOSTChamadoCervejaCriada() throws Exception {
        //when
        BeerDTO beerDto = BeerBuilderDTO.builder().build().toBeerDTO();

        //when
        Mockito.when(beerService.createBeer(beerDto)).thenReturn(beerDto);

        //then
        mockMvc.perform(post(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(beerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",is(beerDto.getName())))
                .andExpect(jsonPath("$.brand",is(beerDto.getBrand())))
                .andExpect(jsonPath("$.type", is(beerDto.getType().toString())));


    }

    @Test
    void quandoPOSTChamadoSemCampoObrigatorioEntaoRetornarErro() throws Exception {
        //when
        BeerDTO beerDto = BeerBuilderDTO.builder().build().toBeerDTO();
        beerDto.setBrand(null);

        //then
        mockMvc.perform(post(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(beerDto)))
                .andExpect(status().isBadRequest());
    }
}
