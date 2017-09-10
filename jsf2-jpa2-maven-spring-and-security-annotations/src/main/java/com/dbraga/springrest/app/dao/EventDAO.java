package com.dbraga.springrest.app.dao;

import java.util.Date;
import java.util.List;

import com.dbraga.springrest.app.domain.Client;
import com.dbraga.springrest.app.domain.Event;

public interface EventDAO extends GenericDAO<Event, Integer> {

	List<Event> getEventsFromInterval(Event event);

	List<Event> getAllEventsFromClient(Client client);

	List<Event> getEventsByDate(Date startDate);

	List<Event> getEventsByParams(Event event);

}
