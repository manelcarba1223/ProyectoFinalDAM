/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.easyGym.controller;

import com.easyGym.models.Gimnasio;
import com.easyGym.models.HorasDisponibles;
import com.easyGym.models.Sala;
import com.easyGym.request.GimnasioDto;
import com.easyGym.services.GimnasioService;
import com.easyGym.services.SalaService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author manel
 */
@RestController
@RequestMapping("/gimnasio")
public class GimnasioController {

    @Autowired
    GimnasioService gimnasioServicio;
    
    @Autowired
    SalaService salaService;

    @GetMapping("/gimnasios")
    public ResponseEntity<List<GimnasioDto>> listarGimnasios() {
        List<GimnasioDto> gimnasiosDTO = gimnasioServicio.verGimnasios();
        return new ResponseEntity<>(gimnasiosDTO, HttpStatus.OK);
    }

    @PostMapping("/crearGimnasio")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DUEÑO')")
    public ResponseEntity<Gimnasio> crearGimnasio(@RequestBody Gimnasio gimnasio) {
        Gimnasio gimnasioResponse = gimnasioServicio.guardarGimnasio(gimnasio);
        return new ResponseEntity<>(gimnasioResponse, HttpStatus.OK);
    }

    @PutMapping("/actualizarGimnasio/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DUEÑO')")
    public ResponseEntity<Gimnasio> actualizarGimnasio(@PathVariable("id") int id, @RequestBody Gimnasio gimnasio) {
        Gimnasio gimnasioActualizado = gimnasioServicio.actualizarGimnasio(id, gimnasio);
        if (gimnasioActualizado != null) {
            return new ResponseEntity<>(gimnasioActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/verGimnasio/{id}")
    public ResponseEntity<GimnasioDto> verPorId(@PathVariable("id") int id) throws Exception {
        GimnasioDto gimnasio = gimnasioServicio.buscarPorIdDto(id);
        return new ResponseEntity<>(gimnasio, HttpStatus.OK);
    }

    @GetMapping("/verGimnasioNombre/{nombre}")
    public ResponseEntity<GimnasioDto> verPorNombre(@PathVariable("nombre") String nombre) throws Exception {
        GimnasioDto gimnasio = gimnasioServicio.buscarPorNombreDto(nombre);
        return new ResponseEntity<>(gimnasio, HttpStatus.OK);
    }

    @DeleteMapping("/gimnasios/{id}")
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DUEÑO')")
    public ResponseEntity<String> borrarGimnasioPorId(@PathVariable int id) {
        try {
            gimnasioServicio.borrarGimnasioPorId(id);
            return new ResponseEntity<>("Gimnasio eliminado exitosamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("No se pudo eliminar el gimnasio", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/agregarUsuarioAGimnasio")
    public ResponseEntity<String> agregarUsuarioAGimnasio(
            @RequestParam("usuarioId") int usuarioId,
            @RequestParam("gimnasioId") int gimnasioId) {

        gimnasioServicio.agregarUsuarioAGimnasio(usuarioId, gimnasioId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadLogo(@PathVariable int id, @RequestParam("logo") MultipartFile file) {
        try {
            gimnasioServicio.uploadLogo(id, file);
            return ResponseEntity.status(HttpStatus.OK).body("Logo uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading logo.");
        }
    }

    @GetMapping("/{id}/logo")
    public ResponseEntity<byte[]> getLogo(@PathVariable int id) {
        Optional<byte[]> logo = gimnasioServicio.getLogo(id);
        return logo.map(bytes -> ResponseEntity.ok().body(bytes))
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("{idGimnasio}/ver-salas")
    public ResponseEntity<Set<Sala>> obtenerSalasPorGimnasio(@PathVariable int idGimnasio) {
        Set<Sala> salas = gimnasioServicio.verSalasPorGimnasio(idGimnasio);
        return new ResponseEntity<>(salas, HttpStatus.OK);
    }
}
