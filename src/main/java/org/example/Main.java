package org.example;

import org.example.controller.ControllerFuenteEstatica;
import org.example.models.repository.RepositoryFuenteEstatica;
import org.example.service.ServiceFuenteEstatica;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.example.utils.BDUtils;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.EntityManager;

@SpringBootApplication
@EntityScan(basePackages = "org.example.models.Schemas")
public class Main {
    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class, args);
        EntityManager em = BDUtils.getEntityManager();
        BDUtils.comenzarTransaccion(em);

        RepositoryFuenteEstatica repo = context.getBean(RepositoryFuenteEstatica.class);
        ControllerFuenteEstatica control = context.getBean(ControllerFuenteEstatica.class);
        ServiceFuenteEstatica serv = context.getBean(ServiceFuenteEstatica.class);
        BDUtils.commit(em);
    }
}
