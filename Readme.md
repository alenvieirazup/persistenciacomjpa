# Persistência com JPA

Projeto para assimilar o conteúdo do curso [Persistência com JPA: introdução ao Hibernate](https://cursos.alura.com.br/course/persistencia-jpa-introducao-hibernate) . O princípio foi utilizar testes automatizados para guiar a utilização dos principais conceitos do curso.

## Tecnologias utilizadas:

- Java 17
- Maven
- Junit 5
- Hibernate
- H2 Database

## Rodando os testes com o maven:

```bash
$ mvn test
```

## Rodando os testes através de um container:
```bash
# Contruindo uma imagem
$ docker build -t alenvieirazup/persistenciacomjpa .
# Executando os testes no container
$ docker run alenvieirazup/persistenciacomjpa
```