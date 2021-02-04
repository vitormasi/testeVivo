package br.com.testeVivo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuperHeroPosition implements Serializable {

    //dados para resultado de cada super herói
    private int position;
    private String code;
    private String name;
    private String lapsNumber;
    private double averageSpeed;
    private LocalTime totalTime;
    private SuperHeroLap bestLap;

}
