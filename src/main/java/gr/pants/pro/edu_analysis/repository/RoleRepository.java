package gr.pants.pro.edu_analysis.repository;

import gr.pants.pro.edu_analysis.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByOrderByNameAsc();
}
