package es.urjc.reservabicicletas.service;

import es.urjc.reservabicicletas.model.Bike;
import es.urjc.reservabicicletas.model.State;
import es.urjc.reservabicicletas.repositories.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BikeService {
    private ConcurrentMap<Long, Bike> bikes = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong(1);

    @Autowired
    private BikeRepository bikeRepository;

    public BikeService() {
    }

    public void save(Bike bike) {
        if(bike.getId() == null || bike.getId() == 0) {
            System.out.println(bikes);
            long id = nextId.getAndIncrement();
            bike.setId(id);
            bikes.put(bike.getId(), bike);
            bikeRepository.save(bike);
        }
    }

    public void saveAll(List<Bike> bikes){
        for (Bike bike : bikes) {
            save(bike);
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
            System.err.println("Cannot parse: "+day+"/"+month+"/"+year);
            throw new RuntimeException("Cannot parse: "+day+"/"+month+"/"+year);
        }
    }

    public Collection<Bike> findAll(){
        return bikes.values();
    }

    public Bike findById(Long id) {
        return bikes.get(id);
    }

    public void deleteById(Long id){
        bikes.remove(id);
    }

    public void update(Bike bike) {
        bikes.put(bike.getId(), bike);
        bikeRepository.save(bike);
    }

    public boolean isBikeInBase( Bike bike ){
        return bike.getEstado() == State.EN_BASE;
    }

    public boolean bookBike(Bike bike){
        if (bike.getEstado() == State.EN_BASE){
            bike.setEstado(State.RESERVADA);
            return true;
        } return false;
    }

    public void setBikeEnBase(Bike bike){
        bike.setEstado(State.EN_BASE);
    }
}
