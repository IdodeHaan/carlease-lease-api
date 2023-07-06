package sogeti.carleaseleaseapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogeti.carleaseleaseapi.model.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
