package Modelo;

import java.io.Serializable;

public class Filme implements Serializable {

    private String id;
    private String nome;
    private String genero;
    private int ano;
    private String classificacao;
    private int duracao;
    private String imagem; // Base64

    public Filme() {
    }

    public Filme(String id,
                 String nome,
                 String genero,
                 int ano,
                 String classificacao,
                 int duracao,
                 String imagem) {
        this.id = id;
        this.nome = nome;
        this.genero = genero;
        this.ano = ano;
        this.classificacao = classificacao;
        this.duracao = duracao;
        this.imagem = imagem;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }

    public String getClassificacao() { return classificacao; }
    public void setClassificacao(String classificacao) { this.classificacao = classificacao; }

    public int getDuracao() { return duracao; }
    public void setDuracao(int duracao) { this.duracao = duracao; }

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }
}