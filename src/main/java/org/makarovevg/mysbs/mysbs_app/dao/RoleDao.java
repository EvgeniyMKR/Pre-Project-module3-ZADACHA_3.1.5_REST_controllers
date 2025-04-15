package org.makarovevg.mysbs.mysbs_app.dao;


import org.makarovevg.mysbs.mysbs_app.models.Role;

import java.util.Optional;


public interface RoleDao {

    void create(Role role);

    void delete(long id);

    Optional<Role> findByRoleName(String roleName);

}
