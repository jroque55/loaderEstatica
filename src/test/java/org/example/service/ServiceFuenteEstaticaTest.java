package org.example.service;

import org.example.models.entities.fuente.Fuente;
import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.entities.fuenteEstatica.LectorCSV;
import org.example.models.entities.hecho.Hecho;
import org.example.models.repository.repoAgregador.IRepositoryAgregador;
import org.example.models.repository.repoFuenteEstatica.IRepositoryFuenteEstatica;
import org.example.utils.EstadoProcesado;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceFuenteEstaticaTest {

    @Mock
    private LectorCSV lectorCSV;
    @Mock
    private IRepositoryFuenteEstatica repositoryFuenteEstatica;

    @Mock
    private IRepositoryAgregador repositoryAgregador;

    @InjectMocks
    private ServiceFuenteEstatica service;

    // --- TEST 1: Lectura de un DataSet ---
    @Test
    void leerDataSet_DeberiaRetornarHechos_CuandoFuenteExisteYNoEstaProcesada(){
        String ruta = "datos.csv";
        FuenteEstatica fuenteMock = new FuenteEstatica(ruta);
        fuenteMock.setEstadoProcesado(EstadoProcesado.NO_PROCESADO);
        List<Hecho> hechosEsperados = List.of(new Hecho(), new Hecho());

        // Configuramos el Mock del lector para devolver la lista
        when(lectorCSV.obtencionHechos(ruta)).thenReturn(hechosEsperados);

        // WHEN
        List<Hecho> resultado = service.leerDataSet(fuenteMock);

        // THEN
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(EstadoProcesado.PROCESADO, fuenteMock.getEstadoProcesado());

        // Verificamos que se asignó el lector correcto (aunque esto es interno, es bueno saberlo)
        assertNotNull(fuenteMock.lector, "El lector debería haber sido asignado");

    }
    @Test
    void leerDataSet_DeberiaPropagarExcepcion_CuandoLectorFalla() {
        // GIVEN
        String ruta = "corrupto.csv";
        FuenteEstatica fuenteMock = new FuenteEstatica(ruta);

        // Simulamos que el lector falla con la RuntimeException que definiste
        when(lectorCSV.obtencionHechos(ruta))
                .thenThrow(new RuntimeException("Error en la lectura del archivo: " + ruta));

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.leerDataSet(fuenteMock);
        });

        assertEquals("Error en la lectura del archivo: " + ruta, exception.getMessage());

        assertEquals(EstadoProcesado.NO_PROCESADO, fuenteMock.getEstadoProcesado());
    }

    // --- TEST 3: Reprocesar Fuente ---
    @Test
    void reprocesarFuente_DeberiaCambiarEstadoANoProcesado() {
        // GIVEN
        String ruta = "archivo.csv";
        FuenteEstatica fuenteProcesada = new FuenteEstatica(ruta);
        fuenteProcesada.setEstadoProcesado(EstadoProcesado.PROCESADO);

        when(repositoryFuenteEstatica.findByRutaDataset(ruta)).thenReturn(fuenteProcesada);

        // WHEN
        service.reprocesarFuente(ruta);

        // THEN
        assertEquals(EstadoProcesado.NO_PROCESADO, fuenteProcesada.getEstadoProcesado());
        verify(repositoryFuenteEstatica).save(fuenteProcesada);
    }

    // --- TEST 4: Subir fuentes al agregador (Nuevas y Existentes) ---
    @Test
    void subirFuentesAlAgregador_DeberiaGuardarSoloFuentesNuevas() {
        // GIVEN
        FuenteEstatica fNueva = new FuenteEstatica("nuevo.csv");
        FuenteEstatica fExistente = new FuenteEstatica("viejo.csv");
        List<FuenteEstatica> fuentesNoLeidas = List.of(fNueva, fExistente);

        when(repositoryFuenteEstatica.findByEstadoProcesado(EstadoProcesado.NO_PROCESADO))
                .thenReturn(fuentesNoLeidas);

        // Simulamos que "viejo.csv" YA existe en el agregador
        when(repositoryAgregador.findByUrl("viejo.csv")).thenReturn(new Fuente());
        // Simulamos que "nuevo.csv" NO existe (devuelve null)
        when(repositoryAgregador.findByUrl("nuevo.csv")).thenReturn(null);

        // WHEN
        service.subirFuentesAlAgregador();

        // THEN
        // Verificamos que save() se llamó SOLO 1 VEZ (para la nueva)
        verify(repositoryAgregador, times(1)).save(any(Fuente.class));

        // Verificamos qué se guardó
        ArgumentCaptor<Fuente> captor = ArgumentCaptor.forClass(Fuente.class);
        verify(repositoryAgregador).save(captor.capture());
        assertEquals("nuevo.csv", captor.getValue().getUrl());
    }
}

