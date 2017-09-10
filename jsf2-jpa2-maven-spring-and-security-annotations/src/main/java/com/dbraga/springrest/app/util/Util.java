package com.dbraga.springrest.app.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class Util {
	private static final DecimalFormat DF = new DecimalFormat("#0.00");
	public static final String DATE_FORMAT_DDMMYYYY_HHSS = "dd/MM/yyyy hh:mm";
	public static final String TIME_HHSS = "hh:mm";
	
	
	public static void addFacesMessage(String msg, MsgType format) {
		Severity severity = null;
		FacesContext instance = FacesContext.getCurrentInstance();

		switch (format) {
		case INFO:
			severity = FacesMessage.SEVERITY_INFO;
			break;
		case WARN:
			severity = FacesMessage.SEVERITY_WARN;
			break;
		case ERROR:
			severity = FacesMessage.SEVERITY_ERROR;
			break;
		case SUCCESS:
			severity = FacesMessage.SEVERITY_INFO;
			break;
		default:
			break;
		}
		instance.addMessage(null, new FacesMessage(severity, msg, msg));
	}
	
	public static String formatDateString(Date date, String dateFormat){
		if(date != null){
			SimpleDateFormat SDF = new SimpleDateFormat(dateFormat);
			return SDF.format(date);
		}
		return "-";
	}
	
	public static String formatDoubleValue(Double value){
		if(value != null)
			return DF.format(value);
		return "-";
	}
	
	public static Date removeTimeFromDate(Date date){
		Calendar endDate = Calendar.getInstance();
    	endDate.setTime(date);    	
    	endDate.set(Calendar.HOUR_OF_DAY, 0);
    	endDate.set(Calendar.MINUTE, 0);
    	endDate.set(Calendar.SECOND, 0);
    		
    	return endDate.getTime();
	}
	
}
