/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.services;

import com.easyGym.models.UserEntity;
import com.easyGym.repository.UserRepository;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author manel
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    UserRepository userRepository;
    
    /**
     * Metodo para cargar los datos del usuario según su username
     * @param username
     * @return un objeto UserDetails personalizado añadiendo id
     * @throws UsernameNotFoundException 
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El ususario "+username+ " no existe"));
        
        Collection<? extends GrantedAuthority> authorities = userEntity.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(("ROLE_"+ role.getName().name())))
                .collect(Collectors.toSet());
        
        return new CustomUserDetailsService(userEntity.getUser_id(), userEntity.getUsername(), userEntity.getPassword(), authorities);
    }
    
}
