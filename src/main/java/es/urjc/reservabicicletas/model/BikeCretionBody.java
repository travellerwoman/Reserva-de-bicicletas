package es.urjc.reservabicicletas.model;

public class BikeCretionBody {

    private String numeroSerie;
    private String modelo;
    private State estado;
    private long stationId;

    public BikeCretionBody() {
    }

    public BikeCretionBody(String numeroSerie, String modelo, State estado, long stationId) {
        this.numeroSerie = numeroSerie;
        this.modelo = modelo;
        this.estado = estado;
        if (estado == State.EN_BASE) {
            this.stationId = stationId;
        }
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
