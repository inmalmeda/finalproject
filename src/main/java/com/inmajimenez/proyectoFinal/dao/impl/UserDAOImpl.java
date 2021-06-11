package com.inmajimenez.proyectoFinal.dao.impl;

import com.inmajimenez.proyectoFinal.dao.UserDAO;
import com.inmajimenez.proyectoFinal.model.entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager manager;

    /**
     * It returns an expert if it exist
     *
     * @param user User
     * @return The user looged
     */
    @Override
    public User findOneUserLoggin(User user) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<User> criteria =  builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);

        System.out.println(user.getEmail() + " " + user.getPassword() + "**********************");

        List<Predicate> predicates = new ArrayList<>();

        if(user.getEmail()!=null)
            predicates.add(builder.equal(root.get("email"), user.getEmail()));

        if(user.getPassword()!=null)
            predicates.add(builder.equal(root.get("password"), user.getPassword()));

        criteria.distinct(true).select(root).where(builder.and(predicates.toArray(new Predicate[0])));

        return manager.createQuery(criteria).getSingleResult();
    }
}
