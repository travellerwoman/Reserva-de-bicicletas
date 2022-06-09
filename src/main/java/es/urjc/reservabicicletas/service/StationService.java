package es.urjc.reservabicicletas.service;

import es.urjc.reservabicicletas.model.Bike;
import es.urjc.reservabicicletas.model.Station;
import es.urjc.reservabicicletas.model.StationCreationBody;
import es.urjc.reservabicicletas.model.StationDTO;
import es.urjc.reservabicicletas.repositories.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StationService {
    private ConcurrentMap<Long, Station> stations = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong(1);

    @Autowired
    private StationRepository stationRepository;

    public StationService() {
    }

    public void save(Station station) {
        if(station.getId() == null || station.getId() == 0) {
            long id = nextId.getAndIncrement();
            station.setId(id);
            stations.put(station.getId(), station);
            stationRepository.save(station);
        }
    }

    public Station save(StationCreationBody station) {
        nextId.getAndIncrement();
        Station newStation = new Station(station, new Date());
        Station stationSaved = stationRepository.save(newStation);
        stations.put(stationSaved.getId(), stationSaved);
        return newStation;
    }

    public void saveAll(List<Station> stations){
        for (Station station : stations) {
            save(station);
        }
    }

    public Date formatDate(int day, int month, int year) {
        try{
            String myDate = day+"/"+month+"/"+year;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(myDate);
            long millis = date.getTime();
            return new Date(millis);
        } catch (ParseException e){
            throw new RuntimeException("Cannot parse: "+day+"/"+month+"/"+year);
        }
    }

    public Collection<StationDTO> findAll(){
        Collection<StationDTO> collection = new HashSet<>();
        for (Station station :
                stations.values()) {
            collection.add(new StationDTO(station));
        }
        return collection;
    }

    public Station findById(Long id) {
        return stations.get(id);
    }

    public void deleteById(Long id){
        stations.remove(id);
    }

    public void update(Station station) {
        stations.put(station.getId(), station);
        stationRepository.save(station);
    }

    public Station bookBike(Station station, Bike bike){
        if (hasBike(station, bike)){
            station.getBikes().remove(bike);
            return station;
        } return null;
    }

    public boolean hasBike(Station station, Bike bike){
        return station.getBikes().contains(bike);
    }

    public Station addBike (Station station, Bike bike){
        station.getBikes().add(bike);
        return station;
    }

    public boolean hasSpace (Station station){
        int totalSpace = station.getIntFromCapacity();
        int occupiedSpace = station.getBikes().size();
        return (totalSpace-occupiedSpace) > 0;
    }
}
