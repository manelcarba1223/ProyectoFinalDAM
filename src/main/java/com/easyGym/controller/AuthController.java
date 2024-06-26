/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.controller;

import com.easyGym.request.UserDto;
import com.easyGym.services.AuthService;
import com.easyGym.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author manel
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    AuthService authService;
    
    @Autowired
    UserDetailsServiceImpl userDetails;

    @PostMapping(value = "register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto, BindingResult result) {
        
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Error de validación");
        }
        return ResponseEntity.ok(authService.register(userDto));
    }
    
    @GetMapping("/actual-usuario")
    public User getUser(Principal principal){
        return (User) this.userDetails.loadUserByUsername(principal.getName());
    }
}
