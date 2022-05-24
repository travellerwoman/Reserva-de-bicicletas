package es.urjc.reservabicicletas.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.urjc.reservabicicletas.model.Bike;
import es.urjc.reservabicicletas.model.Station;
import es.urjc.reservabicicletas.service.BikeService;
import es.urjc.reservabicicletas.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/bikes")
public class BikeController {

    @Autowired
    private BikeService bikeService;


    @Autowired
    StationService stationService;

    @JsonView(Bike.BikeResources.class)
    @GetMapping("/")
    public Collection<Bike> getBikes(){
        return bikeService.findAll();
    }

    @JsonView(Bike.BikeResources.class)
    @GetMapping("/{id}")
    public ResponseEntity<Bike> getBikeById(@PathVariable Long id){
        Bike bike = bikeService.findById(id);

        if (bike != null) {
            return ResponseEntity.ok(bike);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @JsonView(Bike.BikeResources.class)
    @PostMapping("/")
    public ResponseEntity<Bike> createBike(@RequestBody Bike bike){
        bikeService.save(bike);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(bike.getId()).toUri();
        return ResponseEntity.created(location).body(bike);
    }

    @JsonView(Bike.BikeResources.class)
    @DeleteMapping("/{id}")
    public ResponseEntity<Bike> deleteBike(@PathVariable Long id){
        Bike bike = bikeService.findById(id);

        if (bike != null){
//            bikeService.deleteById(id);
            bikeService.setBaja(bike);
            return ResponseEntity.ok(bike);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @JsonView(Bike.BikeResources.class)
    @PutMapping("/{id}")
    public ResponseEntity<Bike> replaceBike(@PathVariable Long id, @RequestBody Bike newBike){
        Bike bike = bikeService.findById(id);

        if (bike != null){
            // Guardamos solo la id de la bike vieja, no nos interesa la nueva
            newBike.setId(id);
            bikeService.update(newBike);
            return ResponseEntity.ok(newBike);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @JsonView(Bike.BikeResources.class)
    @PostMapping(value = "/{bikeId}/stations/{stationId}/users/{userId}", consumes = "text/plain")
    public ResponseEntity<Bike> bookBike (
            @PathVariable Long bikeId,
            @PathVariable Long stationId,
            @PathVariable Long userId,
            @RequestBody String paymentAmount
    ) {
        Station station = stationService.findById(stationId);
        Bike bike = bikeService.findById(bikeId);

        if (station != null && bike != null) {
            if (station.isActive() && bikeService.isBikeInBase(bike) &&
                    (bike.getStationId() == station) &&
                    stationService.hasBike(station, bike)) {

                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8081/users/" + userId + "/bikes";

                try {
                    ResponseEntity<String> userResponse = restTemplate.postForEntity(url, paymentAmount, String.class);

                    stationService.bookBike(station, bike);

                    if (bikeService.bookBike(bike)) {
                        return ResponseEntity.ok(bike);
                    }
                } catch (RestClientException ex) {
                    ex.printStackTrace();
                    return ResponseEntity.notFound().build();
                }
            }
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping(value = "/{bikeId}/stations/{stationId}/users/{userId}", consumes = {})
    public ResponseEntity<Bike> returnBike (
            @PathVariable Long bikeId,
            @PathVariable Long stationId,
            @PathVariable Long userId
    ){
        StationService stationService = new StationService();
        Station station = stationService.findById(stationId);
        Bike bike = bikeService.findById(bikeId);
        /*User user = new UserService().findById(userId);

        if (station != null && bike != null && user != null){
            if (station.isActive() && bikeService.isBikeInBase(bike) &&
                    (bike.getStationId() == station) &&
                    stationService.hasBike(station, bike)){
                UserController userController = new UserController();
                userController.payMoneyBack(userId);

                stationService.addBike(station, bike);

                bikeService.setBikeEnBase(bike);

                return ResponseEntity.ok(bike);
            }
        }*/
        return ResponseEntity.badRequest().build();
    }
}
