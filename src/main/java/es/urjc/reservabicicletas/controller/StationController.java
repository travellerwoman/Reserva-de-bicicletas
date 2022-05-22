package es.urjc.reservabicicletas.controller;

import es.urjc.reservabicicletas.model.Station;
import es.urjc.reservabicicletas.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/stations")
public class StationController {

    @Autowired
    private StationService stationService = new StationService();

    @GetMapping("/")
    public Collection<Station> getStations(){
        return stationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable Long id){
        Station bike = stationService.findById(id);

        if (bike != null) {
            return ResponseEntity.ok(bike);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Station> createStation(@RequestBody Station bike){
        stationService.save(bike);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(bike.getId()).toUri();
        return ResponseEntity.created(location).body(bike);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Station> deleteStation(@PathVariable Long id){
        Station bike = stationService.findById(id);

        if (bike != null){
            stationService.deleteById(id);
            return ResponseEntity.ok(bike);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Station> replaceStation(@PathVariable Long id, @RequestBody Station newStation){
        Station bike = stationService.findById(id);

        if (bike != null){
            // Guardamos solo la id de la bike vieja, no nos interesa la nueva
            newStation.setId(id);
            stationService.update(newStation);
            return ResponseEntity.ok(newStation);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

}
