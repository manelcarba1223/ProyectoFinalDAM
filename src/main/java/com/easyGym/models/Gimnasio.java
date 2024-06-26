/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Data;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

/**
 *
 * @author manel
 */
@Entity
@Table(name = "gimnasio")
@Data
public class Gimnasio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_gimnasio;

    @NotNull
    @Column(unique = true)
    private String nombre;

    private byte[] logo;

    @NotNull
    private String ciudad;
    private String descripcion;

    @ManyToMany(mappedBy = "gimnasios", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UserEntity> users = new HashSet<>();

    @OneToMany(mappedBy = "gimnasio")
    private Set<Sala> salas;

    // Otros atributos y métodos
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
