package org.example;

import org.example.controller.ControllerHecho;
import org.example.models.entities.hecho.Hecho;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.example.utils.BDUtils;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootApplication
@EntityScan(basePackages = "org.example.models.Schemas")
public class Main {
    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class, args);
        EntityManager em = BDUtils.getEntityManager();
        BDUtils.comenzarTransaccion(em);

        ControllerHecho control = context.getBean(ControllerHecho.class);
        BDUtils.commit(em);
    }
}
