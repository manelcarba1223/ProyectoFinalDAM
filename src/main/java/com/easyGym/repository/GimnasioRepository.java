/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.easyGym.repository;

import com.easyGym.models.Gimnasio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author manel
 */
@Repository
public interface GimnasioRepository extends CrudRepository<Gimnasio, Integer> {

    Optional<Gimnasio> findByNombre(String nombre);

}
