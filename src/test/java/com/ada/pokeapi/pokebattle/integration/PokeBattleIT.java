package com.ada.pokeapi.pokebattle.integration;

import com.ada.pokeapi.pokebattle.PokebattleApplication;
import com.ada.pokeapi.pokebattle.model.EvolutionChainResponse;
import com.ada.pokeapi.pokebattle.model.PokemonBattleRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoConfigureMockMvc
@SpringBootTest(classes = PokebattleApplication.class)
public class PokeBattleIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnStatusOkWhenReceiveAValidPokemonName() throws Exception {
        String pokemonName = "rattata";
        mockMvc.perform(MockMvcRequestBuilders
                .get("/pokemon/" + pokemonName)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldReturnStatusNotFoundWhenReceiveAInvalidPokemonName() throws Exception {
        String pokemonName = "ratata";
        mockMvc.perform(MockMvcRequestBuilders
                .get("/pokemon/" + pokemonName)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnAllFormsWhenReceiveAValidPokemonName() throws Exception {
        String pokemonName = "rattata";
        EvolutionChainResponse evolutionChainResponse = EvolutionChainResponse.builder()
                .forms(List.of("rattata", "raticate"))
                .build();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String responseBody = ow.writeValueAsString(evolutionChainResponse);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/pokemon/" + pokemonName + "/forms")
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andExpect(MockMvcResultMatchers.content().json(responseBody));
    }

    @Test
    public void shouldReturnStatusNotFoundWhenReceiveAInvalidPokemonNameToFindForms() throws Exception {
        String pokemonName = "ratata";
        mockMvc.perform(MockMvcRequestBuilders
                .get("/pokemon/" + pokemonName + "/forms")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnTheWinnerWhenReceiveTwoValidPokemonName() throws Exception {
        PokemonBattleRequest pokemonBattleRequest = PokemonBattleRequest.builder()
                .challenged("rattata")
                .challenger("pikachu")
                .build();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String requestBody = ow.writeValueAsString(pokemonBattleRequest);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("winner","pikachu");
        mockMvc.perform(MockMvcRequestBuilders
                .post("/pokemon")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andExpect(MockMvcResultMatchers.content().json(ow.writeValueAsString(responseBody)));
    }

    @Test
    public void shouldReturnDrawWhenReceiveSamePokemonNameAsChallengerAndChallenged() throws Exception {
        PokemonBattleRequest pokemonBattleRequest = PokemonBattleRequest.builder()
                .challenged("pikachu")
                .challenger("pikachu")
                .build();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String requestBody = ow.writeValueAsString(pokemonBattleRequest);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("winner","DRAW");
        mockMvc.perform(MockMvcRequestBuilders
                .post("/pokemon")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andExpect(MockMvcResultMatchers.content().json(ow.writeValueAsString(responseBody)));
    }

    @Test
    public void shouldReturnNotFoundWhenReceiveAnyInvalidPokemonNameAsChallenger() throws Exception {
        PokemonBattleRequest pokemonBattleRequest = PokemonBattleRequest.builder()
                .challenged("pikachu")
                .challenger("ratata")
                .build();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String requestBody = ow.writeValueAsString(pokemonBattleRequest);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/pokemon")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnNotFoundWhenReceiveAnyInvalidPokemonNameAsChallenged() throws Exception {
        PokemonBattleRequest pokemonBattleRequest = PokemonBattleRequest.builder()
                .challenged("ratata")
                .challenger("pikachu")
                .build();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String requestBody = ow.writeValueAsString(pokemonBattleRequest);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/pokemon")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
