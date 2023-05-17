package com.ada.pokeapi.pokebattle.client;

import com.ada.pokeapi.pokebattle.model.EvolutionChain;
import com.ada.pokeapi.pokebattle.model.LocationAreaEncounter;
import com.ada.pokeapi.pokebattle.model.Pokemon;
import com.ada.pokeapi.pokebattle.model.PokemonSpecies;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="pokeapi", url = "https://pokeapi.co/api/v2")
public interface PokeApiClient {

    @GetMapping("/pokemon/{name}")
    Pokemon getPokemonByName(@PathVariable("name") String name);

    @GetMapping("/pokemon-species/{name}")
    PokemonSpecies getPokemonSpeciesByName(@PathVariable("name") String name);

    @GetMapping("/pokemon/{id}/encounters")
    List<LocationAreaEncounter> getAreasWhereCanFound(@PathVariable("id") String name);

    @GetMapping("/evolution-chain/{id}")
    EvolutionChain getEvolutionChainByUrl(@PathVariable ("id") String url);
}
