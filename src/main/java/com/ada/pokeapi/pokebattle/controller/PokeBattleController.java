package com.ada.pokeapi.pokebattle.controller;

import com.ada.pokeapi.pokebattle.model.EvolutionChain;
import com.ada.pokeapi.pokebattle.model.EvolutionChainResponse;
import com.ada.pokeapi.pokebattle.model.Pokemon;
import com.ada.pokeapi.pokebattle.model.PokemonBattleRequest;
import com.ada.pokeapi.pokebattle.service.PokeBattleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("pokemon")
public class PokeBattleController {

    private final PokeBattleService pokeBattleService;

    @GetMapping("{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Pokemon> getPokemon(@PathVariable("name") String name) {
        Pokemon pokemon = pokeBattleService.findPokemon(name);
        return Objects.nonNull(pokemon) ? ResponseEntity.ok().body(pokemon) : ResponseEntity.notFound().build();

    }

    @GetMapping("{name}/forms")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EvolutionChainResponse> getForms(@PathVariable("name") String name) {
        EvolutionChainResponse evoluationChain = pokeBattleService.findForms(name);
        return Objects.nonNull(evoluationChain) ? ResponseEntity.ok().body(evoluationChain) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> executeBattle(@RequestBody PokemonBattleRequest battleRequest) {
        Map<String, String> response = new HashMap<>();
        String battleWinner = pokeBattleService.executeBattle(battleRequest);
        response.put("winner", battleWinner);
        return Objects.nonNull(battleWinner) ? ResponseEntity.ok().body(response) : ResponseEntity.notFound().build();
    }

}
