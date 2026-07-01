package com.rockcatalog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * Um álbum sempre pertence a uma Banda (relação @ManyToOne) e pode ter
 * várias Avaliacoes públicas (relação @OneToMany). É o "meio" da
 * hierarquia do catálogo: Banda -> Album -> Avaliacao.
 */
@Entity
@Table(name = "albuns")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O titulo do album nao pode ficar em branco")
    private String titulo;

    private Integer anoLancamento;

    // URL da capa do album (opcional)
    private String capaUrl;

    // Vários álbuns podem pertencer à mesma banda, por isso @ManyToOne.
    // @JsonIgnore evita loop infinito (Banda -> Album -> Banda -> ...) se um
    // dia eu resolver expor isso como JSON numa API.
    @ManyToOne
    @JoinColumn(name = "banda_id")
    @JsonIgnore
    private Banda banda;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    public Album() {
    }

    public Album(String titulo, Integer anoLancamento, String capaUrl, Banda banda) {
        this.titulo = titulo;
        this.anoLancamento = anoLancamento;
        this.capaUrl = capaUrl;
        this.banda = banda;
    }

    // @Transient diz pro JPA ignorar esse método ao mapear a tabela: a nota
    // média NÃO é uma coluna salva no banco, é calculada na hora, em
    // memória, a partir da lista de avaliações do álbum. Assim ela nunca
    // fica "desatualizada" em relação ao que foi avaliado de verdade.
    @Transient
    public double getNotaMedia() {
        if (avaliacoes == null || avaliacoes.isEmpty()) {
            return 0.0;
        }
        double soma = 0.0;
        for (Avaliacao a : avaliacoes) {
            soma += a.getNota();
        }
        // arredonda pra 1 casa decimal (ex: 8.666 vira 8.7)
        return Math.round((soma / avaliacoes.size()) * 10.0) / 10.0;
    }

    @Transient
    public int getTotalAvaliacoes() {
        return avaliacoes == null ? 0 : avaliacoes.size();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Integer anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getCapaUrl() {
        return capaUrl;
    }

    public void setCapaUrl(String capaUrl) {
        this.capaUrl = capaUrl;
    }

    public Banda getBanda() {
        return banda;
    }

    public void setBanda(Banda banda) {
        this.banda = banda;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
}
