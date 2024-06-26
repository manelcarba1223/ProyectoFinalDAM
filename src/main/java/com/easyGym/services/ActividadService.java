/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.services;

import com.easyGym.models.Actividad;
import com.easyGym.models.Gimnasio;
import com.easyGym.models.HorasDisponibles;
import com.easyGym.models.Sala;
import com.easyGym.models.UserEntity;
import com.easyGym.repository.ActividadRepository;
import com.easyGym.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Version;
import jakarta.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 *
 * @author manel
 */
@Service
public class ActividadService {

    @Autowired
    ActividadRepository actividadRepository;

    @Autowired
    private SalaService salaService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Metodo para obtener todas las actividades
     *
     * @return una lista con todas las actividades
     */
    public List<Actividad> verActividades() {
        return (List<Actividad>) actividadRepository.findAll();
    }

    /**
     * Metdoo para crear una actividad en una sala
     *
     * @param actividad
     * @param salaId
     * @return la actividad creada
     */
    public Actividad guardarActividad(Actividad actividad, int salaId) {
        if (actividad == null) {
            throw new IllegalArgumentException("La rutina no puede ser nula");
        }

        if (actividad.getNombre() == null || actividad.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la rutina no puede estar vacío");
        }

        // Validar que la hora de inicio sea menor que la hora final
        if (actividad.getHoraInicio().isAfter(actividad.getHoraFinal())) {
            throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora final");
        }

        Optional<Sala> optinalSala = salaService.obtenerSalaPorId(salaId);
        if (optinalSala.isPresent()) {
            Sala sala = optinalSala.get();

            // Obtener las horas disponibles del gimnasio
            Set<HorasDisponibles> horasDisponibles = sala.getHorasDisponibles();

            // Verificar si hay horas disponibles para la actividad
            if (horasDisponibles != null && !horasDisponibles.isEmpty()) {
                for (HorasDisponibles horas : horasDisponibles) {
                    ZonedDateTime horaInicio = horas.getHoraInicio();
                    ZonedDateTime horaFinal = horas.getHoraFinal();
                    // Verificar si la hora de inicio y la hora final de la actividad están dentro de las horas disponibles
                    if (actividad.getHoraInicio().isAfter(horaInicio) && actividad.getHoraFinal().isBefore(horaFinal)) {
                        // Asignar el gimnasio a la actividad
                        actividad.setSala(sala);
                        // Agregar la rutina al gimnasio
                        sala.getActividades().add(actividad);
                        // Guardar la rutina y el gimnasio actualizado
                        actividad = actividadRepository.save(actividad);
                        salaService.guardarSala(sala);
                        return actividad;
                    }
                }
                // Si la actividad no está dentro de las horas disponibles
                throw new IllegalStateException("La actividad no está dentro de las horas disponibles del gimnasio");
            } else {
                // Si no hay horas disponibles
                throw new IllegalStateException("No hay horas disponibles en el gimnasio");
            }
        } else {
            // Manejar el caso en el que no se encuentre el gimnasio
            throw new NoSuchElementException("Sala no encontrado");
        }
    }

    /**
     * Metodo para editar una actividad
     *
     * @param actividad
     * @return la actividad actualizada
     */
    @Transactional
    public Actividad actualizarActividad(Actividad actividad, int salaId) {
        // Comprueba si la rutina ya existe en la base de datos
        Optional<Actividad> actividadExistente = actividadRepository.findById(actividad.getId_actividad());
        Optional<Sala> optinalSala = salaService.obtenerSalaPorId(salaId);
        if (optinalSala.isPresent()) {
            Sala sala = optinalSala.get();

            // Obtener las horas disponibles del gimnasio
            Set<HorasDisponibles> horasDisponibles = sala.getHorasDisponibles();

            // Verificar si hay horas disponibles para la actividad
            if (horasDisponibles != null && !horasDisponibles.isEmpty()) {
                for (HorasDisponibles horas : horasDisponibles) {
                    ZonedDateTime horaInicio = horas.getHoraInicio();
                    ZonedDateTime horaFinal = horas.getHoraFinal();
                    if (actividad.getHoraInicio().isAfter(horaInicio) && actividad.getHoraFinal().isBefore(horaFinal)) {
                        if (actividadExistente.isPresent()) {
                            // Actualiza los campos de la rutina existente
                            Actividad actividadActualizada = actividadExistente.get();
                            actividadActualizada.setNombre(actividad.getNombre());
                            actividadActualizada.setHoraInicio(actividad.getHoraInicio());
                            actividadActualizada.setHoraFinal(actividad.getHoraFinal());
                            actividadActualizada.setCapacidad(actividad.getCapacidad());

                            // Guarda la rutina actualizada en la base de datos
                            return actividadRepository.save(actividadActualizada);
                        } else {
                            throw new EntityNotFoundException("No se encontró la actividad con el ID: " + actividad.getId_actividad());
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Metodo que elimina una actividad por id
     *
     * @param id
     */
    public void borrarActividadPorId(int id) {
        try {
            Optional<Actividad> optionalActividad = actividadRepository.findById(id);
            if (optionalActividad.isPresent()) {
                actividadRepository.deleteById(id);
            } else {
                throw new NoSuchElementException("Actividad no encontrada con ID: " + id);
            }
        } catch (DataIntegrityViolationException e) {
            // Manejar la excepción de violación de integridad, por ejemplo, mostrar un mensaje de error al usuario
            throw new RuntimeException("No se pudo eliminar la actividad debido a una violación de integridad de datos.");
        } catch (StaleObjectStateException e) {
            // Manejar la excepción de concurrencia, por ejemplo, mostrar un mensaje de error al usuario
            throw new RuntimeException("No se pudo eliminar la actividad debido a un problema de concurrencia.");
        }
    }

    /**
     * Metodo que busca en la lista de actividades por nombre
     *
     * @param nombre
     * @return la lista de actividades con ese nombre
     */
    public List<Actividad> buscarPorNombre(String nombre) {
        return actividadRepository.findByNombre(nombre);
    }

    /**
     * Metodo que busca en la lista de actividades por id
     *
     * @param id
     * @return la actividad con ese id
     */
    public Optional<Actividad> buscarPorId(int id) {
        return actividadRepository.findById(id);
    }

    /**
     * Método que agrega un usuario a una actividad.
     *
     * @param usuarioId
     * @param actividadId
     */
    public void agregarUsuarioAActividad(int usuarioId, int actividadId) {
        try {
            // Obtener el usuario desde la base de datos
            UserEntity usuario = userRepository.findById(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + usuarioId));

            // Obtener la actividad desde la base de datos
            Actividad actividad = actividadRepository.findById(actividadId)
                    .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada con ID: " + actividadId));

            if (actividad.getUsers().size() < actividad.getCapacidad()) {
                usuario.agregarActividad(actividad);
                userRepository.save(usuario);
            } else {
                throw new IllegalStateException("La actividad ya está llena");
            }

            // Guardar los cambios en la base de datos
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("No se pudo agregar usuario a la actividad: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error interno al agregar usuario a la actividad: " + e.getMessage());
        }
    }

    /**
     * Metodo que desvincula un usuario de una actividad
     *
     * @param usuarioId
     * @param actividadId
     */
    public void desregistrarUsuarioDeActividad(int usuarioId, int actividadId) {
        try {
            UserEntity usuario = userRepository.findById(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + usuarioId));

            Actividad actividad = actividadRepository.findById(actividadId)
                    .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada con ID: " + actividadId));

            if (!usuario.getActividades().contains(actividad)) {
                throw new IllegalStateException("El usuario no está asociado con esta actividad");
            }

            usuario.removerActividad(actividad);

            userRepository.save(usuario);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("No se pudo desregistrar usuario de la actividad: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error interno al desregistrar usuario de la actividad: " + e.getMessage());
        }
    }
}
