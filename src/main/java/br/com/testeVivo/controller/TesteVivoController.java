package br.com.testeVivo.controller;

import br.com.testeVivo.domain.RegisterMatches;
import br.com.testeVivo.domain.SuperHeroResults;
import br.com.testeVivo.service.TesteVivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/teste-vivos")
public class TesteVivoController {

    @Autowired
    private TesteVivoService testeVivoService;
    private final List<String> allowedTypes = Arrays.asList("application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    @GetMapping("/matriz")
    public ResponseEntity get(@RequestParam("registers") int[] registers) throws Exception {
        try {

            List<RegisterMatches> result = testeVivoService.getMatches(registers);

            return new ResponseEntity(result, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
        }
    }

    @PostMapping("/super-hero")
    public ResponseEntity getSuperHero(@RequestBody MultipartFile file) throws IOException {
        if (!allowedTypes.contains(file.getContentType()))
            return new ResponseEntity("Formato de arquivo inválido. Deve ser .xls ou .xlsx", HttpStatus.BAD_REQUEST);

        try {

            SuperHeroResults result = testeVivoService.getSuperHeroData(file);

            return new ResponseEntity(result, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
        }
}

}