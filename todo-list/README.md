# To-Do List Application

## Descrição

Esta é uma aplicação de lista de tarefas (To-Do List) com um backend em Java utilizando Spring Boot e um frontend em React. A aplicação permite criar, visualizar e gerenciar tarefas.

## Estrutura do Projeto

### Backend

- **Tecnologia**: Java com Spring Boot 3.3.3
- **Banco de Dados**: H2 2.1.214
- **IDE Recomendada**: IntelliJ IDEA
- **Endpoints da API**:
  - `POST /tasks`: Cria uma nova tarefa.
  - `GET /tasks`: Obtém todas as tarefas.
  - `GET /tasks/{id}`: Obtém uma tarefa específica por ID.
  - `DELETE /tasks/{id}`: Exclui uma tarefa.

### Frontend

- **Tecnologia**: React
- **Componentes Principais**:
  - `TaskForm.js`: Formulário para criar novas tarefas.
  - `TaskTable.js`: Tabela para exibir e gerenciar tarefas.

## Backend

### Configuração e Execução

1. **Certifique-se de ter o JDK 22 e Maven 3.9.9 instalados.**
2. **Navegue até o diretório do backend**:
    ```bash
    cd caminho/para/seu/backend
    ```
3. **Compile o projeto**:
    ```bash
    mvn clean install
    ```
4. **Execute o projeto**:
    ```bash
    mvn spring-boot:run
    ```
   A aplicação será iniciada na porta `8080` por padrão.

### Estrutura do Projeto Backend

- `src/main/java/com/example/todolist/`: Código fonte do backend
- `src/main/resources/`: Recursos, como `application.properties`

### Documentação dos Endpoints

- **POST /tasks**: 
  - **Descrição**: Cria uma nova tarefa.
  - **Corpo da Requisição**: 
    ```json
    {
      "title": "Título da tarefa",
      "description": "Descrição da tarefa",
      "status": "não iniciado"
    }
    ```
  - **Resposta**: Tarefa criada.

- **GET /tasks**: 
  - **Descrição**: Obtém todas as tarefas.
  - **Resposta**: Lista de tarefas.

- **GET /tasks/{id}**:
  - **Descrição**: Obtém uma tarefa específica por ID.
  - **Resposta**: Detalhes da tarefa.

- **DELETE /tasks/{id}**:
  - **Descrição**: Exclui uma tarefa.
  - **Resposta**: Tarefa excluída.

## Frontend

### Configuração e Execução

1. **Certifique-se de ter Node.js (v20.17.0) e npm instalados.**
2. **Navegue até o diretório do frontend**:
    ```bash
    cd caminho/para/seu/frontend
    ```
3. **Instale as dependências**:
    ```bash
    npm install
    ```
4. **Execute a aplicação**:
    ```bash
    npm start
    ```
   A aplicação será iniciada na porta `3000` por padrão.

### Estrutura do Projeto Frontend

- `src/`: Código fonte do frontend
  - `TaskForm.js`: Componente para o formulário de criação de tarefas
  - `TaskTable.js`: Componente para exibir e gerenciar tarefas
  - `App.js`: Componente principal que integra o formulário e a tabela

## Documentação

- **Backend**: Todos os endpoints estão documentados na seção "Documentação dos Endpoints" acima.
- **Frontend**: A documentação do código está incluída nos próprios arquivos de código.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).
