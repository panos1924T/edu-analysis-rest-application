package gr.pants.pro.edu_analysis.repository;

import gr.pants.pro.edu_analysis.model.static_data.Firm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FirmRepository extends JpaRepository<Firm, Long> {

    List<Firm> findByOrderByNameAsc();
}
