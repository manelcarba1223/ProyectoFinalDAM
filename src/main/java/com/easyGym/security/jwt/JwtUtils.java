/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author manel
 */
@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    /**
     * Genera un token de acceso JWT con el nombre de usuario proporcionado.
     * 
     * @param userName el nombre de usuario para el cual se generará el token.
     * @return String el token de acceso generado.
     */
    public String generateAccesToken(String username) {
        return Jwts.builder().setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    /**
     * Valida si un token de acceso JWT dado es válido.
     * 
     * @param token el token de acceso JWT a validar.
     * @return boolean true si el token es válido, false de lo contrario.
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            log.error("Token invalido, error " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene el nombre de usuario almacenado en un token de acceso JWT.
     * 
     * @param token el token de acceso JWT del cual se obtendrá el nombre de usuario.
     * @return String el nombre de usuario extraído del token.
     */
    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }
    
    /**
     * Obtiene un claim específico del token de acceso JWT.
     * 
     * @param token el token de acceso JWT del cual se obtendrá el claim.
     * @param claimsFunction la función que especifica qué claim se debe extraer del token.
     * @param <T> el tipo de dato del claim que se desea extraer.
     * @return T el claim específico extraído del token.
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsFunction){
        Claims claims=extractAllClaims(token);
        return claimsFunction.apply(claims);
    }

    /**
     * Extrae todos los claims almacenados en un token de acceso JWT.
     * 
     * @param token el token de acceso JWT del cual se extraerán los claims.
     * @return Claims los claims extraídos del token.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene la clave de firma utilizada para firmar y verificar tokens JWT.
     * 
     * @return Key la clave de firma utilizada para tokens JWT.
     */
    public Key getSignatureKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
