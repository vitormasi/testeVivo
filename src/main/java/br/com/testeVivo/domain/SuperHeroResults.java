package br.com.testeVivo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuperHeroResults implements Serializable {

    //melhor volta da corrida
    private SuperHeroLap bestLap;
    //dados de cada super herói
    private List<SuperHeroPosition> superHeroPositionList = new ArrayList<>();

}
