package es.urjc.reservabicicletas.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

public class BikeDTO {

    private Long id;

    private String numeroSerie;
    private String modelo;
    private State estado;
    private long stationId;

    public BikeDTO() {
    }

    public BikeDTO(Long id, String numeroSerie, String modelo, State estado, long stationId) {
        this.id = id;
        this.numeroSerie = numeroSerie;
        this.modelo = modelo;
        this.estado = estado;
        this.stationId = stationId;
    }

    public BikeDTO(Bike bike) {
        this.id = bike.getId();
        this.numeroSerie = bike.getNumeroSerie();
        this.modelo = bike.getModelo();
        this.estado = bike.getEstado();
        if (bike.getStationId() != null) {
            this.stationId = bike.getStationId().getId();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public State getEstado() {
        return estado;
    }

    public void setEstado(State estado) {
        this.estado = estado;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }
}
