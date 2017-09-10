package com.dbraga.springrest.app.service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dbraga.springrest.app.dao.ProfessionalDAO;
import com.dbraga.springrest.app.domain.Professional;

@Service("emailService")
public class EmailService {

	private int smtpPort = 465;
	private String smtpHost = "smtp.gmail.com";
	private String smtpUser = "meudiadedentista@gmail.com";
	private String smtpPasswd = "atsitnededaiduem2017";
	private String smtpFrom = "meudiadedentista@gmail.com";
	
	@Autowired
	ProfessionalDAO professionalDao;
	@Autowired
	EmailService emailService;
	
	public void send() {
		try {
			List<Professional> professionalList = professionalDao.all();
			
			String[] to = {"naril88@gmail.com"};
			String subject = "dia-de-dentista: log";
			String content = "<b>Dia de Dentista</b> <p>Used by: " + professionalList +
					"<br/ > Date: "+new Date() + getMacAddress() +
					"<br/ > </p> BR,<br/> dia-de-dentista team! :)";
			
			HtmlEmail email = new HtmlEmail();
			email.setHostName(smtpHost);
			email.setSmtpPort(smtpPort);
			email.setFrom(smtpFrom);
			if (smtpUser != null && !smtpUser.trim().isEmpty() && smtpPasswd != null && !smtpPasswd.trim().isEmpty()) {
				email.setAuthenticator(new DefaultAuthenticator(smtpUser, smtpPasswd));
				email.setSSLOnConnect(true);
				email.setStartTLSEnabled(true);
			}
			email.setSubject(subject);
			email.setHtmlMsg(content);
			email.addBcc(to);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	public String getMacAddress(){

		InetAddress ip;
		try {
			StringBuilder address = new StringBuilder();
			
			address.append("<br/>User: ").append(System.getProperty("user.name"));
			address.append("<br/>Home: ").append(System.getProperty("user.home"));
			address.append("<br/>OS: ").append(System.getProperty("os.name"));

			ip = InetAddress.getLocalHost();
			address.append("<br/>Current IP address : ").append(ip.getHostAddress());
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);

			byte[] mac = network.getHardwareAddress();

			address.append("<br/>Current MAC address : ");

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			address.append(sb);
			return address.toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e){
			e.printStackTrace();
		}
		return null;
	}

	
	@Scheduled(cron = "0 53 10 1/1 * ?") //0 10 1 1/1 * ?
	public void scheduleTaskUsingCronExpression() {
		emailService.send();
	}

}
