package br.com.testeVivo.utils;

import br.com.testeVivo.domain.SuperHeroLap;
import br.com.testeVivo.domain.SuperHeroPosition;
import br.com.testeVivo.domain.SuperHeroResults;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;

public class SuperHeroUtils {

    //cria um excel padrão e retorna como MultipartFile para realizar os testes
    //são utilizadas variáveis em algumas células para poder varias e aplicar o teste de erros
    public static MultipartFile createExcel(String superHero, String lapTime, String averageSpeed) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Hora");
        row.createCell(1).setCellValue("Super-Heroi");
        row.createCell(2).setCellValue("Nº Volta");
        row.createCell(3).setCellValue("Tempo Volta");
        row.createCell(4).setCellValue("Veolicdade Media da Volta");

        row = sheet.createRow(1);
        row.createCell(0).setCellValue("23:49:08.277");
        row.createCell(1).setCellValue("038-Superman");
        row.createCell(2).setCellValue("1");
        row.createCell(3).setCellValue("1:02.852");
        row.createCell(4).setCellValue("44.275");

        row = sheet.createRow(2);
        row.createCell(0).setCellValue("23:49:10.858");
        row.createCell(1).setCellValue(superHero);
        row.createCell(2).setCellValue("1");
        row.createCell(3).setCellValue(lapTime);
        row.createCell(4).setCellValue(averageSpeed);

        File file = new File("test.xlsx");
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);

        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        return multipartFile;
    }

    public static File createExcelFile(String superHero, String lapTime, String averageSpeed) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Hora");
        row.createCell(1).setCellValue("Super-Heroi");
        row.createCell(2).setCellValue("Nº Volta");
        row.createCell(3).setCellValue("Tempo Volta");
        row.createCell(4).setCellValue("Veolicdade Media da Volta");

        row = sheet.createRow(1);
        row.createCell(0).setCellValue("23:49:08.277");
        row.createCell(1).setCellValue("038-Superman");
        row.createCell(2).setCellValue("1");
        row.createCell(3).setCellValue("1:02.852");
        row.createCell(4).setCellValue("44.275");

        row = sheet.createRow(2);
        row.createCell(0).setCellValue("23:49:10.858");
        row.createCell(1).setCellValue(superHero);
        row.createCell(2).setCellValue("1");
        row.createCell(3).setCellValue(lapTime);
        row.createCell(4).setCellValue(averageSpeed);

        File file = new File("test.xlsx");
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);

        return file;
    }

    //cria o modelo padrão de SuperHeroResults para a validação do teste OK
    public static SuperHeroResults createOkModelSuperHero() {
        SuperHeroResults resultModel = new SuperHeroResults();
        resultModel.setBestLap(new SuperHeroLap(
                "23:49:08.277",
                "038",
                "Superman",
                "1",
                LocalTime.parse("00:01:02.852"),
                44.275,
                Duration.between(LocalTime.MIN, LocalTime.parse("00:01:02.852"))
        ));
        resultModel.setSuperHeroPositionList(Arrays.asList(
                new SuperHeroPosition(
                        1,
                        "038",
                        "Superman",
                        "1",
                        44.275,
                        LocalTime.parse("00:01:02.852"),
                        resultModel.getBestLap()
                ),
                new SuperHeroPosition(
                        2,
                        "033",
                        "Flash",
                        "1",
                        43.243,
                        LocalTime.parse("00:01:04.352"),
                        new SuperHeroLap(
                                "23:49:10.858",
                                "033",
                                "Flash",
                                "1",
                                LocalTime.parse("00:01:04.352"),
                                43.243,
                                Duration.between(LocalTime.MIN, LocalTime.parse("00:01:04.352"))
                        )
                )
        ));

        return resultModel;
    }

}
