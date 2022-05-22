package es.urjc.reservabicicletas.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Bike {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String numeroSerie;
    private String modelo;
    private Date fechaAlta;
    private State estado;

    @ManyToOne
    @JoinColumn(name="id")
    private Station stationId;

    public Bike() {
    }

    public Bike(String numeroSerie, String modelo, Date fechaAlta, State estado) {
        this.numeroSerie = numeroSerie;
        this.modelo = modelo;
        this.fechaAlta = fechaAlta;
        this.estado = estado;
    }

    public Bike(String numeroSerie, String modelo, Date fechaAlta, String estado) {
        this.numeroSerie = numeroSerie;
        this.modelo = modelo;
        this.fechaAlta = fechaAlta;
        this.estado = getStateFromString(estado);
    }

    public Bike(String numeroSerie, String modelo, Date fechaAlta, State estado, Station stationId) {
        this.numeroSerie = numeroSerie;
        this.modelo = modelo;
        this.fechaAlta = fechaAlta;
        this.estado = estado;
        this.stationId = stationId;
    }

    public Bike(String numeroSerie, String modelo, Date fechaAlta, String estado, Station stationId) {
        this.numeroSerie = numeroSerie;
        this.modelo = modelo;
        this.fechaAlta = fechaAlta;
        this.estado = getStateFromString(estado);
        this.stationId = stationId;
    }

    private State getStateFromString(String estado) {
        for (State bikeState : State.values()) {
            if (estado.equalsIgnoreCase(bikeState.toString())) {
                return bikeState;
            }
        }
        return null;
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

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public State getEstado() {
        return estado;
    }

    public void setEstado(State estado) {
        this.estado = estado;
    }

    public Station getStationId() {
        return stationId;
    }

    public void setStationId(Station stationId) {
        this.stationId = stationId;
    }

    @Override
    public String toString() {
        return "Bike{" +
                "id=" + id +
                ", numeroSerie='" + numeroSerie + '\'' +
                ", modelo='" + modelo + '\'' +
                ", fechaAlta=" + fechaAlta +
                ", estado=" + estado +
                '}';
    }
}
