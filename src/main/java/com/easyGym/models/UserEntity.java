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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author manel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;
    @NotBlank
    @Size(max = 30)
    @Column(unique = true)
    private String username;
    @NotNull
    private String name;
    private String lastname;
    @Email
    @NotBlank
    @Size(max = 80)
    @Column(unique = true)
    private String email;
    @NotBlank
    private String password;
    @NotNull
    @Column(unique = true)
    private String phone;
    private LocalDate registration_date;
    @NotNull
    @Past(message = "La edad debe estar en el pasado")
    private Date birth_date;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class, cascade = CascadeType.ALL)
    @JoinTable(name = "user_rol",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<RoleEntity> roles;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_gimnasio",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "id_gimnasio")
    )
    @JsonIgnore
    private Set<Gimnasio> gimnasios = new HashSet<>();

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Actividad> actividades;

    /**
     * Metodo para darle el valor actual a la fecha de registro
     */
    @PrePersist
    protected void onCreate() {
        registration_date = LocalDate.now();
    }

    /**
     * Metodo que se llama desde el servicio para agregar un usuario al gimnasio
     * @param gimnasio 
     */
    public void agregarGimnasio(Gimnasio gimnasio) {
        this.gimnasios.add(gimnasio);
        gimnasio.getUsers().add(this);
    }

    /**
     * Metodo que se llama desde el servicio para vincular una actividad
     * @param actividad 
     */
    public void agregarActividad(Actividad actividad) {
        this.actividades.add(actividad);
        actividad.getUsers().add(this);
    }

    /**
     * Metodo que se llama desde el servicio para desvincular una actividad
     * @param actividad 
     */
    public void removerActividad(Actividad actividad) {
        actividades.remove(actividad);
        actividad.getUsers().remove(this);
    }
}
