/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.Data;

/**
 *
 * @author manel
 */
@Entity
@Table(name = "horas_disponibles")
@Data
public class HorasDisponibles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private ZonedDateTime horaInicio;

    @NotNull
    private ZonedDateTime horaFinal;

    @ManyToOne
    @JoinColumn(name = "idSala")
    @JsonIgnore
    private Sala sala;

    @Override
    public int hashCode() {
        return Objects.hash(id, horaInicio, horaFinal, sala); // Solo incluye los campos necesarios para calcular el hash
    }
}
