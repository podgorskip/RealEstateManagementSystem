package podgorskip.managementSystem.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import podgorskip.managementSystem.jpa.entities.EstateOffer;

@Repository
public interface EstateOfferRepository extends JpaRepository<EstateOffer, Integer> {
}
