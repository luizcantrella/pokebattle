package com.ada.pokeapi.pokebattle.controller;

import com.ada.pokeapi.pokebattle.model.*;
import com.ada.pokeapi.pokebattle.service.PokeBattleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
public class PokeBatllerControllerTest {

    @Mock
    private PokeBattleService service;

    @InjectMocks
    private PokeBattleController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
    }

    @Test
    public void testGetPokemon() throws Exception {

        Pokemon pokemon = Pokemon.builder()
            .id(1)
            .name("pikachu")
            .build();

        Mockito.when(service.findPokemon("pikachu")).thenReturn(pokemon);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/pokemon/pikachu")
        ).andExpect(MockMvcResultMatchers.status().isOk());


    }

    @Test
    public void testGetPokemonEvolution() throws Exception {

        Species species = Species.builder()
                .name("Species")
                .build();

        ChainLink chain = ChainLink.builder()
                .evolves_to(new ArrayList<>())
                .species(species)
                .build();

        EvoluationChain evoluationChain = EvoluationChain.builder()
                .chain(chain)
                .build();

        Mockito.when(service.findForms("pikachu")).thenReturn(evoluationChain);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/pokemon/pikachu/forms")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(asJsonString(evoluationChain)));

    }

    @Test
    public void testPostPokemonBatle() throws Exception {
        PokemonBattleRequest request = PokemonBattleRequest.builder()
                .challenged("caterpilar")
                .challenger("mewtwo")
                .build();

        HashMap<String, String> resultBatle = new HashMap<>();
        resultBatle.put("winner","mewtow");

        Mockito.when(service.executeBattle(request)).thenReturn("mewtow");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/pokemon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(resultBatle)));

    }

    @Test
    public void testPostPokemonBatleBadRequest() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/pokemon")
                ).andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
