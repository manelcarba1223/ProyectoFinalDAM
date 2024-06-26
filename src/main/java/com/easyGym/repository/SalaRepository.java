/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.easyGym.repository;

import com.easyGym.models.Sala;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author manel
 */
public interface SalaRepository extends CrudRepository<Sala, Integer> {

    Optional<Sala> findByNombre(String nombre);


}
