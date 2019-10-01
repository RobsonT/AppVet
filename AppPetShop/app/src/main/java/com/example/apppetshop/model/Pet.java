package com.example.apppetshop.model;

import java.util.Date;

public class Pet {
    private int id;
    private String nome;
    private String sexo;
    private boolean castrado;
    private Date dataNascimento;
    private String tipo;
    private String porte;
    private double peso;
    private int idRaca;
    private Straing imagem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public boolean isCastrado() {
        return castrado;
    }

    public void setCastrado(boolean castrado) {
        this.castrado = castrado;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getIdRaca() {
        return idRaca;
    }

    public void setIdRaca(int idRaca) {
        this.idRaca = idRaca;
    }

    public Straing getImagem() {
        return imagem;
    }

    public void setImagem(Straing imagem) {
        this.imagem = imagem;
    }
}
