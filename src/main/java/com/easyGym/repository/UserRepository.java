/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.repository;

import com.easyGym.models.UserEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author manel
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    public Optional<UserEntity> findByUsername(String username);


    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
