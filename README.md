Esse é o vote-service serviço responsavél por armazenar e gerenecias votos e disponibilizar o resultado de uma sessão de votação.

Pré-requisitos
- Java 17
- Docker e docker compose 
- Executar primeiro o docker-compose.yml do projeto assembly-service para o este projeto já se conectar ao RabbitMq. 
  
Certifique-se de ter as seguintes portas livres na sua máquina para subir a aplicação:<br>
- 8081 -> porta ocupada pela própria aplicação
- 5433 -> banco de dados PostgreSQL


<h2>Descrição do projeto</h2>
Esse é o vote-service serviço responsavél por armazenar e gerenecias votos e disponibilizar o resultado de uma sessão de votação.
Possui as seguintes funcionalidades(endpoints): <br>
<strong>VOTOS</strong>

- Registra um voto:<br>
Request : http://localhost:8081/vote POST<br>
````
{
    "idSession":2,
    "vote":true,
    "idAssociate":5

}
````
Response:
````
{
    "id": 4,
    "idSession":2,
    "vote":true,
    "idAssociate":3
}
````
- Conta votos e exibe resultado de uma sessão de votação:<br>
Request : http://localhost:8081/vote/result/1 GET<br>
Response:
````
{
    "idSession":2,
    "voteForYes":1,
    "voteForYes":0,
    "result":"APROVADA",
}
````

<strong>STATUS DE SESSÃO</strong>

- Busca os status de todas as sessões salvas:<br>
Request : http://localhost:8081/session-status GET<br>
Response:
````
{
    "id":2,
    "idSessions":2,
    "sessionStatus":"OPEN
}
````
