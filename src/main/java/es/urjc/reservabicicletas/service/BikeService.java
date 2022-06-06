package es.urjc.reservabicicletas.service;

import es.urjc.reservabicicletas.model.Bike;
import es.urjc.reservabicicletas.model.State;
import es.urjc.reservabicicletas.model.Station;
import es.urjc.reservabicicletas.repositories.BikeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BikeService {
    private ConcurrentMap<Long, Bike> bikes = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong(1);

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private StationService stationService;

    public BikeService() {
    }

    public void save(Bike bike) {
        if(bike.getId() == null || bike.getId() == 0) {
            System.out.println(bikes);
            long id = nextId.getAndIncrement();
            bike.setId(id);
            bikes.put(bike.getId(), bike);
            bikeRepository.save(bike);
        }
    }

    public void saveAll(List<Bike> bikes){
        for (Bike bike : bikes) {
            save(bike);
        }
    }

    public Date formatDate(int day, int month, int year) {
        try{
            String myDate = day+"/"+month+"/"+year;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(myDate);
            long millis = date.getTime();
            return new Date(millis);
        } catch (ParseException e){
            System.err.println("Cannot parse: "+day+"/"+month+"/"+year);
            throw new RuntimeException("Cannot parse: "+day+"/"+month+"/"+year);
        }
    }

    public Collection<Bike> findAll(){
        return bikes.values();
    }

    public Bike findById(Long id) {
        return bikes.get(id);
    }

    public void deleteById(Long id){
        bikes.remove(id);
    }

    public void update(Bike bike) {
        bikes.put(bike.getId(), bike);
        bikeRepository.save(bike);
    }

    public boolean isBikeInBase( Bike bike ){
        return bike.getEstado() == State.EN_BASE;
    }

    public boolean bookBike(Bike bike, Station station, Long userId){
        Logger logger = LoggerFactory.getLogger(BikeService.class);
        logger.info("En metodo");


        logger.info("In checking "+ station.isActive() + this.isBikeInBase(bike) + (bike.getStationId() == station));
        logger.info(bike.toString());
        logger.info(station.toString());
        if (station.isActive() && this.isBikeInBase(bike) &&
                (bike.getStationId() == station) &&
                stationService.hasBike(station, bike)) {

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8081/users/" + userId + "/deposit";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestJson = "{\"amount\":\"10\"}";
            HttpEntity<String> entity = new HttpEntity<>(requestJson,headers);

            logger.info("Before calling api");
            restTemplate.postForObject(url, entity, String.class);
            stationService.bookBike(station, bike);
            logger.info("Before checking");

            if (bike.getEstado() == State.EN_BASE){
                logger.info("Despues de if");
                bike.setEstado(State.RESERVADA);
                bike.setStationId(null);
                logger.info("return true");
                logger.info("After checking");
                return true;
            }
        } return false;

        /* catch (RestClientException ex) {
                throw ex; */

    }

    public boolean returnBike(Bike bike, Station station, long userId){
        if (station.isActive() && this.isBikeReservada(bike)
                && stationService.hasSpace(station)) {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8081/users/" + userId + "/deposit";

            restTemplate.delete(url);

            stationService.addBike(station, bike);
            System.out.println(station.getBikes());
            this.setBikeEnBase(bike);
            return true;
        } return false;
    }

    public void setBikeEnBase(Bike bike){
        bike.setEstado(State.EN_BASE);
    }

    public void setBaja (Bike bike){
        bike.setEstado(State.BAJA);
    }

    public boolean isBikeReservada( Bike bike ){
        return bike.getEstado() == State.RESERVADA;
    }
}
