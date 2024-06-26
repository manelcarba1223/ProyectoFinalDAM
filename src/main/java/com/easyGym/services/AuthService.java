/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.services;

import com.easyGym.models.ERol;
import com.easyGym.models.RoleEntity;
import com.easyGym.models.UserEntity;
import com.easyGym.repository.UserRepository;
import com.easyGym.request.UserDto;
import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author manel
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Método para crear un usuario en el sistema.
     *
     * @param userDto un objeto UserDto que contiene la información del usuario
     * a crear.
     * @return ResponseEntity<?> Una respuesta HTTP que indica el resultado de
     * la operación de creación de usuario.
     */
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {
        System.out.println("Roles recibidos: " + userDto.getRoles());
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("El email ya está en uso.");
        }

        Set<RoleEntity> roles = userDto.getRoles().stream()
                .map(role -> RoleEntity.builder()
                .name(ERol.valueOf(role))
                .build())
                .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .username(userDto.getUsername())
                .name(userDto.getName())
                .lastname(userDto.getLastname())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phone(userDto.getPhone())
                .birth_date(userDto.getBirth_date())
                .roles(roles)
                .build();

        userRepository.save(userEntity);

        return ResponseEntity.ok(userEntity);
    }

}
