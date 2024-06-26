/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.controller;

import com.easyGym.models.Actividad;
import com.easyGym.models.Sala;
import com.easyGym.models.UserEntity;
import com.easyGym.services.ActividadService;
import com.easyGym.services.GimnasioService;
import com.easyGym.services.SalaService;
import com.easyGym.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author manel
 */
@RestController
@RequestMapping("/actividad")
public class ActividadController {

    @Autowired
    GimnasioService gimnasioServicio;

    @Autowired
    ActividadService actividadService;

    @Autowired
    UserService userService;
    
    @Autowired
    SalaService salaService;

    /**
     * Metodo para obtener todas las actividades
     * @return la lista con las actividades
     */
    @GetMapping("/actividades")
    public ResponseEntity<List<Actividad>> verTodasLasActividades() {
        List<Actividad> actividades = actividadService.verActividades();
        return ResponseEntity.ok(actividades);
    }

    /**
     * Metodo para ver las actividades de una determinada sala
     * @param salaId
     * @return la lista de actividades de una sala por id
     * @throws Exception 
     */
    @GetMapping("/actividades/{salaId}")
    public ResponseEntity<Set<Actividad>> verActividadesPorSala(@PathVariable("salaId") int salaId) throws Exception {
        Sala sala = salaService.obtenerSalaPorId(salaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la sala con el id: " + salaId));

        Set<Actividad> actividades = sala.getActividades();
        return new ResponseEntity<>(actividades, HttpStatus.OK);
    }

    /**
     * Metodo para crear una actividad en una sala
     * @param actividad
     * @param salaId
     * @return un json con la actividad creada
     */
    @PostMapping("/crearActividadEnSala/{salaId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DUEÑO') or hasRole('ROLE_ENTRENADOR')")
    public ResponseEntity<Actividad> crearActividadEnSala(@RequestBody Actividad actividad, @PathVariable int salaId) {
        Actividad nuevaActividad = actividadService.guardarActividad(actividad, salaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaActividad);
    }

    /**
     * Metodo para actualizar una actividad
     * @param actividad
     * @param id
     * @return la actividad actualizada
     */
    @PutMapping("/actualizarActividad/{id}/{salaId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DUEÑO') or hasRole('ROLE_ENTRENADOR')")
    public ResponseEntity<Actividad> actualizarActividad(@RequestBody Actividad actividad, @PathVariable int id, @PathVariable int salaId) {
        actividad.setId_actividad(id); // Establece el ID en la instancia de Actividad
        Actividad actividadActualizada = actividadService.actualizarActividad(actividad, salaId);
        return ResponseEntity.ok(actividadActualizada);
    }

    /**
     * Metodo para borrar una actividad por id
     * @param id
     * @return respuesta vacía con estado OK
     */
    @DeleteMapping("/borrarActividad/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DUEÑO') or hasRole('ROLE_ENTRENADOR')")
    public ResponseEntity<Void> borrarActividad(@PathVariable int id) {
        actividadService.borrarActividadPorId(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Metodo para buscar actividades por nombre
     * @param nombre
     * @return lista de actividades con ese nombre
     */
    @GetMapping("/buscarPorNombre")
    public ResponseEntity<List<Actividad>> buscarActividadPorNombre(@RequestParam String nombre) {
        List<Actividad> actividades = actividadService.buscarPorNombre(nombre);
        return ResponseEntity.ok(actividades);
    }

    /**
     * Metodo para ver una actividad por id
     * @param id
     * @return la actividad con ese id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Actividad> verActividadPorId(@PathVariable int id) {
        Actividad actividad = actividadService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la actividad con el id: " + id));
        return ResponseEntity.ok(actividad);
    }

    /**
     * Metodo para agregar un usuario a una actividad
     * @param usuarioId
     * @param actividadId
     * @return respuesta vacía con estado OK
     */
    @PostMapping("/agregarUsuarioAActividad")
    public ResponseEntity<Void> agregarUsuarioAActividad(
            @RequestParam("usuarioId") int usuarioId,
            @RequestParam("actividadId") int actividadId) {

        try {
            actividadService.agregarUsuarioAActividad(usuarioId, actividadId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se pudo agregar usuario a la actividad: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al agregar usuario a la actividad: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno al agregar usuario a la actividad: " + e.getMessage());
        }
    }

    /**
     * Metodo para desregistrar un usuario de una actividad
     * @param usuarioId
     * @param actividadId
     * @return respuesta vacía con estado OK
     */
    @DeleteMapping("/desregistrarUsuarioDeActividad")
    public ResponseEntity<Void> desregistrarUsuarioDeActividad(
            @RequestParam("usuarioId") int usuarioId,
            @RequestParam("actividadId") int actividadId) {

        try {
            actividadService.desregistrarUsuarioDeActividad(usuarioId, actividadId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se pudo desregistrar usuario de la actividad: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al desregistrar usuario de la actividad: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno al desregistrar usuario de la actividad: " + e.getMessage());
        }
    }

    /**
     * Metodo para verificar si un usuario está registrado en una actividad
     * @param usuarioId
     * @param actividadId
     * @return true si el usuario está registrado, false si no
     */
    @GetMapping("/verificarRegistroUsuarioEnActividad")
    public ResponseEntity<Boolean> verificarRegistroUsuarioEnActividad(@RequestParam int usuarioId, @RequestParam int actividadId) {
        try {
            UserEntity usuario = userService.findById(usuarioId);

            Actividad actividad = actividadService.buscarPorId(actividadId)
                    .orElseThrow(() -> new EntityNotFoundException("La actividad con ID " + actividadId + " no existe"));

            // Verificar si el usuario está registrado en la actividad
            boolean registrado = usuario.getActividades().contains(actividad);
            return ResponseEntity.ok(registrado);
        } catch (EntityNotFoundException e) {
            // Manejar el caso en el que no se encuentre el usuario o la actividad
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se pudo verificar el registro del usuario en la actividad: " + e.getMessage());
        }
    }
}