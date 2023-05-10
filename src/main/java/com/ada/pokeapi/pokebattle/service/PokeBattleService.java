package com.ada.pokeapi.pokebattle.service;

import com.ada.pokeapi.pokebattle.client.PokeApiClient;
import com.ada.pokeapi.pokebattle.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PokeBattleService {

    private final PokeApiClient pokeApiClient;

    public Pokemon findPokemon(String name) {
        Pokemon pokemon = pokeApiClient.getPokemonByName(name);
        List<LocationAreaEncounter> areasWhereCanFound = pokeApiClient.getAreasWhereCanFound(
                pokemon.getId().toString()
        );
        log.info("areas where can be found: {}", areasWhereCanFound);
        pokemon.setAreaEncounters(areasWhereCanFound);

        //
        Integer sum = sumPokemonStats(pokemon);
        log.info("Pokémon {} has sum stats {}", pokemon.getName(), sum.toString());
        return pokemon;
    }

    public EvoluationChain findForms(String name) {
        log.info("GET Request: /pokemon-species/{}",name);
        PokemonSpecies pokemonSpeciesByName = pokeApiClient.getPokemonSpeciesByName(name);
        String url = pokemonSpeciesByName.getEvolution_chain().get("url");
        List<String> urlSplited = List.of(url.split("/"));
        String evolutionId = urlSplited.get(6);
        return pokeApiClient.getEvolutionChainByUrl(evolutionId);
    }

    private Integer sumPokemonStats(Pokemon pokemon) {
        return pokemon.getStats().stream().mapToInt(PokemonStat::getBase_stat).sum();
    }

    public String executeBattle(PokemonBattleRequest battleRequest) {
        Pokemon challenger = this.findPokemon(battleRequest.getChallenger());
        Pokemon challenged = this.findPokemon(battleRequest.getChallenged());

        Integer challengerStats = sumPokemonStats(challenger);
        Integer challengedStats = sumPokemonStats(challenged);

        if(challengerStats>challengedStats) return challenger.getName();
        if(challengerStats<challengedStats) return challenged.getName();
        return "DRAW";
    }
}
