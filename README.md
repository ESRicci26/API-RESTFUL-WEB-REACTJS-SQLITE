# API RESTFUL WEB Spring Boot/REACTJS/SQLITE: Sistema de Gerenciamento de Usuários

## Visão Geral
Este projeto é uma aplicação Web FullStack para gerenciamento de usuários, desenvolvida com React no frontend e Spring Boot no backend.
O sistema oferece funcionalidades CRUD (Create, Read, Update, Delete) para manipulação de registros de usuários.

## Estrutura do Projeto

### Backend (Spring Boot)

#### Tecnologias Utilizadas
- **Spring Boot**: Framework Java para construção de aplicações
- **Spring Data JPA**: Para persistência de dados
- **Spring Web**: Para desenvolvimento de API RESTful
- **SQLITE**: Banco de dados relacional
- **Hibernate**: ORM (Object-Relational Mapping)

### Projeto REACTJS

O O projeto completo do REACTJS consta nesse aquivo "Projeto REACTJS-fornecedores-frontend.zip"

#### Arquitetura do Backend
O backend segue uma arquitetura em camadas:

1. **Entity**: Modelo de dados
2. **Repository**: Interface para acesso aos dados
3. **Service**: Lógica de negócios
4. **Controller**: Endpoints da API
5. **Exception**: Tratamento de exceções

#### Endpoints da API

| Método | URL | Descrição |
|--------|-----|-----------|
| GET | `/api/getUsers` | Obtém todos os usuários |
| GET | `/api/user/{id}` | Obtém um usuário pelo ID |
| POST | `/api/addUser` | Adiciona um novo usuário |
| PUT | `/api/updateUser/{id}` | Atualiza um usuário existente |
| DELETE | `/api/deleteUser/{id}` | Remove um usuário |

### Frontend (React)

#### Tecnologias Utilizadas
- **React**: Biblioteca JavaScript para construção de interfaces
- **Axios**: Cliente HTTP para consumo da API
- **React Router DOM**: Gerenciamento de rotas
- **Bootstrap**: Framework CSS para estilização

#### Componentes Principais

1. **Layout.js**: Componente de layout com NavBar
2. **Home.js**: Página inicial com tabela de usuários
3. **AddUser.js**: Formulário para adicionar novos usuários
4. **EditUser.js**: Formulário para editar usuários existentes
5. **UserService.js**: Serviço para comunicação com a API

#### Rotas

| Rota | Componente | Descrição |
|------|------------|-----------|
| `/` | Home | Lista de usuários |
| `/adduser` | AddUser | Formulário para adicionar usuário |
| `/edituser/:id` | EditUser | Formulário para editar usuário |

## Modelo de Dados

### Entidade User

| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | Long | Identificador único (chave primária) |
| name | String | Nome do usuário |
| username | String | Nome de usuário para login |
| email | String | Endereço de e-mail |

## Fluxo de Funcionamento

### Cadastro de Usuário
1. Usuário acessa rota `/adduser`
2. Preenche formulário com dados (nome, username, email)
3. Submete o formulário
4. Frontend envia requisição POST para `/api/addUser`
5. Backend valida e persiste os dados
6. Usuário é redirecionado para a página inicial

### Edição de Usuário
1. Usuário clica no botão "Editar" na lista de usuários
2. Sistema redireciona para `/edituser/:id`
3. Formulário é carregado com dados do usuário
4. Usuário altera os dados e submete o formulário
5. Frontend envia requisição PUT para `/api/updateUser/{id}`
6. Backend atualiza os dados no banco
7. Usuário é redirecionado para a página inicial

### Exclusão de Usuário
1. Usuário clica no botão "Excluir" na lista de usuários
2. Sistema exibe confirmação
3. Frontend envia requisição DELETE para `/api/deleteUser/{id}`
4. Backend remove o registro do banco de dados
5. Lista de usuários é atualizada

## Configuração do Ambiente

### Backend

#### Requisitos
- Java 8 ou superior
- Maven
- SQLITE

#### Arquivo de Configuração (application.properties)
```properties
#BANCO DE DADOS SQLITE
spring.application.name=API-RESTFUL-WEB-REACTJS
spring.datasource.url=jdbc:sqlite:fornecedores.DB
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=com.javaricci.ApiRestFulWeb.Config.SQLiteDialect
spring.jpa.hibernate.ddl-auto=none

# Adicionei configurações para melhorar as mensagens de erro da API
spring.jackson.serialization.fail-on-empty-beans=false
spring.mvc.throw-exception-if-no-handler-found=true
spring.web.resources.add-mappings=false
```

### Frontend

#### Requisitos
- Node.js (versão 14+)
- npm ou yarn

#### Configuração
Arquivo para configuração da URL base da API:
```javascript
// src/services/api.js
import axios from 'axios';

const API_BASE_URL = "http://localhost:8080/api";

export default API_BASE_URL;
```

## Instruções de Execução

### Backend
1. Clone o repositório
2. Navegue até a pasta do projeto backend
3. Execute `mvn clean install` para instalar dependências
4. Execute `mvn spring-boot:run` para iniciar o servidor

### Frontend
1. Navegue até a pasta do projeto frontend
2. Execute `npm install` para instalar dependências
3. Execute `npm start` para iniciar o servidor de desenvolvimento
4. Acesse `http://localhost:3000` no navegador

## Detalhes de Implementação

### Backend

#### UserController.java
```java
@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/addUser")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    @GetMapping("/getUsers")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PutMapping("/updateUser/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @DeleteMapping("/deleteUser/{id}")
    String deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        return "Usuário com id " + id + " foi excluído com sucesso.";
    }
}
```

#### User.java
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String name;
    private String email;
    
    // Getters and Setters
}
```

### Frontend

#### UserService.js
```javascript
import axios from "axios";
import API_BASE_URL from "./api";

class UserService {
    saveUser(user) {
        return axios.post(API_BASE_URL + "/addUser", user);
    }
    
    getUsers() {
        return axios.get(API_BASE_URL + "/getUsers");
    }
    
    getUserById(id) {
        return axios.get(API_BASE_URL + "/user/" + id);
    }
    
    updateUser(user, id) {
        return axios.put(API_BASE_URL + "/updateUser/" + id, user);
    }
    
    deleteUser(id) {
        return axios.delete(API_BASE_URL + "/deleteUser/" + id);
    }
}

export default new UserService();
```

## Considerações de Segurança e Desempenho

### Segurança
- Implementação básica sem autenticação/autorização
- Em ambiente de produção, recomenda-se adicionar:
  - Spring Security para proteger endpoints
  - JWT para autenticação
  - Validação de dados de entrada
  - HTTPS para comunicação segura

### Desempenho
- Para grandes volumes de dados, considerar:
  - Paginação na listagem de usuários
  - Cache para dados frequentemente acessados
  - Índices no banco de dados para consultas frequentes

## Extensões Futuras
- Implementação de autenticação e autorização
- Perfis de usuário (admin, regular)
- Logs de atividades
- Filtros e ordenação na listagem de usuários
- Testes automatizados
- Dockerização da aplicação
