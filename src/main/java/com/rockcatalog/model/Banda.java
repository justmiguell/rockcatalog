package com.rockcatalog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma banda no catálogo (ex: My Chemical Romance).
 *
 * A anotação @Entity diz para o Spring/Hibernate (o "JPA", que é a
 * especificação de mapeamento objeto-relacional do Java) que esta classe
 * deve virar uma tabela no banco de dados. Cada instância de Banda vira
 * uma linha na tabela "bandas".
 */
@Entity
@Table(name = "bandas")
public class Banda {

    // @Id marca o campo como chave primária.
    // @GeneratedValue diz que o próprio banco gera o valor (auto-incremento),
    // então a gente nunca precisa escolher o id manualmente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotBlank é uma validação (do Bean Validation): impede salvar o
    // formulário se o campo vier vazio. A mensagem aparece no front-end.
    @NotBlank(message = "O nome da banda nao pode ficar em branco")
    private String nome;

    @NotBlank(message = "Informe um genero (ex: Emo, Post-Hardcore, Rock Alternativo)")
    private String genero;

    // URL de uma imagem/capa representativa da banda (opcional, por isso
    // não tem validação @NotBlank).
    private String imagemUrl;

    // Relacionamento "um para muitos": uma Banda tem vários Albuns.
    // mappedBy = "banda" indica que quem "é dono" da relação é o campo
    // "banda" lá na classe Album (é ele que guarda a chave estrangeira).
    // cascade = ALL faz com que, se eu salvar/apagar uma Banda, os Albuns
    // dela sejam salvos/apagados junto automaticamente.
    // orphanRemoval = true remove do banco um Album que for tirado dessa
    // lista, mesmo sem eu apagar a Banda inteira.
    @OneToMany(mappedBy = "banda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Album> albuns = new ArrayList<>();

    // Construtor vazio: o JPA/Hibernate exige um construtor sem argumentos
    // para conseguir criar objetos Banda a partir dos dados do banco.
    public Banda() {
    }

    // Construtor de conveniência, usado por exemplo no DataLoader para
    // criar bandas de exemplo em uma linha só.
    public Banda(String nome, String genero, String imagemUrl) {
        this.nome = nome;
        this.genero = genero;
        this.imagemUrl = imagemUrl;
    }

    // Getters e setters: o Spring/Thymeleaf usa eles por baixo dos panos
    // para ler e escrever os campos (por exemplo, o th:field dos formulários
    // HTML chama esses setters ao processar o POST).

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public List<Album> getAlbuns() {
        return albuns;
    }

    public void setAlbuns(List<Album> albuns) {
        this.albuns = albuns;
    }
}
