# Rock Catalog 🖤

Projeto pessoal pra treinar Java com Spring Boot, misturando com uma coisa
que eu curto de verdade: rock e emo. É um catálogo de bandas e álbuns onde
qualquer pessoa que entrar no site pode ver e deixar uma avaliação pública
(nota + comentário) — sem precisar criar conta.

## Sobre o projeto

Comecei simples, só com um CRUD em memória, e fui evoluindo até virar uma
aplicação web de verdade: back-end em Java (Spring Boot + Thymeleaf),
banco de dados persistente (H2) e um front-end com a cara que eu queria —
paleta escura, tipografia gótica, cards com visual de flyer de show
xerocado.

## Tecnologias usadas

- **Java 17**
- **Spring Boot** (Web, Data JPA, Thymeleaf, Validation)
- **H2 Database** (rodando em modo arquivo, pra não perder os dados)
- **Maven**
- HTML/CSS puro no front (sem framework JS)

## Funcionalidades

- Cadastrar bandas e álbuns
- Listar o catálogo completo
- Ver a nota média de cada álbum, calculada a partir das avaliações
- Qualquer visitante pode deixar uma avaliação pública (nome, nota de 0 a
  10, comentário)
- Remover bandas/álbuns

## Como rodar o projeto

### Pré-requisitos

- Java 17 ou superior instalado (`java -version`)
- Maven instalado (`mvn -version`)

### Passo a passo

```bash
git clone <url-do-repositorio>
cd rockcatalog
mvn spring-boot:run
```

Depois é só abrir **http://localhost:8080** no navegador.

Na primeira execução o catálogo já vem com 3 bandas de exemplo (My
Chemical Romance, Paramore, Taking Back Sunday), só pra não começar vazio.

### Onde os dados ficam salvos

Uso H2 em modo arquivo, então os dados continuam lá mesmo depois de
reiniciar o servidor (`./data/rockcatalog.mv.db`). Pra inspecionar o banco
direto, dá pra acessar **http://localhost:8080/h2-console** com:

- JDBC URL: `jdbc:h2:file:./data/rockcatalog`
- Usuário: `sa`
- Senha: *(em branco)*

## Estrutura do projeto

```
src/main/java/com/rockcatalog/
 ├── model/          -> Banda, Album, Avaliacao (entidades JPA)
 ├── repository/     -> interfaces Spring Data JPA
 ├── controller/      -> rotas web (listar, cadastrar, avaliar, excluir)
 └── config/          -> DataLoader (dados de exemplo)

src/main/resources/
 ├── templates/       -> páginas HTML (Thymeleaf)
 ├── static/css/      -> estilo gótico/flyer
 └── application.properties
```

## O que eu aprendi / próximos passos

Esse projeto foi minha forma de treinar relacionamentos JPA
(`@OneToMany`/`@ManyToOne`), validação de formulário no Spring, e também
de exercitar identidade visual num front-end simples.

Ideias pra continuar evoluindo:

- Autenticação simples (Spring Security), pra ter controle de quem avalia
- Upload de imagem de capa em vez de só URL
- Paginação quando o catálogo crescer
- Deploy num serviço gratuito (Render/Railway) pra compartilhar o link
