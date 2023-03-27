# Movies Battle API

Esse projeto fornece uma API REST que provê uma funcionalidade simples de um quiz. Cada partida exibe 2 filmes e 
o jogador deve acertar qual filme tem maior pontuação. A pontuação é calculada pela multiplicação da nota do filme com 
o número de votos que ele teve.

## Requisitos

### Jogadores devem informar as credenciais para acessar todos os endpoints (exceto a listagem do ranking)

A aplicação possui 2 usuários por padrão: player1 e player2, e a senha é 123456 para os dois.<br>
Para armazenamento dos dados foi usando o banco de dados H2 com persistência em memória.<br>
A autenticação/autorização da API foi feita com Spring Security e utiliza Basic Auth como método de autenticação. <br>

### Geração das Rodadas

Os dados de filmes usados pela aplicação são carregados à partir da API externa do IMDB (http://www.omdbapi.com/).
A aplicação carrega uma quantidade limitada de filmes ao iniciar, esse valor pode ser alterado no arquivo de propriedades.
Os IDs dos filmes seguem um padrão do tipo tt0000000, assim, são feitas "n" chamadas à API em uma sequência que varia de 
acordo com o número de filmes que se desja carregar. Uma outra opção seria carregar os filmes à partir de uma lista estática
fornecida pelo IMDB (https://www.imdb.com/interfaces/), porém, isso faria com que o custo de inicialização ficasse muito alto,
por isso optou-se por carregar uma quantidade limitada.

A API possui as seguintes regras de geração das rodadas:

1. Não é permitido ter filmes repetidos em uma rodada
2. Não é permitido ter rodadas repetidas em uma mesma partida (ex A-B, B-A)
3. Não deve ser possível gerar uma nova rodada caso a rodada atual não tenha sido respondida.

### Regras do Jogo

1. Cada partida permite no máximo 3 erros, e uma vez antingido esse limite, a partida deve ser encerrada.
2. Cada jogador só pode ter 1 jogo ativo, assim, para começar um novo jogo, o atual precisa ser finalizado.
3. O jogador só poderá encerrar um jogo ativo.
4. A pontuação de cada jogador é obtida através da multiplicação da quantidade de rodadas respondidas pela porcentagem de acerto.

### Ranking

É possível exibir o Ranking com os 5 (valor escolhido arbitrariamente) melhores jogadores, ordenados de forma decrescente.

## API Documentation:

A documentação da API pode ser vista no endpoint abaixo, após a inicialização da aplicação:

http://localhost:8080/swagger-ui/

Para autenticação na API são disponibilizados, por padrão, os seguintes usuários:

- user/password: player1 / 123456
- user/password: player2 / 123456

### Endpoints

#### Iniciar uma partida

_POST /v1/match/new_

#### NEXT QUIZ

_POST /v1/match/round_

#### STOP GAME

_POST /v1/match/stop_

#### RANKING

_GET /v1/ranking_

## Principais tecnologias utilizadas

* Maven
* Java 11
* SpringBoot 2.7
* JUnit 5 - Unit and Integration tests
* Mockito - Test Mock
* H2 database

## Frameworks utilizados

* Spring Web
* Spring Data
* Spring Security
* OpenAPI 3.0

## Commands:

Executar a aplicação:

    mvn spring-boot:run

Executar somente os testes:

    mvn test

Compilar e testar:

    mvn clean install

## Relatório de Cobertura do Testes
https://github.com/jashkenas/backbone/tree/master/examples/todos
O relatório de cobertura dos testes pode ser visualizado clicando no link abaixo:

[Visualizar Relatório de Cobertura](https://htmlpreview.github.io/?https://github.com/robsongomes/ada-movies-battle/blob/main/htmlReport/index.html)