package com.orchard.obs.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orchard.obs.Exceptions.serviceExceptions.CustomerServiceExceptions;
import com.orchard.obs.core.models.Customer;
import com.orchard.obs.core.services.RegistrationServices;





@Component(service = {Servlet.class},
			property = {
					"sling.servlet.paths=/bin/Register",
					"sling.servlet.methods=POST"
            })
public class Register extends SlingAllMethodsServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger=LoggerFactory.getLogger(Register.class);
		
	@Reference
	RegistrationServices registrationServices;

	@Override
	protected void doPost(SlingHttpServletRequest request,SlingHttpServletResponse response) throws IOException, ServletException {
		//response.setContentType("text/plain");
		logger.info("within servlet");
		Customer customer=new Customer();
		String idString=request.getParameter("uname");
		//response.getWriter().println(idString);
		customer.setId(idString);
		String name = request.getParameter("name");
		//response.getWriter().println(name);
		customer.setName(name);
		String password=request.getParameter("password");
		//response.getWriter().println(password);
		customer.setPassword(password);
		String dob=request.getParameter("dob");
		//response.getWriter().println(dob);
		customer.setDob(dob);
		String country=request.getParameter("country");
		//response.getWriter().println(country);
		customer.setCountry(country);
		String state=request.getParameter("state");
		//response.getWriter().println(state);
		customer.setState(state);
		String city=request.getParameter("city");
		//response.getWriter().println(city);
		customer.setCity(city);
		String address=request.getParameter("address");
		//response.getWriter().println(address);
		customer.setAddress(address);
		String pin=request.getParameter("pin");
		//response.getWriter().println(pin);
		customer.setPin(pin);
		String gender=request.getParameter("gender");
		//response.getWriter().println(gender);
		customer.setGender(gender);
		String phone=request.getParameter("phone");
		//response.getWriter().println(phone);
		customer.setPhone(phone);
		String mail=request.getParameter("mail");
		//response.getWriter().println(mail);
		customer.setMail(mail);
		boolean check=false;
		try {
			check=registrationServices.checkDuplicateMail("obs",mail);
		} catch (CustomerServiceExceptions e1) {
			// TODO Auto-generated catch block
			response.getWriter().println(e1.getMessage());
		}
		boolean set=false;
		if(!check) {
			try {
				set=registrationServices.registerUser("obs", customer);			
			} catch (Exception e) {
				response.getWriter().println(e.getMessage());
			}
			if(set) {
				response.getWriter().println("Registered");
			}
			else {
				response.getWriter().println("An account with this Username already exists");			
			}
		}
		else {
			response.getWriter().println("An account with this Mail already exists");
		}
	}
	
}
