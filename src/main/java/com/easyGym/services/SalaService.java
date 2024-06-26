/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.services;

import com.easyGym.models.Gimnasio;
import com.easyGym.models.HorasDisponibles;
import com.easyGym.models.Sala;
import com.easyGym.repository.GimnasioRepository;
import com.easyGym.repository.SalaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author manel
 */
@Service
@Transactional
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private GimnasioRepository gimnasioRepository;

    /**
     * Metodo que crea una sala
     * @param sala
     * @return la sala creada
     */
    public Sala guardarSala(Sala sala) {
        return salaRepository.save(sala);
    }

    /**
     * Metodo que crea una sala en un gimnasio
     * @param idGimnasio
     * @param sala
     * @return la sala creada 
     */
    public Sala guardarSalaEnGimnasio(int idGimnasio, Sala sala) {
    Optional<Gimnasio> gimnasioOptional = gimnasioRepository.findById(idGimnasio);
    if (gimnasioOptional.isPresent()) {
        Gimnasio gimnasio = gimnasioOptional.get();

        // Verificamos si ya existe una sala con el mismo nombre en el gimnasio
        boolean salaExiste = gimnasio.getSalas().stream()
            .anyMatch(s -> s.getNombre().equalsIgnoreCase(sala.getNombre()));
        
        if (salaExiste) {
            throw new IllegalArgumentException("Ya existe una sala con el nombre: " + sala.getNombre() + " en el gimnasio con id: " + idGimnasio);
        }

        sala.setGimnasio(gimnasio); // Establecemos el gimnasio en la sala
        return salaRepository.save(sala);
    } else {
        throw new IllegalArgumentException("Gimnasio not found with id: " + idGimnasio);
    }
}

    /**
     * Metodo que devuelve una sala por id
     * @param id
     * @return la sala que tiene ese id
     */
    public Optional<Sala> obtenerSalaPorId(int id) {
        return salaRepository.findById(id);
    }

    /**
     * Metodo que obtiene todas las salas de la BBDD
     * @return todas las salas
     */
    public Iterable<Sala> obtenerTodasLasSalas() {
        return salaRepository.findAll();
    }

    /**
     * Metodo que actualiza una sala por id
     * @param id
     * @param salaActualizada
     * @return la sala actualizada
     */
    public Sala actualizarSala(int id, Sala salaActualizada) {
        Optional<Sala> salaOptional = salaRepository.findById(id);
        if (salaOptional.isPresent()) {
            Sala salaExistente = salaOptional.get();
            salaExistente.setNombre(salaActualizada.getNombre());
            // Actualizar otros campos según sea necesario
            return salaRepository.save(salaExistente);
        } else {
            throw new IllegalArgumentException("La sala con el ID proporcionado no existe");
        }
    }

    /**
     * Metodo que elimina una sala por id
     * @param id 
     */
    public void eliminarSalaPorId(int id) {
        salaRepository.deleteById(id);
    }

    /**
     * Metodo que agrega horas disponibles a una sala por id
     * @param idSala
     * @param horas 
     */
    public void agregarHorasDisponibles(int idSala, HorasDisponibles horas) {
        // Validar que la hora de inicio sea menor que la hora final
        if (horas.getHoraInicio().isAfter(horas.getHoraFinal())) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora final");
        }

        Optional<Sala> salaOptional = salaRepository.findById(idSala);
        if (salaOptional.isPresent()) {
            Sala sala = salaOptional.get();
            sala.agregarHorasDisponibles(horas);
            salaRepository.save(sala);
        } else {
            throw new IllegalArgumentException("Gimnasio not found with id: " + idSala);
        }
    }

    /**
     * Metodo que devuelve las horas disponibles de una sala por id
     * @param idSala
     * @return las horas disponibles de la sala
     */
    public Set devolverHorasDisponiblesPorId(int idSala) {
        Optional<Sala> salaOptional = salaRepository.findById(idSala);
        if (salaOptional.isPresent()) {
            Sala sala = salaOptional.get();
            return sala.getHorasDisponibles();
        } else {
            throw new IllegalArgumentException("Gimnasio not found with id: " + idSala);
        }
    }

  
}
