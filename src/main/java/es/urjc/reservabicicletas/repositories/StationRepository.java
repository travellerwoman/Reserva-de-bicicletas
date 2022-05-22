package es.urjc.reservabicicletas.repositories;

import es.urjc.reservabicicletas.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StationRepository extends JpaRepository<Station, Long>{
    Optional<Station> findById(long id);
    List<Station> findAllById(long id);
}
