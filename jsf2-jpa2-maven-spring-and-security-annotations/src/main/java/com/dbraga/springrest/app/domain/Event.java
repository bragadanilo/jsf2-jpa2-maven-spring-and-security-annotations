package com.dbraga.springrest.app.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dbraga.springrest.app.util.Util;

@Entity
@Table(name = "event")
public class Event implements AbstractModel{

	private static final long serialVersionUID = -1569652468933551970L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "startDate", nullable = true)
	private Date startDate;
	
	@Column(name = "endDate", nullable = true)
	private Date endDate;
	
	@Column(name = "active", nullable = false)
	private boolean active = true;
		
	@Column(name = "description", nullable = true, length = 1000)
	private String description;
	
	@Column
	private Double totalValue = 0.0;
	
	@Column
	private Double pgValue = 0.0;
	
	@ManyToOne(optional = false)
	private Client client;
	
	@ManyToOne(optional = false)
	private Professional professional;
	
	public Event() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Professional getProfessional() {
		return professional;
	}

	public void setProfessional(Professional professional) {
		this.professional = professional;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public Double getPgValue() {
		return pgValue;
	}

	public void setPgValue(Double pgValue) {
		this.pgValue = pgValue;
	}
	
	@Transient
	public String getScheduledDate() {
		String start = Util.formatDateString(startDate, Util.DATE_FORMAT_DDMMYYYY_HHSS);
		String end = Util.formatDateString(endDate, Util.TIME_HHSS);
		
		return String.format("(%s - %s)", start, end);
	}

	@Override
	public String toString() {
		return client.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}