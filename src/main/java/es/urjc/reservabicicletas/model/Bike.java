package es.urjc.reservabicicletas.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Bike {

    public interface BikeResources{}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonView(BikeResources.class)
    private Long id;

    @JsonView(BikeResources.class)
    private String numeroSerie;

    @JsonView(BikeResources.class)
    private String modelo;

    @JsonView(BikeResources.class)
    private Date fechaAlta;

    @JsonView(BikeResources.class)
    private State estado;

    @ManyToOne
    @JoinColumn(name="stationId")
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

    public Bike(BikeDTO bikeDTO, Date fechaAlta) {
        this.id = bikeDTO.getId();
        this.numeroSerie = bikeDTO.getNumeroSerie();
        this.modelo = bikeDTO.getModelo();
        this.estado = bikeDTO.getEstado();
        this.fechaAlta = fechaAlta;
    }

    public Bike(BikeDTO bikeDTO, Date fechaAlta, Station stationId) {
        this.id = bikeDTO.getId();
        this.numeroSerie = bikeDTO.getNumeroSerie();
        this.modelo = bikeDTO.getModelo();
        this.estado = bikeDTO.getEstado();
        this.fechaAlta = fechaAlta;
        this.stationId = stationId;
    }

    public Bike(BikeCretionBody bikeDTO, Date fechaAlta) {
        this.numeroSerie = bikeDTO.getNumeroSerie();
        this.modelo = bikeDTO.getModelo();
        this.estado = bikeDTO.getEstado();
        this.fechaAlta = fechaAlta;
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

    @Override
    public boolean equals(Object o) {
        Bike bike = (Bike) o;
        return Objects.equals(id, bike.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
