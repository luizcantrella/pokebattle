package com.ada.pokeapi.pokebattle.service;

import com.ada.pokeapi.pokebattle.client.PokeApiClient;
import com.ada.pokeapi.pokebattle.model.*;
import com.google.common.math.Stats;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PokeBatleService {

    @Mock
    PokeApiClient pokeApi;

    @InjectMocks
    PokeBattleService service;

    private String nome;

    @BeforeEach
    public void setup(){
        nome = "pikachu";
    }

    @Test
    public void testFindPokemon() throws Exception {
        Pokemon pokemon = Pokemon.builder()
                .id(1)
                .stats(List.of(PokemonStat.builder().base_stat(1).build()))
                .build();

        List<LocationAreaEncounter> areasWhereCanFound = new ArrayList<>();

        Mockito.when(pokeApi.getPokemonByName(nome)).thenReturn(pokemon);
        Mockito.when(pokeApi.getAreasWhereCanFound(Mockito.anyString()))
                .thenReturn(areasWhereCanFound);

        Pokemon poke = service.findPokemon(nome);
        Assert.notNull(poke, "Pokemon não é nulo");
    }

    @Test
    public void testFindForms(){
        HashMap<String, String> url = new HashMap<>();
        url.put("url","/url/url/url/url/url/url/url");

        EvoluationChain evoluationChain = EvoluationChain.builder().build();

        PokemonSpecies species = PokemonSpecies.builder()
                .evolution_chain(url)
                .build();

        Mockito.when(pokeApi.getPokemonSpeciesByName(nome))
                .thenReturn(species);

        Mockito.when(pokeApi.getEvolutionChainByUrl(Mockito.anyString()))
                .thenReturn(evoluationChain);

        EvoluationChain result = service.findForms(nome);

        Assert.notNull(result, "Pokemon não é nulo");
    }

    @Test
    public void testExecuteBattleChallengedWins(){

        Pokemon challenger = Pokemon.builder()
                .id(10)
                .name("mewtwo")
                .stats(List.of(PokemonStat.builder().base_stat(10).build()))
                .build();

        Pokemon challenged = Pokemon.builder()
                .id(1)
                .name("caterpilar")
                .stats(List.of(PokemonStat.builder().base_stat(20).build()))
                .build();

        PokemonBattleRequest request = PokemonBattleRequest.builder()
            .challenged("caterpilar")
            .challenger("mewtwo")
            .build();

        List<LocationAreaEncounter> areasWhereCanFound = new ArrayList<>();

        Mockito.when(pokeApi.getPokemonByName(request.getChallenged()))
                .thenReturn(challenged);
        Mockito.when(pokeApi.getPokemonByName(request.getChallenger()))
                .thenReturn(challenger);

        Mockito.when(pokeApi.getAreasWhereCanFound(Mockito.anyString()))
                .thenReturn(areasWhereCanFound);

        String result = service.executeBattle(request);
        Assertions.assertEquals("caterpilar",result);
    }

    @Test
    public void testExecuteBattleChallengerWins(){

        Pokemon challenger = Pokemon.builder()
                .id(10)
                .name("mewtwo")
                .stats(List.of(PokemonStat.builder().base_stat(20).build()))
                .build();

        Pokemon challenged = Pokemon.builder()
                .id(1)
                .name("caterpilar")
                .stats(List.of(PokemonStat.builder().base_stat(10).build()))
                .build();

        PokemonBattleRequest request = PokemonBattleRequest.builder()
                .challenged("caterpilar")
                .challenger("mewtwo")
                .build();

        List<LocationAreaEncounter> areasWhereCanFound = new ArrayList<>();

        Mockito.when(pokeApi.getPokemonByName(request.getChallenged()))
                .thenReturn(challenged);
        Mockito.when(pokeApi.getPokemonByName(request.getChallenger()))
                .thenReturn(challenger);

        Mockito.when(pokeApi.getAreasWhereCanFound(Mockito.anyString()))
                .thenReturn(areasWhereCanFound);

        String result = service.executeBattle(request);
        Assertions.assertEquals("mewtwo",result);
    }

    @Test
    public void testExecuteBattleDraw(){

        Pokemon challenger = Pokemon.builder()
                .id(10)
                .name("mewtwo")
                .stats(List.of(PokemonStat.builder().base_stat(10).build()))
                .build();

        Pokemon challenged = Pokemon.builder()
                .id(1)
                .name("caterpilar")
                .stats(List.of(PokemonStat.builder().base_stat(10).build()))
                .build();

        PokemonBattleRequest request = PokemonBattleRequest.builder()
                .challenged("caterpilar")
                .challenger("mewtwo")
                .build();

        List<LocationAreaEncounter> areasWhereCanFound = new ArrayList<>();

        Mockito.when(pokeApi.getPokemonByName(request.getChallenged()))
                .thenReturn(challenged);
        Mockito.when(pokeApi.getPokemonByName(request.getChallenger()))
                .thenReturn(challenger);

        Mockito.when(pokeApi.getAreasWhereCanFound(Mockito.anyString()))
                .thenReturn(areasWhereCanFound);

        String result = service.executeBattle(request);
        Assertions.assertEquals("DRAW",result);
    }

}
