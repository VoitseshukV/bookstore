package com.bookstore.core.repository.role;

import com.bookstore.core.model.Role;
import com.bookstore.core.security.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role getByName(RoleName name);
}
