package io.everyonecodes.anber.repository;


import io.everyonecodes.anber.data.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	public Role findByRole(String role);
}
