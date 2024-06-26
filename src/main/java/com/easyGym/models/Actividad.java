/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import lombok.Data;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

/**
 *
 * @author manel
 */
@Entity
@Table(name = "actividad")
@Data
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_actividad;
    @NotNull
    private String nombre;
    private String descripcion;
    @NotNull
    private ZonedDateTime horaInicio;

    @NotNull
    private ZonedDateTime horaFinal;
    
    @Min(value = 1, message = "La capacidad debe ser mayor o igual a 1")
    @NotNull
    private int capacidad;
    
    

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_actividad",
            joinColumns = {
                @JoinColumn(name = "id_actividad", referencedColumnName = "id_actividad",
                        nullable = false, updatable = false, insertable = false)},
            inverseJoinColumns = {
                @JoinColumn(name = "user_id", referencedColumnName = "user_id",
                        nullable = false, updatable = false, insertable = false)})
    private Set<UserEntity> users;

    
    @ManyToOne
    @JsonIgnore
    private Sala sala;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
