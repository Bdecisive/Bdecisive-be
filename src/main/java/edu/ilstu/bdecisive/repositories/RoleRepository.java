package edu.ilstu.bdecisive.repositories;

import edu.ilstu.bdecisive.enums.AppRole;
import edu.ilstu.bdecisive.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);

}
