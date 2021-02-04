package br.com.testeVivo.controller;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.testeVivo.utils.SuperHeroUtils;
import br.com.testeVivo.TesteVivoApplication;
import br.com.testeVivo.domain.RegisterMatches;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesteVivoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TesteVivoControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testMatrizOk() {
        final ResponseEntity<List<RegisterMatches>> response = testRestTemplate.exchange("/teste-vivos/matriz?registers=0,1",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<List<RegisterMatches>>(){});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(0)).isEqualToComparingFieldByField(new RegisterMatches(0, 15));
        assertThat(response.getBody().get(1)).isEqualToComparingFieldByField(new RegisterMatches(1, 15));
    }

    @Test
    public void testMatrizError() {
        final ResponseEntity<String> response = testRestTemplate.exchange("/teste-vivos/matriz?registers=0,16",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.PRECONDITION_FAILED);
        assertThat(response.getBody()).isEqualTo("Os itens dentro do vetor não podem ser maior do que 15.");
    }

    @Test
    public void testSuperHeroOk() throws IOException {
        Resource resource = SuperHeroUtils.createExcel("033-Flash", "1:04.352", "43.243").getResource();

        final ResponseEntity<String> response = testRestTemplate.exchange("/teste-vivos/super-hero",
                HttpMethod.POST,
                this.multipartEntity(resource),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testSuperHeroErrorFileFormat() throws IOException {

        final ResponseEntity<String> response = testRestTemplate.exchange("/teste-vivos/super-hero",
                HttpMethod.POST,
                this.multipartEntity(this.createEmptyTxtFile().getResource()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Formato de arquivo inválido. Deve ser .xls ou .xlsx");

    }

    @Test
    public void testSuperHeroErrorFields() throws IOException {

        Resource resource = SuperHeroUtils.createExcel("", "1:04.352", "43.243").getResource();

        final ResponseEntity<String> response = testRestTemplate.exchange("/teste-vivos/super-hero",
                HttpMethod.POST,
                this.multipartEntity(resource),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.PRECONDITION_FAILED);
        assertThat(response.getBody()).isEqualTo("O campo Super-Heroi é obrigatório");

    }

    private MultipartFile createEmptyTxtFile() throws IOException {
        File file = new File("test.txt");
        FileOutputStream outputStream = new FileOutputStream(file);

        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
    }

    private HttpEntity<LinkedMultiValueMap<String, Object>> multipartEntity(Resource resource) {
        LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", resource);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        return new HttpEntity<>(parts, httpHeaders);
    }
}
