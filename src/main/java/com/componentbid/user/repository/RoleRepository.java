package com.componentbid.user.repository;

import com.componentbid.user.entity.Role;
import com.componentbid.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.lang.ScopedValue;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(UserRole role);
}
