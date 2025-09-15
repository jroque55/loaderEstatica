package org.example.models.entities.fuenteEstatica;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import org.example.models.entities.hecho.*;
import static java.lang.Float.parseFloat;

import java.time.format.DateTimeFormatter;
import com.opencsv.CSVReader;
import java.io.FileReader;

public class LectorCSV implements ILector {

//    @Override
//    public List<Hecho> obtencionHechos(String ruta) {
//        String partes[];
//        Hecho hecho = null;
//        Hecho repetido = null;
//        List<Hecho> hechos = new ArrayList<>();
//
//        try {
//            File archivo = new File(ruta);
//            Scanner lector = new Scanner(archivo);
//            if (lector.hasNextLine()) lector.nextLine();
//            while (lector.hasNextLine()) { // Revisa si hay una línea
//                partes = lector.nextLine().split(","); // Obtengo las partes del archivo
//
//                // no copia encabezado
//                //if (partes[0].trim().equalsIgnoreCase("título")) continue;
//
//                // tener en cuenta que en el futuro podrían ser más campos
//                // TODO: Adaptar si se agregan más columnas
//
//                repetido = encontrarRepetido(partes[0], hechos); // Buscamos repetido
//
//                if (repetido != null) {
//                    reemplazarHechoRepetido(repetido, partes);
//                } else {
//                    hecho = crearHecho(hechos,partes);
//                    hechos.add(hecho);
//                }
//
//            }
//            lector.close();
//        } catch (FileNotFoundException e) {
//            System.err.println("Error: No se pudo encontrar el archivo - " + e.getMessage());
//        }
//
//        return hechos;
//    }

    @Override
    public List<Hecho> obtencionHechos(String ruta) {
        List<Hecho> hechos = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try (CSVReader reader = new CSVReader(new FileReader(ruta))) {
            String[] line;
            reader.readNext(); // saltar encabezado
            while ((line = reader.readNext()) != null) {
                // line[0] -> titulo
                // line[1] -> descripcion (comillas manejadas)
                // line[2] -> categoria
                // line[3] -> latitud
                // line[4] -> longitud
                // line[5] -> fecha
                float lat = Float.parseFloat(line[3].trim());
                float lon = Float.parseFloat(line[4].trim());
                LocalDate fecha = LocalDate.parse(line[5].trim(), formatter);
                Categoria categoria = encontrarCategoriaRepetido(hechos, line[2].trim());
                Ubicacion ubicacion = new Ubicacion(lat, lon);
                Hecho hecho = new Hecho(line[0].trim(), line[1].trim(), categoria, ubicacion, fecha);
                hechos.add(hecho);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hechos;
    }
    //TODO: Pasar responsabilidad a el repo de hechos?
    private Hecho encontrarRepetido(String titulo, List<Hecho> hechos) {
        // para cada elemento de hechos ":" es un for each
        for (Hecho h : hechos) {
            if (h.getTitulo().equalsIgnoreCase(titulo)) {
                return h;
            }
        }
        return null;
    }

    private Categoria encontrarCategoriaRepetido(List<Hecho> hechos, String cate) {
        for (Hecho h: hechos) {
            if (h.getCategoria().getNombre().equals(cate)) {
                return h.getCategoria();
            }
        }
        return new Categoria(cate);
    }

    private void reemplazarHechoRepetido(Hecho hecho, String[] datos) {
        hecho.setDescripcion(datos[1]);
        hecho.setCategoria(datos[2]);
        hecho.setUbicacion(datos[3], datos[4]);
        hecho.setFecha(LocalDate.parse(datos[5]));
    }

    //TODO: pasar responsabilidad a hechos
    private Hecho crearHecho(List<Hecho> hechos, String[] datos) {
        Categoria categoria = encontrarCategoriaRepetido(hechos, datos[2]); //, aqui deberiamos buscar la categoria o crearla en caso de no existir
        Ubicacion ubicacion = new Ubicacion ((parseFloat(datos[3])), parseFloat(datos[4]));
        LocalDate fecha = LocalDate.parse(datos[5]);
        return new Hecho(datos[0], datos[1], categoria,ubicacion,fecha);
    }
}