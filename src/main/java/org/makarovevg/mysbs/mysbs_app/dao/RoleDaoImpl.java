package org.makarovevg.mysbs.mysbs_app.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.makarovevg.mysbs.mysbs_app.models.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    public RoleDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void create(Role role) {

        Role newRole = entityManager.createQuery("from Role r where  r.roleName =:roleName ", Role.class)
                .setParameter("roleName", role.getRoleName())
                .getResultStream()
                .findAny()
                .orElse(null);

        if (newRole == null) {
            entityManager.persist(role);
        } else {
            throw new RuntimeException("Роль \"" + role.getRoleName() + "\" уже существует, поэтому не была создана!");
        }
    }

    @Override
    public void delete(long id) {
        entityManager.createQuery("DELETE FROM Role WHERE id=:id", Role.class)
                .setParameter("id", id)
                .executeUpdate();

    }

    @Override
    public Optional<Role> findByRoleName(String roleName) {

        return Optional.of(entityManager.createQuery("from Role r where r.roleName =:roleName ", Role.class)
                .setParameter("roleName", roleName)
                .getSingleResult());
    }
}
