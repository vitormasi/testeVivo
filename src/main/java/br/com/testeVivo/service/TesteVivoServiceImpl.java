package br.com.testeVivo.service;

import br.com.testeVivo.domain.RegisterMatches;
import br.com.testeVivo.domain.SuperHeroLap;
import br.com.testeVivo.domain.SuperHeroPosition;
import br.com.testeVivo.domain.SuperHeroResults;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Component
public class TesteVivoServiceImpl implements TesteVivoService {

    @Override
    public List<RegisterMatches> getMatches(int[] registers) throws Exception {
        if (Arrays.stream(registers).anyMatch(register -> Objects.nonNull(register) && register > 15)){
            throw new Exception("Os itens dentro do vetor não podem ser maior do que 15.");
        }

        int[][] matriz = createMatriz();

        List<RegisterMatches> registerMatchesList = new ArrayList<>();

        //percorre o vetor de registros e identifica o número de vezes que aparece aquele registro na matriz
        Arrays.stream(registers).forEach(register -> {
            int matches = Arrays.stream(matriz).mapToInt(vetor -> (int) Arrays.stream(vetor)
                    .filter(number -> Objects.equals(number, register))
                    .count()).sum();

            //cria RegisterMatches com o valor do registro do vetor e o número de ocorrências para cada registro
            registerMatchesList.add(new RegisterMatches(register, matches));
        });

        return registerMatchesList;
    }

    @Override
    public SuperHeroResults getSuperHeroData(MultipartFile file) throws Exception {

        //lê o arquivo xlsx e armazena cada registro do arquivo em uma lista auxiliar
        List<SuperHeroLap> superHeroDataList = new ArrayList<>();
        final Workbook workbook = WorkbookFactory.create(file.getInputStream());
        final Sheet sheet = workbook.getSheetAt(0);
        final Iterator<Row> rowIterator = sheet.rowIterator();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if(row.getRowNum() == 0)
                continue;

            if (isRowEmpty(row))
                continue;

            Iterator<Cell> cellIterator = row.cellIterator();

            SuperHeroLap superHeroLap = new SuperHeroLap();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                fillSuperHeros(superHeroLap, cell);
            }

            superHeroDataList.add(superHeroLap);

        }

        //retorna o método que processa os resultados
        return createResults(superHeroDataList);
    }

    //cria a matriz base
    private int[][] createMatriz() {
        int[][] matriz = new int[15][15];

        IntStream.range(0, 15).forEach(i -> {
            IntStream.range(0, 15).forEach(j -> {
                matriz[i][j] = i;
            });
        });

        System.out.println(matriz);
        return matriz;
    }

    //verifica se a linha que está sendo lida está vazia
    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }

    //preenche o registro da lista auxiliar com cada célula do excel
    private void fillSuperHeros(SuperHeroLap superHeroLap, Cell cell) throws Exception {
        switch (cell.getColumnIndex()) {

            case 0: //Hora
                final String hour = CellType.NUMERIC.equals(cell.getCellType()) ? NumberToTextConverter.toText(cell.getNumericCellValue()).trim() : cell.getStringCellValue().trim();

                if(StringUtils.isBlank(hour))
                    break;

                superHeroLap.setHour(hour);

                break;

            case 1: //Super-Heroi

                if(StringUtils.isBlank(cell.getStringCellValue()))
                    throw new Exception("O campo Super-Heroi é obrigatório");

                String[] superHeroParts = cell.getStringCellValue().split("-");
                if (superHeroParts.length < 2)
                    throw new Exception("O campo Super-Heroi deve estar preenchido no formato {código}-{nome}");

                superHeroLap.setCode(superHeroParts[0]);
                superHeroLap.setName(superHeroParts[1]);

                break;

            case 2: //Nº Volta
                final String lap = CellType.NUMERIC.equals(cell.getCellType()) ? NumberToTextConverter.toText(cell.getNumericCellValue()).trim() : cell.getStringCellValue().trim();

                if(StringUtils.isBlank(lap))
                    break;

                superHeroLap.setLap(lap);

                break;

            case 3: //Tempo Volta
                String lapTime = CellType.NUMERIC.equals(cell.getCellType()) ? NumberToTextConverter.toText(cell.getNumericCellValue()).trim() : cell.getStringCellValue().trim();

                if(StringUtils.isBlank(lapTime))
                    throw new Exception("O campo Tempo Volta é obrigatório");

                lapTime = "00:0" + lapTime;

                superHeroLap.setLapTime(LocalTime.parse(lapTime));
                superHeroLap.setLapTimeDuration(Duration.between(LocalTime.MIN, LocalTime.parse(lapTime)));

                break;

            case 4: //Velocidade média da volta
                final String averageSpeed = CellType.NUMERIC.equals(cell.getCellType()) ? NumberToTextConverter.toText(cell.getNumericCellValue()).trim() : cell.getStringCellValue().trim();

                if(StringUtils.isBlank(averageSpeed))
                    throw new Exception("O campo Velocidade Média da Volta é obrigatório");

                superHeroLap.setAverageSpeed(Double.parseDouble(averageSpeed));

                break;

            default:
                break;
        }
    }

    private SuperHeroResults createResults(List<SuperHeroLap> superHeroLapList) {

        SuperHeroResults superHeroResults = new SuperHeroResults();
        List<String> codes = superHeroLapList.stream().map(SuperHeroLap::getCode).distinct().collect(Collectors.toList());

        //preenche as informações de cara super herói na classe de resultado
        codes.stream().forEach(code -> {

            AtomicInteger qtLaps = new AtomicInteger();
            AtomicReference<Double> totalAverageSpeed = new AtomicReference<>((double) 0);
            SuperHeroPosition superHeroPosition = new SuperHeroPosition();
            superHeroPosition.setCode(code);
            superHeroLapList.stream()
                    .filter(data -> Objects.equals(code, data.getCode()))
                    .forEach(data -> {

                        qtLaps.getAndIncrement();
                        if (StringUtils.isBlank(superHeroPosition.getName()))
                            superHeroPosition.setName(data.getName());

                        //armazena a melhor volta deste super herói
                        if (superHeroPosition.getBestLap() == null || superHeroPosition.getBestLap().getLapTime().isAfter(data.getLapTime()))
                            superHeroPosition.setBestLap(data);

                        if (superHeroPosition.getTotalTime() == null)
                            superHeroPosition.setTotalTime(data.getLapTime());
                        else
                            superHeroPosition.setTotalTime(superHeroPosition.getTotalTime().plus(data.getLapTimeDuration()));

                        //calcula a velocidade média deste super herói
                        totalAverageSpeed.set(totalAverageSpeed.get() + data.getAverageSpeed());
                        superHeroPosition.setAverageSpeed(totalAverageSpeed.get() / qtLaps.get());
                        superHeroPosition.setLapsNumber(qtLaps.toString());

                    });

            superHeroResults.getSuperHeroPositionList().add(superHeroPosition);

        });

        //Preenche as informações de melhor volta entre todos corredores
        superHeroResults.getSuperHeroPositionList().stream().forEach(superHeroPosition -> {

            if (superHeroResults.getBestLap() == null || superHeroResults.getBestLap().getLapTime().isAfter(superHeroPosition.getBestLap().getLapTime()))
                superHeroResults.setBestLap(superHeroPosition.getBestLap());

        });

        //Ordena a lista de personagens entre finalizado em menor tempo total e número de voltas (caso não tenha completado o mesmo nº de voltas)
        superHeroResults.getSuperHeroPositionList().sort(Comparator.comparing(SuperHeroPosition::getLapsNumber).reversed().thenComparing(SuperHeroPosition::getTotalTime));
        AtomicInteger position = new AtomicInteger(1);
        superHeroResults.getSuperHeroPositionList().stream().forEach(p -> p.setPosition(position.getAndIncrement()));

        return superHeroResults;
    }
}
