package com.dbraga.springrest.app.managedbean;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dbraga.springrest.app.dao.EventDAO;
import com.dbraga.springrest.app.domain.Event;
import com.dbraga.springrest.app.util.MsgType;
import com.dbraga.springrest.app.util.Util;


@Component
@Scope("view")
public class ScheduleListBean {
	
	@Resource
	private EventDAO eventDao;
	
	private List<Event> list;
	private Event event = new Event();
	private boolean anyDate;  
	
	@ManagedProperty("#{param.modelId}")
	private Integer modelId;
	
	@PostConstruct
	private void init() {
		updateList();
	}
	
	public String details(Integer modelId){
		return "schedule-event?faces-redirect=true&includeViewParams=true&returnPage=schedule-list&modelId="+modelId;
	}
	
	public void addNewEvent() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("schedule-event.xhtml?faces-redirect=true&includeViewParams=true&returnPage=schedule-list&modelId=0");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			Util.addFacesMessage("Erro ao criar um agendamento", MsgType.ERROR);
			e.printStackTrace();
		}
	}
	
	public List<Event> getList() {
		return list;
	}

	public void updateList() {
		if(isAnyDate()){
			//event.setStartDate(null);
			list = eventDao.all();
			return;
		}
		else if(event.getStartDate() == null) {
			event.setStartDate(Util.removeTimeFromDate(new Date()));
		}
		
		list = eventDao.getEventsByParams(event);
	}
	
		
	public void setList(List<Event> list) {
		this.list = list;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public boolean isAnyDate() {
		return anyDate;
	}

	public void setAnyDate(boolean anyDate) {
		this.anyDate = anyDate;
	}

	public void rest() {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("http://www.mocky.io/v2/58b5d0bb100000151bea5772", String.class);
        System.out.println("RES: " + result);
    }

}
