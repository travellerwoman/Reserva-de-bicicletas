package es.urjc.reservabicicletas.controller;

import com.fasterxml.jackson.annotation.JsonView;
import es.urjc.reservabicicletas.model.Bike;
import es.urjc.reservabicicletas.model.Station;
import es.urjc.reservabicicletas.model.StationCreationBody;
import es.urjc.reservabicicletas.model.StationDTO;
import es.urjc.reservabicicletas.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

//    @JsonView(Station.StationResources.class)
    @Operation(summary = "Devuelve todas las bicicletas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StationDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content)})
    @GetMapping("/")
    public Collection<StationDTO> getStations(){
        return stationService.findAll();
    }

//    @JsonView(Station.StationResources.class)
    @GetMapping("/{id}")
    @Operation(summary = "Devuelve la bici con el identificador correspondiente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StationDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Bike not found",
                    content = @Content) })
    public ResponseEntity<StationDTO> getStationById(@Parameter(description = "Id of the station to return") @PathVariable Long id){
        Station stationServiceById = stationService.findById(id);

        if (stationServiceById != null) {
            return ResponseEntity.ok(new StationDTO(stationServiceById));
        } else{
            return ResponseEntity.notFound().build();
        }
    }

//    @JsonView(Station.StationResources.class)
    @PostMapping("/")
    @Operation(summary = "Crea una nueva estacion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StationDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content)})
    public ResponseEntity<StationDTO> createStation(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "station to create without id") @RequestBody StationCreationBody station
    ){
        StationDTO stationDTO = new StationDTO(stationService.save(station));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(stationDTO.getId()).toUri();
        return ResponseEntity.created(location).body(stationDTO);
    }

//    @JsonView(Station.StationResources.class)
    @DeleteMapping("/{id}")
    @Operation(summary = "Borrar estacion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StationDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Station not found",
                    content = @Content) })
    public ResponseEntity<StationDTO> deleteStation(@Parameter(description = "Id of the station to delete") @PathVariable Long id){
        Station station = stationService.findById(id);

        if (station != null){
            stationService.deleteById(id);
            return ResponseEntity.ok(new StationDTO(station));
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Reemplaza una estacion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the ID",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StationDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Station not found",
                    content = @Content) })
    public ResponseEntity<StationDTO> replaceStation(
            @Parameter(description = "Id of the station to replace") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data of the station to replace, without id") @RequestBody Station newStation){
        Station bike = stationService.findById(id);

        if (bike != null){
            // Guardamos solo la id de la bike vieja, no nos interesa la nueva
            newStation.setId(id);
            stationService.update(newStation);
            return ResponseEntity.ok(new StationDTO(newStation));
        } else{
            return ResponseEntity.notFound().build();
        }
    }

}
