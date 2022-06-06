package es.urjc.reservabicicletas.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Station {

    public interface StationResources{}

    @Id
    @Column(name = "id", nullable = false)
    @JsonView(StationResources.class)
    private Long id;

    @JsonView(StationResources.class)
    private String numeroSerie;

    @JsonView(StationResources.class)
    private float latitud;

    @JsonView(StationResources.class)
    private float longitud;

    @JsonView(StationResources.class)
    private Capacity capacidad;

    @JsonView(StationResources.class)
    private boolean active;

    @JsonView(StationResources.class)
    private Date fechaInstalacion;

    @OneToMany(mappedBy="stationId", cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Bike> bikes;

    public Station() {
    }

    public Station(String numeroSerie, float latitud, float longitud, Capacity capacidad, boolean active, Date fechaInstalacion) {
        this.numeroSerie = numeroSerie;
        this.latitud = latitud;
        this.longitud = longitud;
        this.capacidad = capacidad;
        this.active = active;
        this.fechaInstalacion = fechaInstalacion;
    }

    public Station(String numeroSerie, float latitud, float longitud, String capacidad, boolean active, Date fechaInstalacion) {
        this.numeroSerie = numeroSerie;
        this.latitud = latitud;
        this.longitud = longitud;
        this.capacidad = getCapacityFromString(capacidad);
        this.active = active;
        this.fechaInstalacion = fechaInstalacion;
    }

    public Station(String numeroSerie, float latitud, float longitud, int capacidad, boolean active, Date fechaInstalacion) {
        this.numeroSerie = numeroSerie;
        this.latitud = latitud;
        this.longitud = longitud;
        this.capacidad = getCapacityFromInt(capacidad);
        this.active = active;
        this.fechaInstalacion = fechaInstalacion;
    }

    private Capacity getCapacityFromInt(int capacidad) {
        if (capacidad == 5){
            return Capacity.CINCO;
        } else {
            return Capacity.DIEZ;
        }
    }

    private Capacity getCapacityFromString(String capacidad) {
        for (Capacity stationCapacity : Capacity.values()) {
            if (capacidad.equalsIgnoreCase(stationCapacity.toString())) {
                return stationCapacity;
            }
        }
        return null;
    }

    public int getIntFromCapacity(){
        if (capacidad == Capacity.CINCO){
            return 5;
        } else {
            return 10;
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

    public Date getFechaInstalacion() {
        return fechaInstalacion;
    }

    public void setFechaInstalacion(Date fechaInstalacion) {
        this.fechaInstalacion = fechaInstalacion;
    }

    public List<Bike> getBikes() {
        return bikes;
    }

    public void setBikes(List<Bike> bikes) {
        this.bikes = bikes;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", numeroSerie='" + numeroSerie + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", capacidad=" + capacidad +
                ", active=" + active +
                ", fechaInstalacion=" + fechaInstalacion +
                ", bikes=" + bikes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
