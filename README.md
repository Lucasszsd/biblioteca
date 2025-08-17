Biblioteca - Sistema de Gerenciamento

Requisito para rodar o projeto : 
 - Docker e Docker Compose instalados

Variáveis de Ambiente :

Crie um arquivo .env na raiz do projeto com as seguintes variáveis:
    (Valores de referencia, podem ser alterados)
    BANCO=biblioteca
    BANCO_USER=postgres
    BANCO_SENHA=postgres
    BACK_PORT=8080
    FRONT_PORT=3000

Executar com Docker :

1. Na raiz do projeto, execute:
2. docker-compose up --build
3. Acesse a aplicação:
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080/api
   - Swagger : http://localhost:8080/swagger-ui/index.html


