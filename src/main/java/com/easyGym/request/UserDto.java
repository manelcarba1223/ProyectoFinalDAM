/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.request;

import com.easyGym.models.Gimnasio;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author manel
 */

//Esta clase sirve para evitar usar la clase UserEntity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String username;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private Date birth_date;
    private Set<String> roles;
    private Set<Gimnasio> gimnasios;

}
