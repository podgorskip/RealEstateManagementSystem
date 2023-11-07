package podgorskip.managementSystem.jpa.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import podgorskip.managementSystem.jpa.entities.Estate;

import java.util.List;

@Repository
public interface EstatesRepository extends JpaRepository<Estate, Integer> {
    List<Estate> findAll(Specification<Estate> specification);
}
