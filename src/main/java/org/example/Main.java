package org.example;

import org.example.controller.ControllerFuenteEstatica;
import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.repository.repoAgregador.IRepositoryAgregador;
import org.example.models.repository.repoFuenteEstatica.IRepositoryFuenteEstatica;
import org.example.service.ServiceFuenteEstatica;
import org.example.utils.EstadoProcesado;
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

//        FuenteEstatica f1 = repo.findByRutaDataset("hecho_para_fabri.csv");
//        f1.setEstadoProcesado(EstadoProcesado.PROCESADO);
//        repo.save(f1);
//        FuenteEstatica f2 = repo.findByRutaDataset("hechos_para_juan.csv");
//        f2.setEstadoProcesado(EstadoProcesado.PROCESADO);
//        repo.save(f2);
//        FuenteEstatica f3 = repo.findByRutaDataset("hechos_para_yeri.csv");
//        f3.setEstadoProcesado(EstadoProcesado.PROCESADO);
//        repo.save(f3);
//        FuenteEstatica f4 = repo.findByRutaDataset("hechos_para_ruka.csv");
//        f4.setEstadoProcesado(EstadoProcesado.PROCESADO);
//        repo.save(f4);
//        FuenteEstatica f5 = repo.findByRutaDataset("hechos_para_manu.csv");
//        f5.setEstadoProcesado(EstadoProcesado.PROCESADO);
//        repo.save(f5);
//        FuenteEstatica f6 = repo.findByRutaDataset("hechos_sobre_kpopers.csv");
//        f6.setEstadoProcesado(EstadoProcesado.PROCESADO);
//        repo.save(f6);

    }
}
