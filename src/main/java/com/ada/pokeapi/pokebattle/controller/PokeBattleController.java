package com.ada.pokeapi.pokebattle.controller;

import com.ada.pokeapi.pokebattle.model.EvoluationChain;
import com.ada.pokeapi.pokebattle.model.Pokemon;
import com.ada.pokeapi.pokebattle.model.PokemonBattleRequest;
import com.ada.pokeapi.pokebattle.service.PokeBattleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("pokemon")
public class PokeBattleController {

    private final PokeBattleService pokeBattleService;

    @GetMapping("{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Pokemon> getPokemon(@PathVariable("name") String name) {
        Pokemon pokemon = pokeBattleService.findPokemon(name);
        return ResponseEntity.ok().body(pokemon);
    }

    @GetMapping("{name}/forms")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EvoluationChain> getForms(@PathVariable("name") String name) {
        EvoluationChain evoluationChain = pokeBattleService.findForms(name);
        return ResponseEntity.ok().body(evoluationChain);
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> executeBattle(@RequestBody PokemonBattleRequest battleRequest) {
        Map<String, String> response = new HashMap<>();
        String battleWinner = pokeBattleService.executeBattle(battleRequest);
        response.put("winner", battleWinner);
        return ResponseEntity.ok().body(response);
    }

}
