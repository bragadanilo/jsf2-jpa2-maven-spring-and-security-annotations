package com.dbraga.springrest.app.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.dbraga.springrest.app.domain.User;

@Repository
public class UserDAOImpl extends GenericDAOImp<User, Integer> implements UserDAO {

	@Autowired
	private EntityManagerFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public User findByUserName(String username){

		List<User> users = new ArrayList<User>();

		users = sessionFactory.createEntityManager()
			.createQuery("from User where username = :user")
			.setParameter("user", username)
			.getResultList();

		if (users.size() > 0) {
			return users.get(0);
		} else {
			throw new UsernameNotFoundException("User not found");
		}

	}

	@Override
	public User getUserWithRoles(Integer modelId) {
		User user = this.getEntityManager()
					.createQuery("from User u left join fetch u.userRole where u.id = :id", User.class)
					.setParameter("id", modelId).getSingleResult();
		return user;
	}

}