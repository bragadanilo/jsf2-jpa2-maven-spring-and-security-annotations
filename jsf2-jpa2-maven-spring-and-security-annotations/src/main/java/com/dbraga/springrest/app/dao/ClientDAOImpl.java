package com.dbraga.springrest.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.dbraga.springrest.app.domain.Client;

@Repository("clientDAO")
@Transactional
public class ClientDAOImpl extends GenericDAOImp<Client, Integer> implements ClientDAO {

	@Override
	public List<Client> getAll() {
		return this.all();
	}

	

	

}
