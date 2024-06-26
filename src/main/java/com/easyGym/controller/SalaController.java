/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.controller;

import com.easyGym.models.HorasDisponibles;
import com.easyGym.models.Sala;
import com.easyGym.services.SalaService;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author manel
 */
@RestController
@RequestMapping("/salas")
public class SalaController {

    private final SalaService salaService;

    @Autowired
    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DUEÑO')")
    public ResponseEntity<Sala> guardarSala(@RequestBody Sala sala) {
        Sala nuevaSala = salaService.guardarSala(sala);
        return new ResponseEntity<>(nuevaSala, HttpStatus.CREATED);
    }

    @PostMapping("{idGimnasio}/guardarSala")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DUEÑO')")
    public ResponseEntity<Sala> guardarSalaEnGimnasio(@PathVariable int idGimnasio, @RequestBody Sala sala) {
        Sala nuevaSala = salaService.guardarSalaEnGimnasio(idGimnasio, sala);
        return new ResponseEntity<>(nuevaSala, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> obtenerSalaPorId(@PathVariable int id) {
        Optional<Sala> salaOptional = salaService.obtenerSalaPorId(id);
        return salaOptional.map(sala -> new ResponseEntity<>(sala, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/todas")
    public ResponseEntity<Iterable<Sala>> obtenerTodasLasSalas() {
        Iterable<Sala> salas = salaService.obtenerTodasLasSalas();
        return new ResponseEntity<>(salas, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DUEÑO')")
    public ResponseEntity<Sala> actualizarSala(@PathVariable int id, @RequestBody Sala salaActualizada) {
        Sala salaActualizadaRespuesta = salaService.actualizarSala(id, salaActualizada);
        return new ResponseEntity<>(salaActualizadaRespuesta, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DUEÑO')")
    public ResponseEntity<Void> eliminarSalaPorId(@PathVariable int id) {
        salaService.eliminarSalaPorId(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/agregar-horas")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DUEÑO')")
    public ResponseEntity<Void> agregarHorasDisponibles(@PathVariable int id, @RequestBody HorasDisponibles horas) {
        salaService.agregarHorasDisponibles(id, horas);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}/horas-disponibles")
    public ResponseEntity<Set<HorasDisponibles>> devolverHorasDisponiblesPorId(@PathVariable int id) {
        Set<HorasDisponibles> horasDisponibles = salaService.devolverHorasDisponiblesPorId(id);
        return new ResponseEntity<>(horasDisponibles, HttpStatus.OK);
    }

    
}
