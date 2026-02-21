package org.example.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.example.models.dtos.FuenteEstaticaDTO;
import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.entities.hecho.Hecho;
import org.example.service.ServiceFuenteEstatica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/estatica")
public class ControllerFuenteEstatica {

    @Autowired
    private final ServiceFuenteEstatica serviceEstatica;

    public ControllerFuenteEstatica(ServiceFuenteEstatica serviceEstatica) {
        this.serviceEstatica = serviceEstatica;
    }

    @GetMapping("/fuentesLeidas")
    @Operation(
            summary = "Listar fuentes leídas",
            description = "Devuelve un listado con todas las fuentes estáticas que han sido leídas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de fuentes leídas obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = FuenteEstatica.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<List<FuenteEstatica>> findByLeidas() {
        log.info("Empezando busqueda de fuentes leídas");
        List<FuenteEstatica> fe = this.serviceEstatica.findByLeidas();
        log.debug("Fuentes leídas obtenidas: {}", fe.size());
        return ResponseEntity.status(200).body(fe);
    }


    @GetMapping("/fuentes")
    @Operation(
            summary = "Listar fuentes",
            description = "Devuelve un listado con todas las fuentes estáticas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de fuentes obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = FuenteEstaticaDTO.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<List<FuenteEstaticaDTO>> findAll() {
        log.info("Empezando busqueda de todas las fuentes");
        List<FuenteEstaticaDTO> fe = this.serviceEstatica.findAll();
        log.debug("Fuentes obtenidas: {}", fe.size());
        return ResponseEntity.status(200).body(fe);
    }

    @PostMapping(value = "/subirDataSet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Subir un dataset",
            description = "Permite subir un archivo CSV o PDF para ser procesado como fuente estática"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Archivo subido y procesado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Archivo inválido o vacío"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor al procesar el archivo"
            )
    })
    ResponseEntity<String> subirArchivo(
             @Valid @RequestParam("file") MultipartFile file) {
        log.info("Recibiendo solicitud para subir un dataset: {}", file.getOriginalFilename());
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }
        try {
            Boolean valor = serviceEstatica.subirDataSet(file);
            if (!valor){
                return ResponseEntity.internalServerError().body("Ese tipo de archivo no es soportado");
            }
            log.info("Archivo subido exitosamente: {}", file.getOriginalFilename());
            return ResponseEntity.ok("Archivo subido exitosamente: " + file.getOriginalFilename());
        } catch (Exception e) {
            log.error("Error al subir el archivo: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error al subir el archivo: " + e.getMessage());
        }
    }

    @GetMapping("/hechos")
    @Operation(
            summary = "Listar hechos no leídos",
            description = "Devuelve un listado con los hechos obtenidos de las fuentes estáticas que aún no han sido marcados como leídos"
    )
    @ApiResponses( value = {
        @ApiResponse(
                responseCode = "200",
                description = "Listado de hechos no leídos obtenido correctamente",
                content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(
                                schema = @Schema(implementation = Hecho.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor"
        )
    })
    public ResponseEntity<List<List<Hecho>>> getHechosNoLeidos() {
        log.info("Empezando busqueda de hechos no leídos");
        List<List<Hecho>> hechos = this.serviceEstatica.leerDataSetNoLeidos();
        log.debug("Hechos no leídos obtenidos: {}", hechos.size());
        return ResponseEntity.status(HttpStatus.OK).body(hechos);
    }


    @PutMapping("/reprocesar/{fuente}")
    @Operation(
            summary = "Reprocesar una fuente",
            description = "Permite reprocesar una fuente estática específica, marcándola como no procesada para que pueda ser leída nuevamente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Fuente marcada para reprocesar exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Fuente no encontrada"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor al reprocesar la fuente"
            )
    })
    public ResponseEntity<String> reprocesarFuente(@PathVariable String fuente) {
        log.info("Recibiendo solicitud para reprocesar la fuente: {}", fuente);
        this.serviceEstatica.reprocesarFuente(fuente);
        return ResponseEntity.status(HttpStatus.OK).body("Fuente marcada para reprocesar exitosamente: " + fuente);
    }

}