/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author manel
 */
@Entity
@Table(name = "sala")
@Data
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSala;

    @NotNull
    private String nombre;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.PERSIST)
    private Set<Actividad> actividades;

    @ManyToOne
    @JsonIgnore
    private Gimnasio gimnasio;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL)
    private Set<HorasDisponibles> horasDisponibles;

    public void agregarHorasDisponibles(HorasDisponibles horas) {
        if (horasDisponibles == null) {
            horasDisponibles = new HashSet<>();
        }
        horasDisponibles.add(horas);
        horas.setSala(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSala, nombre, actividades, gimnasio); // Solo incluye los campos necesarios para calcular el hash
    }

}
