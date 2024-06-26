/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.easyGym.repository;

import com.easyGym.models.Actividad;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author manel
 */
@Repository
public interface ActividadRepository extends CrudRepository<Actividad, Integer> {

    List<Actividad> findByNombre(String nombre);

}
