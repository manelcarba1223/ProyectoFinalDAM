/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.services;

import com.easyGym.models.Gimnasio;
import com.easyGym.models.Sala;
import com.easyGym.models.UserEntity;
import com.easyGym.repository.GimnasioRepository;
import com.easyGym.repository.SalaRepository;
import com.easyGym.repository.UserRepository;
import com.easyGym.request.GimnasioDto;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author manel
 */
@Service
public class GimnasioService {

    @Autowired
    GimnasioRepository gimnasioRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SalaRepository salaRepository;

    /**
     * Metodo que obtiene los gimnasios y crea una lista de gimnasios
     *
     * @return devuelve la lista de gimnasios
     */
    public List<GimnasioDto> verGimnasios() {
        List<Gimnasio> gimnasios = (List<Gimnasio>) gimnasioRepository.findAll();

        return gimnasios.stream()
                .map(gimnasio -> {
                    GimnasioDto gimnasioDTO = new GimnasioDto();
                    gimnasioDTO.setId_gimnasio(gimnasio.getId_gimnasio());
                    gimnasioDTO.setNombre(gimnasio.getNombre());
                    gimnasioDTO.setCiudad(gimnasio.getCiudad());
                    gimnasioDTO.setNumeroUsuarios(gimnasio.getUsers().size());
                    gimnasioDTO.setDescripcion(gimnasio.getDescripcion());
                    gimnasioDTO.setSalas(gimnasio.getSalas());
                    // Convertir la lista de usuarios a un conjunto
                    Set<String> nombresUsuariosSet = new HashSet<>(gimnasio.getUsers().stream().map(UserEntity::getUsername).collect(Collectors.toList()));
                    gimnasioDTO.setUsuarios(nombresUsuariosSet);
                    return gimnasioDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * Metodo que crea un gimnasio en la BBDD
     *
     * @param gimnasio
     * @return el valor del gimnasio
     */
    public Gimnasio guardarGimnasio(Gimnasio gimnasio) {
        return gimnasioRepository.save(gimnasio);
    }

    /**
     * Metodo que actualiza un gimnasio
     *
     * @param id 
     * @param gimnasio
     * @return el valor del gimnasio o null si el gimnasio no existe por id
     */
    public Gimnasio actualizarGimnasio(int id, Gimnasio gimnasio) {
        Optional<Gimnasio> gimnasioExistente = gimnasioRepository.findById(id);
        if (gimnasioExistente.isPresent()) {
            Gimnasio gimnasioActualizado = gimnasioExistente.get();
            gimnasioActualizado.setNombre(gimnasio.getNombre());
            gimnasioActualizado.setCiudad(gimnasio.getCiudad());
            gimnasioActualizado.setDescripcion(gimnasio.getDescripcion());
            return gimnasioRepository.save(gimnasioActualizado);
        } else {
            return null;
        }
    }

    /**
     * Metodo que elimina un gimnasio por id
     *
     * @param id
     */
    public void borrarGimnasioPorId(int id) {
        gimnasioRepository.deleteById(id);
    }

    /**
     * Metodo que devuelve un gimnasio por nombre
     *
     * @param nombre 
     * @return el valor del gimnasio
     */
    public Optional<Gimnasio> buscarPorNombre(String nombre) {
        return gimnasioRepository.findByNombre(nombre);
    }

    /**
     * Metodo que devuelve un gimnasio con el formato GimnasioDTO por nombre
     *
     * @param nombre
     * @return el valor del gimnasio o null si no existe por nombre
     */
    public GimnasioDto buscarPorNombreDto(String nombre) {
        Optional<UserEntity> usuarioOptional = userRepository.findByUsername(nombre);
        if (usuarioOptional.isPresent()) {
            UserEntity usuario = usuarioOptional.get();
            Gimnasio gimnasio = (Gimnasio) usuario.getGimnasios();
            GimnasioDto gimnasioDto = new GimnasioDto();
            gimnasioDto.setId_gimnasio(gimnasio.getId_gimnasio());
            gimnasioDto.setNombre(gimnasio.getNombre());
            gimnasioDto.setCiudad(gimnasio.getCiudad());
            gimnasioDto.setNumeroUsuarios(gimnasio.getUsers().size());
            gimnasioDto.setSalas(gimnasio.getSalas());
            gimnasioDto.setDescripcion(gimnasio.getDescripcion());
            Set<String> nombresUsuariosSet = new HashSet<>(gimnasio.getUsers().stream().map(UserEntity::getUsername).collect(Collectors.toList()));
            gimnasioDto.setUsuarios(nombresUsuariosSet);
            return gimnasioDto;
        } else {
            return null;
        }
    }

    /**
     * Metodo que devuelve un gimnasio por id
     *
     * @param id del gimnasio
     * @return el valor del gimnasio
     */
    public Optional<Gimnasio> buscarPorIdCompleto(int id) {
        return gimnasioRepository.findById(id);
    }

    /**
     * Metodo que devuelve un gimnasio con el formato GimnasioDTO por id
     *
     * @param id
     * @return el valor del gimnasio o null si no existe por id
     */
    public GimnasioDto buscarPorIdDto(int id) {
        Optional<Gimnasio> gimnasioOptional = gimnasioRepository.findById(id);
        if (gimnasioOptional.isPresent()) {
            Gimnasio gimnasio = gimnasioOptional.get();
            GimnasioDto gimnasioDto = new GimnasioDto();
            gimnasioDto.setId_gimnasio(gimnasio.getId_gimnasio());
            gimnasioDto.setNombre(gimnasio.getNombre());
            gimnasioDto.setCiudad(gimnasio.getCiudad());
            gimnasioDto.setNumeroUsuarios(gimnasio.getUsers().size());
            gimnasioDto.setSalas(gimnasio.getSalas());
            gimnasioDto.setDescripcion(gimnasio.getDescripcion());
            Set<String> nombresUsuariosSet = new HashSet<>(gimnasio.getUsers().stream().map(UserEntity::getUsername).collect(Collectors.toList()));
            gimnasioDto.setUsuarios(nombresUsuariosSet);
            return gimnasioDto;
        } else {
            return null;
        }
    }

    /**
     * Metodo que registra un usuario en un gimnasio
     *
     * @param usuarioId
     * @param gimnasioId
     */
    public void agregarUsuarioAGimnasio(int usuarioId, int gimnasioId) {
        try {
            UserEntity usuario = userRepository.findById(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + usuarioId));

            Gimnasio gimnasio = gimnasioRepository.findById(gimnasioId)
                    .orElseThrow(() -> new EntityNotFoundException("Gimnasio no encontrado con ID: " + gimnasioId));

            if (usuario.getGimnasios().contains(gimnasio)) {
                throw new IllegalStateException("El usuario ya está asociado con este gimnasio");
            }

            usuario.agregarGimnasio(gimnasio);

            userRepository.save(usuario);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("No se pudo agregar usuario al gimnasio: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error interno al agregar usuario al gimnasio: " + e.getMessage());
        }
    }

    /**
     * Metodo que sube el logo a la base de datos
     * @param id
     * @param file
     * @throws IOException 
     */
    public void uploadLogo(int id, MultipartFile file) throws IOException {
        Optional<Gimnasio> optionalGimnasio = gimnasioRepository.findById(id);
        if (optionalGimnasio.isPresent()) {
            Gimnasio gimnasio = optionalGimnasio.get();
            gimnasio.setLogo(file.getBytes());
            gimnasioRepository.save(gimnasio);
        } else {
            throw new IllegalArgumentException("Gimnasio not found with id: " + id);
        }
    }

    /**
     * Metodo que obtiene el logo de la base de datos
     * @param id
     * @return el logo
     */
    public Optional<byte[]> getLogo(int id) {
        Optional<Gimnasio> optionalGimnasio = gimnasioRepository.findById(id);
        //El metodo map aplica una función a un optional, en este caso aplica la función getLogo a el gimnasio
        return optionalGimnasio.map(Gimnasio::getLogo);
    }

    /**
     * Metodo que devuelve las salas de un gimnasio
     * @param idGimnasio
     * @return una lista de salas que tiene un gimnasio
     */
    public Set<Sala> verSalasPorGimnasio(int idGimnasio) {
        Optional<Gimnasio> gimnasioOptional = gimnasioRepository.findById(idGimnasio);
        if (gimnasioOptional.isPresent()) {
            Gimnasio gimnasio = gimnasioOptional.get();
            return gimnasio.getSalas();
        } else {
            throw new IllegalArgumentException("Gimnasio not found with id: " + idGimnasio);
        }
    }
}
