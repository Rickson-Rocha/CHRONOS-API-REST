# Sistema de Ponto - Desafio Full Stack Idus (Backend)

![Status](https://img.shields.io/badge/status-concluído-green)
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x.x-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)

Este repositório contém o código-fonte do backend para a avaliação de Desenvolvedor Full Stack da Idus[cite: 1]. [cite_start]O projeto consiste em um sistema para gestão de pontos de jornada de trabalho, projetado para ser uma solução robusta e escalável para o controle de horas trabalhadas por colaboradores[cite: 6, 10].

## Sumário

* [Tecnologias Utilizadas](#-tecnologias-utilizadas)
* [Estrutura do Banco de Dados (DER)](#-estrutura-do-banco-de-dados-der)
* [Funcionalidades Implementadas](#-funcionalidades-implementadas)
* [Como Executar o Projeto](#-como-executar-o-projeto)
* [Testes](#-testes)
* [Endpoints da API (Swagger)](#-endpoints-da-api-swagger)
* [Decisões de Projeto](#-decisões-de-projeto)
* [Funcionalidades Idealizadas](#-funcionalidades-idealizadas-melhorias-futuras)

##  Tecnologias-utilizadas

 lista a seguir representa as principais tecnologias e frameworks utilizados no desenvolvimento do backend, seguindo as sugestões da documentação do desafio[cite: 2, 3]:

* **Linguagem**: Java 17
* **Framework Principal**: Spring Boot 3.5.4
* **Segurança**: Spring Security com autenticação baseada em JWT (JSON Web Token)
* **Persistência de Dados**: Spring Data JPA com Hibernate
* **Banco de Dados**: PostgreSQL 16 
* **Migração**: Flyway
* **Gerenciador de Dependências**: Maven
* **Testes**: JUnit 5 e Mockito para testes unitários [cite: 33]
* **Documentação da API**: Springdoc OpenAPI (Swagger)
* **Front end (SPA)**: REACT + VITE 

## Estrutura do banco de dados

Para atender ao requisito de documentação da arquitetura de dados, o Diagrama de Entidade e Relacionamento (DER) foi criado para representar as entidades principais e seus relacionamentos.



[![](https://img.plantuml.biz/plantuml/svg/hPFFRjD04CRl-nIZz40g50aXzP9JGmabeAAGfd2qnjv9CiIxQ-sE5OM4f-5GYJjFa1TZd5R2mLLFNAprzurdV_F7bqamIkrAOrx0LV0BNjOH3_U7tvJ04c25yV3B5unW0rZ6PKI79gtPguCEIlOasueWH5c5bf5MQCE6_H8MM2PgEIlqTXDPX7p7AqfCQIGIEQ-59aNmj_oTu9LPiTAGgsJhoEKKKmFybXRiMMb_AXic67jXYrRH-tFCINET6qCgoXPwacUR4DVPjr17JziUw0TVLVZmy0ux0_0Is0xXpUHwycaE_Ud7SzK60trepY9o9Hpy4BwCPbVlHxF-wukB9a1RA7Ngo1Qvt1wlB0FQpB7NYZHCKu-lnpEDK8bTxR8y4gwx0JzEsEkaHH-XxvhJyxWU7NM0o2dHgGhzky-DLLIBfurAnw4bs4wo64hgrdm6xHPdRRftV-aAhLV8PYX3c4zknhVpqSrKzRgoJ-YdRQW2UpcsOTgSdc-3i2ETZgjExpc3ffd_ZUwVFGm9PhfwXQwAJi5Bq5Ljxzb-FnZiTuzJ74BFDXzJwfa7fR73xh4wjICjEKapUsCkoLlzL_y0)](https://editor.plantuml.com/uml/hPFFRjD04CRl-nIZz40g50aXzP9JGmabeAAGfd2qnjv9CiIxQ-sE5OM4f-5GYJjFa1TZd5R2mLLFNAprzurdV_F7bqamIkrAOrx0LV0BNjOH3_U7tvJ04c25yV3B5unW0rZ6PKI79gtPguCEIlOasueWH5c5bf5MQCE6_H8MM2PgEIlqTXDPX7p7AqfCQIGIEQ-59aNmj_oTu9LPiTAGgsJhoEKKKmFybXRiMMb_AXic67jXYrRH-tFCINET6qCgoXPwacUR4DVPjr17JziUw0TVLVZmy0ux0_0Is0xXpUHwycaE_Ud7SzK60trepY9o9Hpy4BwCPbVlHxF-wukB9a1RA7Ngo1Qvt1wlB0FQpB7NYZHCKu-lnpEDK8bTxR8y4gwx0JzEsEkaHH-XxvhJyxWU7NM0o2dHgGhzky-DLLIBfurAnw4bs4wo64hgrdm6xHPdRRftV-aAhLV8PYX3c4zknhVpqSrKzRgoJ-YdRQW2UpcsOTgSdc-3i2ETZgjExpc3ffd_ZUwVFGm9PhfwXQwAJi5Bq5Ljxzb-FnZiTuzJ74BFDXzJwfa7fR73xh4wjICjEKapUsCkoLlzL_y0)


### Descrição das Entidades

* **tb_work_journey**: Armazena as regras e parâmetros para cada tipo de jornada de trabalho.
    * `id`: Chave primária.
    * `description`: Descrição do regime (ex: "Jornada de 8 horas com 1 hora de pausa").
    * `daily_workload_minutes`: Carga horária diária total em minutos (ex: 480).
    * `minimum_break_minutes`: Tempo mínimo de pausa obrigatório em minutos (ex: 60).
    * `status`: Status da jornada (ex: IN_PROGRESS, COMPLETED).

* **tb_users**: Armazena os dados dos colaboradores e administradores do sistema.
    * `id`: Chave primária.
    * `name`: Nome completo do usuário.
    * `email`: Email utilizado para login (valor único).
    * `cpf`: CPF do usuário (valor único).
    * `password`: Senha criptografada do usuário.
    * `roles`: Papel do usuário no sistema (ex: 'ADMIN', 'COMUM').
    * `work_journey_id`: Chave estrangeira que referencia o ID da jornada de trabalho do usuário na tabela `tb_work_journey`.

* **tb_point**: Armazena cada registro de entrada ou saída de um usuário.
    * `id`: Chave primária.
    * `timestamp`: Data e hora exata em que o ponto foi registrado.
    * `user_id`: Chave estrangeira que referencia o usuário que fez o registro na tabela `tb_users`.

### Relacionamentos

* **Um** `tb_work_journey` pode ser aplicado a **muitos** `tb_users`.
* **Um** `tb_user` pode registrar **muitos** `tb_point`.

## Funcionalidades implementadas

O sistema atende a todos os requisitos funcionais solicitados na especificação do desafio:

* **Segurança e Autenticação**
    * ` - O sistema é protegido por login e senha utilizando Spring Security.
    * `[x]` - Os tokens JWT são utilizados para autorizar o acesso aos endpoints após a autenticação.
* **Tipos de Usuário e Permissões**
    * ` - Suporte a dois tipos de usuários: **Administrador** e **Comum**.
    * ` - **Administrador (MANAGER)**: Tem permissão para cadastrar novos usuários (comuns), definindo seu nome, email, senha e regime de jornada[cite: 22].
    * ` - **Usuário Comum (EMPLOYEE)**: Não pode cadastrar outros usuários e tem acesso apenas às suas próprias informações e funcionalidades de ponto[cite: 24].
* **Gestão de Pontos e Jornada**
    * `[x]` - Usuários comuns podem registrar múltiplos pontos ao longo do diaO sistema tem flexibilidade para aceitar registros em qualquer horário, acomodando atrasos ou horas extras[cite: 15].
    * `[x]` - Suporte para dois regimes de jornada: 6 horas contínuas e 8 horas com no mínimo 1 hora de pausa.
    * `[x]` - **Resumo da Jornada Diária**: O usuário pode consultar os pontos registrados no dia e verificar se a jornada está completa[cite: 24].
    * `[x]` - **Cálculo de Horas Restantes**: O sistema informa quantas horas faltam para completar a jornada do dia com base nos registros existentes[cite: 25].
    * `[x]` - **Cálculo de Horas Excedentes**: Caso a jornada tenha sido ultrapassada, o sistema calcula e exibe o total de horas extras[cite: 26].
* **Persistência**
    * `[x]` - Todos os dados de usuários e pontos são persistidos em um banco de dados PostgreSQL.

## Como executar o projeto como executar o projeto

Para executar o projeto localmente, siga os passos abaixo.

### Pré-requisitos

* Java 17 (ou superior)
* Maven 3.8 (ou superior)
* Git
* PostgreSQL 16 (rodando localmente ou em um container Docker)

### Passos

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/Rickson-Rocha/CHRONOS-API-REST.git
    cd CHRONOS-API-REST
    ```

2.  **Execute o dokcer-compose.yaml:**
   O arquivo docker-compose.yaml presente na raíz do projeto,já tem um serviço de banco(postgres) e uma interface web para visualizar os dados
    ```bash
    docker compose up chronos-db chronos-pgadmin

    ```
3.  **Compile e instale as dependências:**
    ```bash
    mvn clean install
    ```

4.  **Execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```
    A aplicação estará disponível em `http://localhost:8080`.

## Testes

Para garantir a qualidade do código e a corretude das regras de negócio, foram implementados testes unitários, com foco especial na lógica de cálculo de horas da jornada de trabalho, conforme solicitado.

* **Localização dos Testes**: Os testes estão na pasta `src/test/java/com/idus/chronos/service/PointCaculationServiceTest`.
* **Execução dos Testes**: Para rodar todos os testes unitários, utilize o comando:
    ```bash
    mvn test
    ```
Obs: Importante o projeto estar em execução antes de executar o comando de teste.Caso não consiga utilizar terminal para executar os testes,execute via IDE.
## endpoints-da-api-swagger-endpoints-da-api-swagger

A documentação completa da API está disponível via Swagger UI. Isso permite uma exploração interativa de todos os endpoints.

### Opção 1: Acesso Local

Após executar o projeto localmente (seguindo os passos acima), a documentação do Swagger estará acessível em:

**URL do Swagger UI Local:** `http://localhost:8080/swagger-ui.html`

### Principais Endpoints
| Método HTTP | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/v1/auth/login` | Realiza o login de um usuário, retornando um token de acesso JWT. |
| `POST` | `/api/v1/users` | (Admin) Cadastra um novo usuário (funcionário) no sistema. |
| `GET` | `/api/v1/users` | (Admin) Lista todos os usuários cadastrados. |
| `GET` | `/api/v1/users/employees/{id}` | (Admin) Busca um usuário específico pelo seu ID. |
| `POST` | `/api/v1/work-journeys` | (Admin) Cria um novo tipo de jornada de trabalho. |
| `GET` | `/api/v1/work-journeys` | (Admin) Lista todas as jornadas de trabalho disponíveis. |
| `POST` | `/api/v1/points/register` | (Funcionário) Registra um novo ponto para o usuário autenticado. |
| `GET` | `/api/v1/points/summary/today`| (Funcionário) Retorna o resumo da jornada do dia atual. |


## Decisões projeto

Durante o desenvolvimento, algumas decisões foram tomadas para garantir a qualidade, manutenibilidade e segurança da aplicação, conforme a liberdade criativa sugerida[cite: 27, 28, 29]:

* **Arquitetura em Camadas (Layered Architecture)**: O projeto foi estruturado em camadas (Controller, Service, Repository) para promover a separação de responsabilidades, facilitar os testes e a manutenção.
* **Segurança com JWT**: A escolha pelo JWT (JSON Web Token) para a autenticação se deu por ser um padrão de mercado consolidado, stateless (não sobrecarrega o servidor com sessões) e ideal para APIs REST.
* **Uso de DTOs (Data Transfer Objects)**: Foram utilizados DTOs para desacoplar a representação dos dados entre a camada de controle (API) e a camada de persistência (entidades), evitando a exposição de detalhes do modelo de dados e customizando os dados enviados e recebidos.
* **Tratamento de Exceções Centralizado**: Foi implementado um `ControllerAdvice` para capturar exceções lançadas pela aplicação e retornar respostas de erro padronizadas e amigáveis para o cliente da API.

## funcionalidades idealizadas melhorias futuras

Além do escopo implementado, o sistema foi projetado pensando em futuras expansões. [cite_start]A seguir, algumas funcionalidades idealizadas que poderiam ser agregadas[cite: 32]:

* **Relatórios Mensais e por Período**: Geração de relatórios em PDF ou CSV com o resumo de horas trabalhadas, horas extras e faltas para um determinado período, útil para o fechamento da folha de pagamento.
* **Dashboard para Administradores**: Um painel visual para que administradores possam acompanhar em tempo real as jornadas de todos os colaboradores, identificando quem está presente, ausente ou em pausa.
* **Solicitação e Aprovação de Ajustes**: Funcionalidade para que o usuário comum possa solicitar ajustes em pontos (ex: esquecimento de registro) para aprovação do administrador.
* **Geolocalização (Opcional)**: Possibilidade de atrelar a geolocalização ao registro de ponto, garantindo que o colaborador está no local de trabalho (relevante para trabalho presencial).
* **Notificações**: Envio de notificações (via e-mail ou push) para lembrar o colaborador de registrar o ponto ou para informar sobre ajustes aprovados/reprovados.