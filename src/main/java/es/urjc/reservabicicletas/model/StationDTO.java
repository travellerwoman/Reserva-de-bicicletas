package es.urjc.reservabicicletas.model;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.Date;

public class StationDTO {

    private Long id;

    private String numeroSerie;
    private float latitud;
    private float longitud;
    private Capacity capacidad;
    private boolean active;

    public StationDTO() {
    }

    public StationDTO(Long id, String numeroSerie, float latitud, float longitud, Capacity capacidad, boolean active) {
        this.id = id;
        this.numeroSerie = numeroSerie;
        this.latitud = latitud;
        this.longitud = longitud;
        this.capacidad = capacidad;
        this.active = active;
    }

    public StationDTO(Station station) {
        this.id = station.getId();
        this.numeroSerie = station.getNumeroSerie();
        this.latitud = station.getLatitud();
        this.longitud = station.getLongitud();
        this.capacidad = station.getCapacidad();
        this.active = station.isActive();
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

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public Capacity getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Capacity capacidad) {
        this.capacidad = capacidad;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
