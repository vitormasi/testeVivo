package br.com.testeVivo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import br.com.testeVivo.utils.SuperHeroUtils;
import br.com.testeVivo.TesteVivoApplication;
import br.com.testeVivo.domain.RegisterMatches;
import br.com.testeVivo.domain.SuperHeroResults;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesteVivoApplication.class)
public class TesteVivoServiceTests {

    @Autowired
    private TesteVivoServiceImpl testeVivoServiceImpl;

    @Test
    public void testGetMatchesOk() throws Exception {

        List<RegisterMatches> registerMatchesModel = new ArrayList<>(Arrays.asList(
                new RegisterMatches(0, 15),
                new RegisterMatches(1, 15)
        ));

        List<RegisterMatches> registerMatchesResult = testeVivoServiceImpl.getMatches(new int[]{0,1});

        assertThat(registerMatchesResult.get(0)).isEqualToComparingFieldByField(registerMatchesModel.get(0));
        assertThat(registerMatchesResult.get(1)).isEqualToComparingFieldByField(registerMatchesModel.get(1));

    }

    @Test
    public void testGetMatchesError() {

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> {
                    testeVivoServiceImpl.getMatches(new int[]{0,16});
                }).withMessage("Os itens dentro do vetor não podem ser maior do que 15.");

    }

    @Test
    public void testGetSuperHeroDataOk() throws Exception {

        SuperHeroResults result = testeVivoServiceImpl.getSuperHeroData(SuperHeroUtils.createExcel("033-Flash", "1:04.352", "43.243"));

        assertThat(result).isEqualToComparingFieldByFieldRecursively(SuperHeroUtils.createOkModelSuperHero());
    }

    @Test
    public void testGetSuperHeroDataError() {

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> {
                    testeVivoServiceImpl.getSuperHeroData(SuperHeroUtils.createExcel("", "1:04.352", "43.243"));
                }).withMessage("O campo Super-Heroi é obrigatório");

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> {
                    testeVivoServiceImpl.getSuperHeroData(SuperHeroUtils.createExcel("033 Flash", "1:04.352", "43.243"));
                }).withMessage("O campo Super-Heroi deve estar preenchido no formato {código}-{nome}");

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> {
                    testeVivoServiceImpl.getSuperHeroData(SuperHeroUtils.createExcel("033-Flash", "", "43.243"));
                }).withMessage("O campo Tempo Volta é obrigatório");

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> {
                    testeVivoServiceImpl.getSuperHeroData(SuperHeroUtils.createExcel("033-Flash", "1:04.352", ""));
                }).withMessage("O campo Velocidade Média da Volta é obrigatório");

    }

}
