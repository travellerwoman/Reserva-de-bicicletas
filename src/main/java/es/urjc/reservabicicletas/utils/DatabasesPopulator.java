package es.urjc.reservabicicletas.utils;


import es.urjc.reservabicicletas.model.Bike;
import es.urjc.reservabicicletas.model.State;
import es.urjc.reservabicicletas.model.Station;
import es.urjc.reservabicicletas.service.BikeService;
import es.urjc.reservabicicletas.service.StationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("local")
public class DatabasesPopulator {

    private Logger log = LoggerFactory.getLogger(DatabasesPopulator.class);

    @Autowired
    private BikeService bikeService = new BikeService();

    @Autowired
    private StationService stationService = new StationService();

    @PostConstruct
    public void populateDB() {

        Station station1 = new Station("8427",15, 18, "Cinco", true, bikeService.formatDate(1022, 2, 1));
        Station station2 = new Station("23423",17, 25, "Diez", true, bikeService.formatDate(1022, 4, 19));
        Station station3 = new Station("2",18, 85, "Cinco", true, bikeService.formatDate(1022, 3, 1));
        Station station4 = new Station("54256",55, 28, "Diez", true, bikeService.formatDate(1022, 4, 19));
        // numeroSerie, latitud, longitud, capacidad, activo, fechaInstalacion

        Bike bike1 = new Bike("numeroSerie1","model 1", new Date(1020, 5, 6), "En_base", station1);
        Bike bike2 = new Bike(  "numeroSerie1","model 1", new Date(1020, 5, 6), "En_base", station1);
        Bike bike3 = new Bike( "numeroSerie1","model 1", new Date(1020, 5, 6), "En_base", station1);
        Bike bike4 = new Bike(  "numeroSerie1","model 1", new Date(1021, 5, 6), "En_base", station1);
        Bike bike5 = new Bike( "numeroSerie1","model 2", new Date(1021, 5, 6), "En_base", station2);
        Bike bike6 = new Bike( "numeroSerie1","model 1", new Date(1020, 5, 6), "En_base", station2);
        Bike bike7 = new Bike(  "numeroSerie1","model 1", new Date(1021, 5, 6), "En_base", station2);
        Bike bike8 = new Bike( "numeroSerie1","model 1", new Date(1020, 5, 6), State.EN_BASE);
        Bike bike9 = new Bike( "numeroSerie1","model 1", new Date(1020, 5, 6), "Sin_base");
        //numeroSerie, modelo, fecha alta, estado

        List<Bike> bikeList = new ArrayList<>(Arrays.asList(bike1, bike2, bike3, bike4));
        station1.setBikes(bikeList);
        List<Bike> bikeList2 = new ArrayList<>(Arrays.asList(bike5, bike6, bike7));
        station2.setBikes(bikeList2);

        stationService.saveAll(Arrays.asList(station1, station2, station3, station4));
        bikeService.saveAll(Arrays.asList( bike1, bike2, bike3, bike4, bike5, bike6, bike7, bike8, bike9));

    }
}
