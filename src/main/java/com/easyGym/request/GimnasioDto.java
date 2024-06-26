/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.request;

import com.easyGym.models.Actividad;
import com.easyGym.models.Sala;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author manel
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GimnasioDto {

    private int id_gimnasio;
    private String nombre;
    private String ciudad;
    private String descripcion;
    private int numeroUsuarios;
    private Set<String> usuarios;
    private Set<Sala> salas;
}
