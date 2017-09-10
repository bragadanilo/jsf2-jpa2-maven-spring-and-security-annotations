package com.dbraga.springrest.app.managedbean;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dbraga.springrest.app.converter.ClientConverter;
import com.dbraga.springrest.app.converter.ProfessionalConverter;
import com.dbraga.springrest.app.dao.ClientDAO;
import com.dbraga.springrest.app.dao.EventDAO;
import com.dbraga.springrest.app.dao.ProfessionalDAO;
import com.dbraga.springrest.app.domain.Client;
import com.dbraga.springrest.app.domain.Event;
import com.dbraga.springrest.app.domain.HistoricalEvent;
import com.dbraga.springrest.app.domain.Professional;
import com.dbraga.springrest.app.service.EventService;
import com.dbraga.springrest.app.util.MsgType;
import com.dbraga.springrest.app.util.Util;


@Component
@Scope("view")
public class ScheduleBean {
	
	@Resource
	private ClientDAO clientDao;
	
	@Resource
	private EventDAO eventDao;
	
	@Resource
	private ProfessionalDAO professionalDao;
	
	@Resource
	private EventService eventService; 
	
	private ScheduleModel eventModel;
    private Event event = null;
    
    private List<Client> clientList;
    private ClientConverter clientConverter;
	
	private List<Professional> professionalList;
	private ProfessionalConverter professionalConverter;
	
	@ManagedProperty("#{param.modelId}")
	private Integer modelId;
	
	@ManagedProperty("#{param.returnPage}")
	private String returnPage;
	
	private HistoricalEvent historical;
	
    @PostConstruct
    public void init() {
        updateList();
        
        clientList = clientDao.all();
        clientConverter = new ClientConverter(clientList);
        professionalList = professionalDao.all();
        professionalConverter = new ProfessionalConverter(professionalList);
    }
    
    public String addEvent() {
        try {
        	if(eventService.hasAvailability(event) && validateEvent(event)){
				if(event.getId() == null){
					eventDao.save(event);
				    eventModel.addEvent(toDefaultEvent(event));
				} 
				else { 
					eventDao.update(event);
				    eventModel.updateEvent(toDefaultEvent(event));
				}
				event = new Event();
				
				Util.addFacesMessage("Evento salvo com sucesso", MsgType.SUCCESS);
				
				FacesContext context = FacesContext.getCurrentInstance();
				context.getExternalContext().getFlash().setKeepMessages(true);
				return returnPage + "?faces-redirect=true";
        	}
        	else{
        		Util.addFacesMessage("Horário indisponível", MsgType.ERROR);
        	}
		} catch (Exception e) {
			Util.addFacesMessage("Erro ao salvar um agendamento", MsgType.ERROR);
			e.printStackTrace();
		}
        return "";
    }
    private boolean validateEvent(Event event) {
		if(event.getStartDate().after(event.getEndDate())){
			Util.addFacesMessage("Hora inicial após hora final", MsgType.ERROR);
			return false;
		}
		return true;
	}

	public String removeEvent() {
    	try {
			eventDao.delete(event);
			Util.addFacesMessage("Evento removido com sucesso", MsgType.SUCCESS);
			return returnPage + "?faces-redirect=true";
		} catch (Exception e) {
			Util.addFacesMessage("Erro ao remover um agendamento", MsgType.ERROR);
			e.printStackTrace();
		}
    	return "";
    }
    
    public void historical(Client client) {
    	try {
    		historical = eventService.createHistorical(client);
    		
		} catch (Exception e) {
			Util.addFacesMessage("Erro ao remover um agendamento", MsgType.ERROR);
			e.printStackTrace();
		}
    }

	public void loadById() {
		if(modelId > 0)
			event = eventDao.searchById(modelId);
		else{
			SelectEvent selectEvent = (SelectEvent) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("selectEvent");
			event = new Event();
	    	
			Date eventDate = (selectEvent != null) ? (Date) selectEvent.getObject() : new Date();
			Calendar nowStart = Calendar.getInstance();
	    	
	    	Calendar startDate = Calendar.getInstance();
	    	startDate.setTime(eventDate);
	    	if(!hasHourOrMinute(startDate))
	    		startDate.set(Calendar.HOUR_OF_DAY, nowStart.get(Calendar.HOUR_OF_DAY));
	    	
	    	
	    	Calendar endDate = Calendar.getInstance();
	    	endDate.setTime(eventDate);
	    	if(!hasHourOrMinute(endDate))
	    		endDate.set(Calendar.HOUR_OF_DAY, nowStart.get(Calendar.HOUR_OF_DAY) +1);
	    	else
	    		endDate.set(Calendar.HOUR_OF_DAY, endDate.get(Calendar.HOUR_OF_DAY) +1);
	    	
	    	event.setStartDate(startDate.getTime());
	    	event.setEndDate(endDate.getTime());
		}
	}

	public void onEventSelect(SelectEvent selectEvent) {
		ScheduleEvent scheduleEvent = (ScheduleEvent) selectEvent.getObject();
		modelId = Integer.valueOf(scheduleEvent.getId());
		goToAddEventScreen();
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectEvent", selectEvent);
    	modelId = 0;
    	goToAddEventScreen();
    }
    
    public void goToAddEventScreen() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("schedule-event.xhtml?faces-redirect=true&includeViewParams=true&returnPage=schedule&modelId=" + modelId);
			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			Util.addFacesMessage("Erro ao criar um agendamento", MsgType.ERROR);
			e.printStackTrace();
		}
	}
    
	public String cancel(){
		System.out.println(returnPage);
		return returnPage + "?faces-redirect=true";
	}
	
    public void updateList(){
    	eventModel = new DefaultScheduleModel();
    	List<Event> eventList = eventDao.all();
    	for (Event event : eventList) {
    		DefaultScheduleEvent defaultEvent = toDefaultEvent(event);
    		eventModel.addEvent(defaultEvent);
    		defaultEvent.setId(event.getId().toString());
    		defaultEvent.setStyleClass(event.getProfessional().getEventColor());
		}
    }
    
    private DefaultScheduleEvent toDefaultEvent(Event event) {
    	DefaultScheduleEvent scheduleEvent = new DefaultScheduleEvent(event.toString(), event.getStartDate(),event.getEndDate());
    	if(event.getId() != null)
    		scheduleEvent.setId(event.getId().toString());
		return scheduleEvent;
	}
    
	private boolean hasHourOrMinute(Calendar date) {
		return date.get(Calendar.HOUR) != 0 || date.get(Calendar.MINUTE) != 0;
	}
    
	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<Client> getClientList() {
		return clientList;
	}

	public void setClientList(List<Client> clientList) {
		this.clientList = clientList;
	}

	public List<Professional> getProfessionalList() {
		return professionalList;
	}

	public void setProfessionalList(List<Professional> professionalList) {
		this.professionalList = professionalList;
	}

	public ClientConverter getClientConverter() {
		return clientConverter;
	}

	public void setClientConverter(ClientConverter clientConverter) {
		this.clientConverter = clientConverter;
	}

	public ProfessionalConverter getProfessionalConverter() {
		return professionalConverter;
	}

	public void setProfessionalConverter(ProfessionalConverter professionalConverter) {
		this.professionalConverter = professionalConverter;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public HistoricalEvent getHistorical() {
		return historical;
	}

	public void setHistorical(HistoricalEvent historical) {
		this.historical = historical;
	}

	public String getReturnPage() {
		return returnPage;
	}

	public void setReturnPage(String returnPage) {
		this.returnPage = returnPage;
	}

}
