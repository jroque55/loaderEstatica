package org.example.models.entities.fuenteEstatica;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import org.example.models.dtos.FuenteDto;
import org.example.models.entities.hecho.*;
import java.time.format.DateTimeFormatter;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;
@Component
public class LectorCSV implements ILector {

    @Value("${app.urlFile}")
    private String urlFile;
    private static final Logger logger = LoggerFactory.getLogger(LectorCSV.class);

    @Override
    public List<Hecho> obtencionHechos(String ruta) {
        List<Hecho> hechos = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        FuenteDto fuente = new FuenteDto(ruta, TipoFuente.ESTATICA);
        fuente.obtenerNombre();
        logger.info(fuente.getNombre());
        try (CSVReader reader = new CSVReader(new FileReader(this.urlFile + '/'+ ruta))) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                float lat = Float.parseFloat(line[3].trim());
                float lon = Float.parseFloat(line[4].trim());
                LocalDate fecha = LocalDate.parse(line[5].trim(), formatter);
                Categoria categoria = encontrarCategoriaRepetido(hechos, line[2].trim());
                Ubicacion ubicacion = new Ubicacion(lat, lon);
                Hecho hecho = new Hecho(line[0].trim(), line[1].trim(), categoria, ubicacion, fecha,fuente);
                hechos.add(hecho);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hechos;
    }

    private Categoria encontrarCategoriaRepetido(List<Hecho> hechos, String cate) {
        for (Hecho h: hechos) {
            if (h.getCategoria().getNombre().equals(cate)) {
                return h.getCategoria();
            }
        }
        return new Categoria(cate);
    }

}