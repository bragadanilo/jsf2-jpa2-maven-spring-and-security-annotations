package com.dbraga.springrest.app.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.dbraga.springrest.app.domain.Client;
import com.dbraga.springrest.app.domain.Event;
import com.dbraga.springrest.app.domain.Professional;

@Repository("eventDAO")
@Transactional
public class EventDAOImpl extends GenericDAOImp<Event, Integer> implements EventDAO {

	@Override
	public List<Event> getEventsFromInterval(Event event) {
		
		Professional professional = event.getProfessional(); 
		Date startDate = event.getStartDate(); 
		Date endDate = event.getEndDate();
		Integer eventId = event.getId();
		 
		List<Event> events = new ArrayList<Event>();
		StringBuilder bd = new StringBuilder();
		bd.append("SELECT e FROM Event e ");
		bd.append("INNER JOIN FETCH e.professional p ");
		bd.append("WHERE p = :prof ");
		
		bd.append(" and(  :startDate BETWEEN e.startDate and e.endDate ");
		bd.append("	 or  :endDate   BETWEEN e.startDate and e.endDate ");
		bd.append("  or (:startDate < e.startDate and :endDate > e.endDate) )");
		
		bd.append(" and(  :eventId is null or :eventId <> e.id )");
		
		startDate = new Date(startDate.getTime()+1000);
		endDate = new Date(endDate.getTime()+1000);
		
		events = this.getEntityManager()
				.createQuery(bd.toString(), Event.class)
				.setParameter("prof", professional)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("eventId", eventId)
				.getResultList();
		
		return events;
	}

	@Override
	public List<Event> getAllEventsFromClient(Client client) {
		
		List<Event> events = new ArrayList<Event>();
		StringBuilder bd = new StringBuilder();
		bd.append("SELECT e FROM Event e ");
		bd.append("INNER JOIN FETCH e.professional p ");
		bd.append("WHERE e.client = :client ");
		bd.append("ORDER BY e.startDate DESC ");
		
		events = this.getEntityManager()
				.createQuery(bd.toString(), Event.class)
				.setParameter("client", client)
				.getResultList();
		
		return events;
	}

	@Override
	public List<Event> getEventsByDate(Date startDate) {
		Date endDate = extractEndDate(startDate);
		 
		List<Event> events = new ArrayList<Event>();
		StringBuilder bd = new StringBuilder();
		bd.append("SELECT e FROM Event e ");
		bd.append("INNER JOIN FETCH e.professional p ");
		bd.append("WHERE e.startDate BETWEEN :startDate and :endDate ");
    	
		events = this.getEntityManager()
				.createQuery(bd.toString(), Event.class)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		
		return events;
	}

	private Date extractEndDate(Date startDate) {
		Calendar endDate = Calendar.getInstance();
    	endDate.setTime(startDate);    	
    	endDate.set(Calendar.DAY_OF_MONTH, endDate.get(Calendar.DAY_OF_MONTH) +1);
    		
    	return endDate.getTime();
	}

	@Override
	public List<Event> getEventsByParams(Event event) {

		Map<String, Object> params = new HashMap<>();
		List<Event> events = new ArrayList<Event>();
		StringBuilder bd = new StringBuilder();
		bd.append("SELECT e FROM Event e ");
		bd.append("INNER JOIN FETCH e.professional p ");
		bd.append("INNER JOIN FETCH e.client c ");
		bd.append("WHERE (1 = 1) ");

		if (event.getStartDate() != null) {
			bd.append(" AND (e.startDate BETWEEN :startDate and :endDate) ");
			params.put("startDate", event.getStartDate());
			params.put("endDate", extractEndDate(event.getStartDate()));
		}

		if (event.getProfessional() != null) {
			bd.append(" AND ( p = :professional) ");
			params.put("professional", event.getProfessional());
		}

		if (event.getClient() != null) {
			bd.append(" AND (c = :client) ");
			params.put("client", event.getClient());
		}
		
		bd.append(" ORDER BY e.startDate ");

		TypedQuery<Event> query = this.getEntityManager().createQuery(bd.toString(), Event.class);
		for (String param : params.keySet()) {
			query.setParameter(param, params.get(param));
		}
		events = query.getResultList();

		return events;
	}

	

}
