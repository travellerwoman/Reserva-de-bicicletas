package es.urjc.reservabicicletas.model;

public class StationCreationBody {

    private String numeroSerie;
    private float latitud;
    private float longitud;
    private Capacity capacidad;
    private boolean active;

    public StationCreationBody() {
    }

    public StationCreationBody(String numeroSerie, float latitud, float longitud, Capacity capacidad, boolean active) {
        this.numeroSerie = numeroSerie;
        this.latitud = latitud;
        this.longitud = longitud;
        this.capacidad = capacidad;
        this.active = active;
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
