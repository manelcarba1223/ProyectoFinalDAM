/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.services;

import com.easyGym.models.RoleEntity;
import com.easyGym.models.UserEntity;
import com.easyGym.repository.GimnasioRepository;
import com.easyGym.repository.UserRepository;
import com.easyGym.request.UserDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author manel
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GimnasioRepository gimnasioRepository;

    /**
     * Metodo que busca un usuario por username
     * @param username
     * @return el usuario con ese nombre
     */
    public UserDto findByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe"));

        Set<String> roles = userEntity.getRoles().stream()
                .map(role -> role.getName().toString()) // Map enum values to strings
                .collect(Collectors.toSet());

        // Crear y devolver un DTO con los mismos valores que la entidad
        return new UserDto(
                userEntity.getUsername(),
                userEntity.getName(),
                userEntity.getLastname(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getPhone(),
                userEntity.getBirth_date(),
                roles,
                userEntity.getGimnasios());
    }

    /**
     * Metodo que busca un usuario por id
     * @param userId
     * @return el usuario con ese id
     */
    public UserEntity findById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + userId + " no existe"));
    }

}
