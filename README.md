# Weather Data Application

## Descrição
Esta aplicação, desenvolvida em Java/Quarkus, tem como objetivo obter dados climáticos de duas cidades diferentes e guardalos em um banco de dados PostgreSQL. Utiliza o serviço OpenWeatherMap para a obtenção desses dados climáticos e integra-se com um broker MQTT (Mosquitto) para o envio e recebimento de informações.

## Requisitos
- Java 11 ou superior
- Quarkus Framework
- Broker MQTT (Mosquitto)
- Banco de dados PostgreSQL

## Configurações
Certifique-se de configurar corretamente as seguintes propriedades:

- **OpenWeatherMap API Key:** `weather.api.key`
- **Coordenadas das Cidades:**
  - `city1.latitude`, `city1.longitude` para a primeira cidade
  - `city2.latitude`, `city2.longitude` para a segunda cidade
- **Configurações do Mosquitto:**
  - `mqtt.topic` para o tópico MQTT
  - `mqtt.server.host`, `mqtt.server.port` para o host e porta do servidor MQTT
- **Configurações do PostgreSQL:**
  - Detalhes de conexão com o banco de dados PostgreSQL (`quarkus.datasource`)

## Estrutura do Projeto
A estrutura do projeto está organizada da seguinte maneira:

- `com.weather.models`: Contém a entidade `WeatherDataEntity` para mapeamento da tabela no banco de dados.
- `com.weather.mosquitto`: Possui classes para enviar e receber dados do broker MQTT.
- `com.weather`: Contém os agendamentos e recursos para obter dados climáticos e processá-los.

## Executando a Aplicação
1. Certifique-se de ter todas as dependências e configurações corretamente.
2. Execute a aplicação com o comando `mvn quarkus:dev`.
3. A aplicação estará acessível em `http://localhost:8080`.

## Funcionalidades
- **Scheduler:** Agendamento para buscar dados climáticos a cada 20 segundos.
- **REST Endpoint:** Endpoint REST para obter dados climáticos das cidades.
- **Integração MQTT:** Envio e recebimento de dados do broker MQTT.
- **Persistência de Dados:** Persistência dos dados climáticos no banco de dados PostgreSQL.

## Sobre a OpenWeatherMap
A OpenWeatherMap é um serviço online que fornece dados climáticos, previsões meteorológicas e informações relacionadas ao clima em tempo real. Neste projeto, é usada para obter os dados climáticos das cidades especificadas.

É uma plataforma de previsão do tempo e serviços meteorológicos que oferece dados climáticos em tempo real e previsões precisas para diversas localizações ao redor do mundo. Ela fornece uma API acessível para desenvolvedores integrarem informações meteorológicas em suas aplicações.

A integração com a API envolve o uso de endpoints RESTful para obter dados climáticos atuais, previsões futuras, informações sobre vento, umidade, temperatura e muito mais, tudo baseado nas coordenadas geográficas ou nome da cidade especificada.

As chaves da API (como `weather.api.key`) são fornecidas pela OpenWeatherMap para autenticar e permitir o acesso aos dados.

## Autor
- Marcos D.
