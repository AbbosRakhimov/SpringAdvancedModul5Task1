package uz.pdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.pdp.entity.Role;
import uz.pdp.entity.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Role findByRoleName(RoleName roleName);
}
