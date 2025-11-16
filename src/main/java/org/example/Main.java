package org.example;

import org.example.controller.ControllerFuenteEstatica;
import org.example.models.repository.repoAgregador.IRepositoryAgregador;
import org.example.models.repository.repoFuenteEstatica.IRepositoryFuenteEstatica;
import org.example.service.ServiceFuenteEstatica;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EntityScan(basePackages = "org.example.models")
public class Main {
    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class, args);

        IRepositoryFuenteEstatica repo = context.getBean(IRepositoryFuenteEstatica.class);
        ControllerFuenteEstatica control = context.getBean(ControllerFuenteEstatica.class);
        IRepositoryAgregador repoAgregador = context.getBean(IRepositoryAgregador.class);
        ServiceFuenteEstatica serv = context.getBean(ServiceFuenteEstatica.class);

        serv.subirFuentesAlAgregador();
    }
}
