package com.gkrishnan.smsnps;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class Result extends HttpServlet {

	private static final Logger logger = Logger.getLogger(Result.class.getName());
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		processRequest(req,resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		processRequest(req,resp);
	}

	/**
	 * @param req  
	 */
	private void processRequest(HttpServletRequest req, HttpServletResponse resp) {

		try{

			// Get the Datastore Service
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			// Use class Query to assemble a query
			Query q = new Query("VoteInfo");

			int totalCount = 0;
			double nps = 0.0;
			int num_ones = 0;
			int num_minusones = 0;
			int num_zeroes = 0;
			// Use PreparedQuery interface to retrieve results
			PreparedQuery pq = datastore.prepare(q);

			String responseString = "<table border=\"1\"><tr><th>Date</th><th>Mobilehash</th><th>Rank</th></tr>";
			for (Entity result : pq.asIterable()) {
				Date date = (Date) result.getProperty("date");
				String mobile =  (String) result.getProperty("mobile");
				String response = (String)result.getProperty("response");
				responseString += "<tr><td>" + date + "</td><td>" + mobile+"</td><td>" + response + "</td><br/>";
				totalCount += 1;
				//+1 = 9/10 score (Promoters)
				if (response.equalsIgnoreCase("9") || response.equalsIgnoreCase("10")){
					num_ones += 1;
				}
				//0 = 7/8 score (Passives)
				if (response.equalsIgnoreCase("7") || response.equalsIgnoreCase("8")){
					num_zeroes += 1;
				}
				//-1 = 0/1/2/3/4/5/6 score (Detractors)
				if (response.equalsIgnoreCase("0") || response.equalsIgnoreCase("1") || response.equalsIgnoreCase("2")|| response.equalsIgnoreCase("3") || response.equalsIgnoreCase("4") || response.equalsIgnoreCase("5") || response.equalsIgnoreCase("6")){
					num_minusones += 1;
				}
				logger.log(Level.INFO, date + "," + mobile+"," + response);
			}
			responseString += "</table>";
			nps = Math.round(((num_ones - num_minusones)/(double)totalCount) * 100);
			String finalResponse = "<html>Promoters = "+num_ones +"<br/>Detractors = "+num_minusones+"<br/>Passive = "+num_zeroes;
			finalResponse += "<br/>NPS Score: " + nps + "<br/>Total respondents = " + totalCount + responseString +"</html>";
			sendResponse(resp, finalResponse);
		}catch(Exception e){
			logger.log(Level.SEVERE, e.getMessage(), e);
			e.printStackTrace();
		}
	}

	private void sendResponse(HttpServletResponse response, String smsResponse) {
		try {
			PrintWriter pw = response.getWriter(); 
			pw.println(smsResponse);
			pw.flush();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			e.printStackTrace();
		} 
	}
}
