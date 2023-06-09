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
        Pokemon pokemon;
        try {
            pokemon = pokeApiClient.getPokemonByName(name);
            List<LocationAreaEncounter> areasWhereCanFound = pokeApiClient.getAreasWhereCanFound(
                    pokemon.getId().toString()
            );
            log.info("areas where can be found: {}", areasWhereCanFound);
            pokemon.setAreaEncounters(areasWhereCanFound);

            //
            Integer sum = sumPokemonStats(pokemon);
            log.info("Pokémon {} has sum stats {}", pokemon.getName(), sum.toString());
        } catch (Exception e){
            log.info("pokemon não encontrado");
            pokemon = null;
        }
        return pokemon;
    }

    public EvolutionChainResponse findForms(String name) {
        log.info("GET Request: /pokemon-species/{}",name);
        EvolutionChainResponse evolutionChainResponse = new EvolutionChainResponse();
        try {
        PokemonSpecies pokemonSpeciesByName = pokeApiClient.getPokemonSpeciesByName(name);
        String url = pokemonSpeciesByName.getEvolution_chain().get("url");
        List<String> urlSplited = List.of(url.split("/"));
        String evolutionId = urlSplited.get(6);
        EvolutionChain evolutionChain = pokeApiClient.getEvolutionChainByUrl(evolutionId);
        evolutionChainResponse = new EvolutionChainResponse();
        List<ChainLink> evolvesTo = evolutionChain.getChain().getEvolves_to();
        evolutionChainResponse.getForms().add(evolutionChain.getChain().getSpecies().getName());
        while(evolvesTo.size() > 0) {
            var i = 0;
            while(i < evolvesTo.size()) {
                evolutionChainResponse.getForms().add(evolvesTo.get(i).getSpecies().getName());
                i++;
            }
            evolvesTo = evolvesTo.get(0).getEvolves_to();
        }
        } catch (Exception e){
            log.info("pokemon não encontrado");
            evolutionChainResponse = null;
        }
        return evolutionChainResponse;
    }

    private Integer sumPokemonStats(Pokemon pokemon) {
        return pokemon.getStats().stream().mapToInt(PokemonStat::getBase_stat).sum();
    }

    public String executeBattle(PokemonBattleRequest battleRequest) {
        Pokemon challenger = this.findPokemon(battleRequest.getChallenger());
        Pokemon challenged = this.findPokemon(battleRequest.getChallenged());
        if(challenged == null || challenger == null) return null;

        Integer challengerStats = sumPokemonStats(challenger);
        Integer challengedStats = sumPokemonStats(challenged);

        if(challengerStats>challengedStats) return challenger.getName();
        if(challengerStats<challengedStats) return challenged.getName();
        return "DRAW";
    }
}
