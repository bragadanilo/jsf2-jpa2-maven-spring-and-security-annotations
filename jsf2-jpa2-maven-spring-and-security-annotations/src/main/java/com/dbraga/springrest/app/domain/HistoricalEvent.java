package com.dbraga.springrest.app.domain;

public class HistoricalEvent {
	private String events;
	private String paymentDue;
	
	public String getEvents() {
		return events;
	}
	public void setEvents(String events) {
		this.events = events;
	}
	public String getPaymentDue() {
		return paymentDue;
	}
	public void setPaymentDue(String paymentDue) {
		this.paymentDue = paymentDue;
	}
}
