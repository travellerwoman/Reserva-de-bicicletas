package es.urjc.reservabicicletas.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.urjc.reservabicicletas.model.Bike;
import es.urjc.reservabicicletas.model.BikeBookingBody;
import es.urjc.reservabicicletas.model.Station;
import es.urjc.reservabicicletas.service.BikeService;
import es.urjc.reservabicicletas.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private StationService stationService;

    @JsonView(Bike.BikeResources.class)
    @GetMapping("/")
    @Operation(summary = "Devuelve todas las bicicletas guardadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Bikes accesed and returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bike.class))})})
    public Collection<Bike> getBikes(){
        return bikeService.findAll();
    }

    @JsonView(Bike.BikeResources.class)
    @GetMapping("/{id}")
    @Operation(summary = "Devuelve la bicicleta correspondiente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bike.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Bike not found",
                    content = @Content) })
    public ResponseEntity<Bike> getBikeById(@Parameter(description = "id of the bike to return") @PathVariable Long id){
        Bike bike = bikeService.findById(id);

        if (bike != null) {
            return ResponseEntity.ok(bike);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @JsonView(Bike.BikeResources.class)
    @PostMapping("/")
    @Operation(summary = "Crea una bici nueva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Creada correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bike.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid body supplied",
                    content = @Content)})
    public ResponseEntity<Bike> createBike(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Bike to be added without id parameter") @RequestBody Bike bike
    ){
        bikeService.save(bike);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(bike.getId()).toUri();
        return ResponseEntity.created(location).body(bike);
    }

    @JsonView(Bike.BikeResources.class)
    @DeleteMapping("/{id}")
    @Operation(summary = "Pasa el estado de la bici a 'BAJA'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bike.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Bike not found",
                    content = @Content) })
    public ResponseEntity<Bike> deleteBike(@Parameter(description = "Id of the bike to delete") @PathVariable Long id){
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
    @Operation(summary = "Remplaza los datos de la bici pasada por la id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the ID and replaced data correctly",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bike.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied or body not correct",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Bike not found",
                    content = @Content) })
    public ResponseEntity<Bike> replaceBike(
            @Parameter(description = "Id of the bike to replace data") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Bike data to replace old one") @RequestBody Bike newBike){
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
    @PostMapping(value = "/{bikeId}/rental", consumes = "application/json")
    @Operation(summary = "Reserva o devuelve la bicicleta correspondiente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bike.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Bike not found",
                    content = @Content) })
    public ResponseEntity<Bike> bookBike (
            @Parameter(description = "Id of the bike to book") @PathVariable Long bikeId,
            @Parameter(description = "Id of the station") @RequestBody BikeBookingBody bikeBookingBody
    ) {
        Station station = stationService.findById(bikeBookingBody.getStationId());
        Bike bike = bikeService.findById(bikeId);
        Logger logger = LoggerFactory.getLogger(BikeController.class);
        logger.info("Before checking");

        if (station != null && bike != null) {
            if (bikeService.bookBike(bike, station, bikeBookingBody.getUserId())){
                return ResponseEntity.ok(bike);
            }
        }
        return ResponseEntity.badRequest().build();
    }


    @JsonView(Bike.BikeResources.class)
    @DeleteMapping(value = "/{bikeId}/rental", consumes = "application/json")
    @Operation(summary = "Reserva o devuelve la bicicleta correspondiente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bike.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Bike not found",
                    content = @Content) })
    public ResponseEntity<Bike> returnBike (
            @PathVariable Long bikeId,
            @RequestBody BikeBookingBody bikeBookingBody
    ){
        Station station = stationService.findById(bikeBookingBody.getStationId());
        Bike bike = bikeService.findById(bikeId);

        if (station != null && bike != null){
            if (bikeService.returnBike(bike, station, bikeBookingBody.getUserId())){
                return ResponseEntity.ok(bike);
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
