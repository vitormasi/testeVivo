package br.com.testeVivo.service;

import br.com.testeVivo.domain.RegisterMatches;
import br.com.testeVivo.domain.SuperHeroResults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TesteVivoService {

    public List<RegisterMatches> getMatches(int[] registers) throws Exception;

    public SuperHeroResults getSuperHeroData(MultipartFile file) throws Exception;

}
