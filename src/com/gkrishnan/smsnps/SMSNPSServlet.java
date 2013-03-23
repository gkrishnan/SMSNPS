package com.gkrishnan.smsnps;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class SMSNPSServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(SMSNPSServlet.class.getName());
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {		
		processRequest(req,resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		processRequest(req,resp);
	}

	private void processRequest(HttpServletRequest req, HttpServletResponse resp) {

		String txtWebMessage = req.getParameter(Constants.TXTWEB_MESSAGE);
		String txtWebMobile = req.getParameter(Constants.TXTWEB_MOBILE);	
		String txtWebProtocol = req.getParameter(Constants.TXTWEB_PROTOCOL);
		boolean eventComplete = false;
		
		if(eventComplete){
			sendResponse(resp,getCompletedMessage().toString());
			return;
		}

		
		if (txtWebProtocol != null && !txtWebProtocol.equalsIgnoreCase("1000")){
			sendResponse(resp,getWrongProtocolMessage().toString());
			return;
		}
		
		if (txtWebMobile == null || txtWebMobile.isEmpty()){
			txtWebMobile = Constants.WEB;
		}		
		if (txtWebMessage == null || txtWebMessage.isEmpty()){
			sendResponse(resp,getWelcomeMessage().toString());
			return;
		}
		if (!txtWebMessage.matches("[0-9]|(10)")){
			sendResponse(resp,getErrorMessage().toString());
			return;
		}

		try{
			createEntry(txtWebMobile, txtWebMessage);
			sendResponse(resp, getReplyMessage().toString());
		}catch(Exception e){
			logger.log(Level.SEVERE, e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public boolean createEntry(String mobile, String rank) {
		return VoteDB.saveVoteInfoEntry(mobile,rank);
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

	private StringBuffer getWelcomeMessage() { 
		return new StringBuffer("<html><head><meta name=\"txtweb-appkey\" content=\""+"your-txtweb-appkey"+"\" /></head><body>Welcome to the SMS NPS Feedback Application!<br/><br/>" + 
		"Please reply with your feedback. Ex.: @srp 10 if you would recommend SRP to others.</body></html>");
	}
	
	private StringBuffer getErrorMessage() { 
		return new StringBuffer("<html><head><meta name=\"txtweb-appkey\" content=\""+"your-txtweb-appkey"+"\" /></head><body>Invalid feedback provided.<br/><br/>" + 
		"Please respond with a number between 0 and 10.<br/>Ex.@srp 10 to give a rating of 10</body></html>");
	}

	private StringBuffer getReplyMessage() { 
		return new StringBuffer("<html><head><meta name=\"txtweb-appkey\" content=\""+"your-txtweb-appkey"+"\" /></head><body>" + 
		"Thank you for providing feedback. Your response has been recorded.</body></html>");
	}
	
	private StringBuffer getWrongProtocolMessage() {
		return new StringBuffer("<html><head><meta name=\"txtweb-appkey\" content=\""+"your-txtweb-appkey"+"\" /></head><body>" + 
		"Sorry, this application works only on SMS.</body></html>");
	}
	
	private StringBuffer getCompletedMessage() { 
		return new StringBuffer("<html><head><meta name=\"txtweb-appkey\" content=\""+"your-txtweb-appkey"+"\" /></head><body>" + 
		"Sorry, your vote has not been recorded. The voting lines are now closed.</body></html>");
	}
}
