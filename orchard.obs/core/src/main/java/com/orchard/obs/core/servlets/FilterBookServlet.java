package com.orchard.obs.core.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.orchard.obs.Exceptions.serviceExceptions.SideNavigationServiceException;
import com.orchard.obs.core.entity.Book;
import com.orchard.obs.core.services.SideNavigationServices;

@Component(service = { Servlet.class }, property = { "sling.servlet.paths=/bin/FilterBookServlet",
		"sling.servlet.extensions=json", "sling.servlet.methods=GET" })

public class FilterBookServlet extends SlingAllMethodsServlet {
	private Logger logger = LoggerFactory.getLogger(FilterBookServlet.class);

	@Reference
	SideNavigationServices sideNavigationServices;
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		logger.error("now Inside FilterBookServlet servlet");
		response.setContentType("application/json");
		List<Book> filteredBook;
		String first = request.getParameter("first");
		String second = request.getParameter("second");
		String customerId = request.getParameter("customerId");
		try {
			if (first.equals("genre"))

				filteredBook = sideNavigationServices.getBookBasedOnGenre("bookworm", second, customerId);

			else if (first.equals("publisher"))
				filteredBook = sideNavigationServices.getBookBasedOnPublisher("bookworm", second, customerId);
			else
				filteredBook = sideNavigationServices.getBookBasedOnGenreAndPublisher("bookworm", first, second,
						customerId);
			String json = new Gson().toJson(filteredBook);
			logger.error(json);
			response.getWriter().print(json);
		} catch (SideNavigationServiceException e) {
			logger.info(e.getMessage());
		}
	}
}
