# testeVivo  
  
Para o problema 1:  
  Foi desenvolvido utilizando como saída uma lista do objeto RegisterMatches, que contém o registro filtrado e o número de ocorrencias dele.
 
Para o problema 2:  
  Foi desenvolvida uma chamada GET que deve enviar como parâmetro um array de int registers.  
    Exemplo rodando local porta 8080 enviando array com [0, 1] : localhost:8080/matriz?registers=0,1  
      
Para o problema 3:  
  Foi desenvolvido uma chamada POST que deve enviar no body um arquivo .xlsx ou .xls contendo o log com linhas e colunas como no log.  
  Foi adicionado no repositório um arquivo modelo com o log informado no enunciado: voltasHeroi.xlsx  
  O programa irá retornar um objeto SuperHeroResults que possui o registro da melhor volta e uma lista de posições para cada herói, onde também consta a sua melhor volta e a velocidade média de toda a corrida.  
    Exemplo rodando local porta 8080: localhost:8080/super-hero  
    
Foram desenvolvidos testes unitários para cada problema.
