package br.com.testeVivo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuperHeroLap {

    //dados de cada volta recebida no documento
    private String hour;
    private String code;
    private String name;
    private String lap;
    private LocalTime lapTime;
    private double averageSpeed;

    //auxiliar para facilitar a soma do SuperHeroPosition.totalTime
    @JsonIgnore
    private Duration lapTimeDuration;

}
