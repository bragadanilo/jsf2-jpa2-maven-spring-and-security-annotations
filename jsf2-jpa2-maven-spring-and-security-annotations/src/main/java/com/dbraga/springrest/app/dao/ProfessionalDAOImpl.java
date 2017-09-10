package com.dbraga.springrest.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.dbraga.springrest.app.domain.Professional;

@Repository("professionalDAO")
@Transactional
public class ProfessionalDAOImpl extends GenericDAOImp<Professional, Integer> implements ProfessionalDAO {

	@Override
	public List<Professional> getAll() {
		return this.all();
	}

	

	

}
