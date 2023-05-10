package com.ada.pokeapi.pokebattle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pokemon {

    private Integer id;
    private String name;
    private Integer height;
    private Integer weight;
    private List<LocationAreaEncounter> areaEncounters;
    private List<PokemonStat> stats;
    private List<PokemonType> types;
}
