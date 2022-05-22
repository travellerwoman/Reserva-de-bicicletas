package es.urjc.reservabicicletas.repositories;

import es.urjc.reservabicicletas.model.Bike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BikeRepository extends JpaRepository<Bike, Long>{
    Optional<Bike> findById(long id);
    List<Bike> findAllById(long id);
}
