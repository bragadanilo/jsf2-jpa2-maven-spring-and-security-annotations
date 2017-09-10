package com.dbraga.springrest.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbraga.springrest.app.dao.EventDAO;
import com.dbraga.springrest.app.domain.Client;
import com.dbraga.springrest.app.domain.Event;
import com.dbraga.springrest.app.domain.HistoricalEvent;
import com.dbraga.springrest.app.util.Util;

@Service("eventService")
public class EventService {

	@Autowired
	private EventDAO eventDao;
	private static final String NEW_LINE = "\r\n";

	public boolean hasAvailability(Event event) {
		List<Event> otherEvents = eventDao.getEventsFromInterval(event);
		return otherEvents.isEmpty();
	}


	public HistoricalEvent createHistorical(Client client) {
		List<Event> otherEvents = eventDao.getAllEventsFromClient(client);
		StringBuilder historical = new StringBuilder("");
		StringBuilder payments = new StringBuilder("");
		HistoricalEvent historicalEvent = new HistoricalEvent();
		Double paymentEvents = 0.0;
		
		for (Event eventHist : otherEvents) {
			String date = Util.formatDateString(eventHist.getStartDate(),Util.DATE_FORMAT_DDMMYYYY_HHSS);
			String professional = eventHist.getProfessional().toString();
			String totalValue = Util.formatDoubleValue(eventHist.getTotalValue());
			String pgValue = Util.formatDoubleValue(eventHist.getPgValue());
			String description = (eventHist.getDescription() != null ? eventHist.getDescription() : "-");
			
			StringBuilder eventDetail = new StringBuilder();
			eventDetail.append("------------------------------------------------------------").append(NEW_LINE);
			eventDetail.append(String.format("Consulta: %s - %s%s", date, professional, NEW_LINE));
			eventDetail.append(String.format("Valor total: R$ %s - Valor pago: %s%s",totalValue, pgValue, NEW_LINE));
			eventDetail.append(" ").append(NEW_LINE);
			eventDetail.append(description).append(NEW_LINE);
			
			paymentEvents += (eventHist.getTotalValue() - eventHist.getPgValue());
			
			historical.append(eventDetail);
		}
		if(historical.length() == 0)
			historical.append("Nenhum consulta realizada anteriormente");
		else{
			if(paymentEvents > 0.0 )
				payments.append("Valor devido: ").append(Util.formatDoubleValue(paymentEvents));
			else if(paymentEvents < 0.0 )
				payments.append("Crédito de: ").append(Util.formatDoubleValue(Math.abs(paymentEvents)));
			else
				payments.append("Pagamento em dia");
		}
		
		historicalEvent.setEvents(historical.toString());
		historicalEvent.setPaymentDue(payments.toString());
		return historicalEvent;
	}
	
}