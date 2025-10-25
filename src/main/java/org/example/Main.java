package org.example;

import org.example.controller.ControllerHecho;
import org.example.models.entities.hecho.Hecho;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.example.utils.BDUtils;
import javax.persistence.EntityManager;
import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class, args);
        EntityManager em = BDUtils.getEntityManager();
        BDUtils.comenzarTransaccion(em);


        ControllerHecho control = context.getBean(ControllerHecho.class);

        // acá ya va a tener inyectado urlFile desde application.properties
        List<Hecho> hechos = control.getHechosRuta("/desastres_sanitarios_contaminacion_argentina.csv");

        for (Hecho hecho : hechos) {
            System.out.println(hecho.getTitulo());
        }

    }
}
