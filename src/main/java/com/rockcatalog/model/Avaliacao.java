package com.rockcatalog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * A parte "social" do projeto: qualquer visitante do site pode criar uma
 * Avaliacao pra um Album, sem precisar de cadastro/login. É de propósito
 * que não existe uma entidade de Usuário aqui — pra manter o escopo do
 * projeto simples, o "autor" é só um texto livre que a pessoa digita.
 */
@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Diga quem esta avaliando")
    private String autor;

    // Nota livre de 0 a 10 (aceita casa decimal, tipo 8.5). As anotações
    // @DecimalMin/@DecimalMax garantem que ninguém manda nota fora da faixa
    // mesmo mexendo direto no formulário via inspecionar elemento.
    @NotNull
    @DecimalMin(value = "0.0", message = "A nota minima e 0")
    @DecimalMax(value = "10.0", message = "A nota maxima e 10")
    private Double nota;

    @NotBlank(message = "Escreva um comentario")
    @Column(length = 1000)
    private String comentario;

    // Preenchida automaticamente com a data de hoje quando a avaliação é
    // criada — o visitante não escolhe essa data.
    private LocalDate data = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "album_id")
    @JsonIgnore
    private Album album;

    public Avaliacao() {
    }

    public Avaliacao(String autor, Double nota, String comentario, Album album) {
        this.autor = autor;
        this.nota = nota;
        this.comentario = comentario;
        this.album = album;
        this.data = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
