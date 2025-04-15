package org.makarovevg.mysbs.mysbs_app.service;

import org.makarovevg.mysbs.mysbs_app.models.Role;
import java.util.Optional;


public interface RoleService {

    void create(Role role);

    void delete(long id);

    Optional<Role> findByRoleName(String roleName);
}
