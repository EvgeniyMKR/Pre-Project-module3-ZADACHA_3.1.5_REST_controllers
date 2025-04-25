package org.makarovevg.mysbs.mysbs_app.service;

import org.makarovevg.mysbs.mysbs_app.dao.RoleDao;
import org.makarovevg.mysbs.mysbs_app.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class RoleServiceImpl implements RoleService {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional
    public void create(Role role) {
        try {
            roleDao.create(role);
            log.info("Роль успешно создана: " + role.getRoleName());
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        roleDao.delete(id);
        log.info("Роль успешно удалена: " + id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findByRoleName(String roleName) {

        return roleDao.findByRoleName(roleName);
    }
}
