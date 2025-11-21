package com.com.jwtdemo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "competencia")
public class Competencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCompetencia;

    @Column(name = "nombreCompetencia")
    private String nombreCompetencia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cursos")
    private Curso curso;

    // Constructores
    public Competencia() {}

    public Competencia(int idCompetencia, String nombreCompetencia, Curso curso) {
        this.idCompetencia = idCompetencia;
        this.nombreCompetencia = nombreCompetencia;
        this.curso = curso;
    }

    // Getters y Setters
    public int getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(int idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public String getNombreCompetencia() {
        return nombreCompetencia;
    }

    public void setNombreCompetencia(String nombreCompetencia) {
        this.nombreCompetencia = nombreCompetencia;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}