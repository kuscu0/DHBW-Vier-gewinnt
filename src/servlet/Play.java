package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Control;


public class Play extends HttpServlet {
	private static final long serialVersionUID = 2893048395274292624L;
	
	
	Control c;
    boolean yourTurn = true;
    
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {	
    	doPost(request, response, null);
    }
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response, String sessionID) throws ServletException, IOException
    {	
        PrintWriter out = response.getWriter();
        HttpSession session = getSession(request, sessionID);
        
        System.out.println(session.getId());

        out.println("<!DOCTYPE html>" +
		            "<html>" +
		            	"<head>" +
		            		"<meta charset=\"UTF-8\">" +
		            		"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>" +
		            		"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\">" +
		            		"<link rel=\"shortcut icon\" href=\"favicon.ico\"/>" +
		            		"<title>Vier gewinnt</title>" +
		            	"</head>" +
		            	"<body>" +
		            		"<div id=\"gameCanvas\"></div>" +
		            		"<script src=\"js/display.js\"></script>" +
		            		"<script>");


        
        if(request.getParameter("insertbtn") != null) 
        {
    		c.getRefresh(session);
            c.setChip(Integer.parseInt(request.getParameter("insertbtn")));
            c.nextRound();
            c.setRefresh(session);
        } 
        else 
        {
        	c = new Control();
        	if(request.getParameter("playBtn") != null) {
        		c.newRound(true);
        		c.setRefresh(session);
        	} else if(request.getParameter("2playersBtn") != null) {
        		c.newRound(false);
        		c.setRefresh(session);
        	}
        	yourTurn = true;
    	}

        out.println(			"var gameTable = " + Arrays.deepToString(c.getField()) + ";" +
								"createTable(gameTable, document.getElementById(\"gameCanvas\"));</script>");
        
        if(c.checkGewonnen()) {
        	
        	
        	
        	if(c.getPlayerWon() == 1){
        		out.println("<script>removeButtons();</script>" +
        					"<h1 class=\"statusText\">Du hast das Spiel gewonnen!</h1>");
        		
        	} else if(c.getPlayerWon() == 2) {
        		
        		out.println("<script>removeButtons();</script>" +
        					"<h1 class=\"statusText\">Du hast verloren :(</h1>");
        	}
        }
        
        out.println(		"<button class=\"backBtn\" onclick=\"location.href = 'index.jsp';\">Zur Startseite</button>" +
        				"</body>" +
        			"</html>");
        
        
        out.close();
    }

    private HttpSession getSession(HttpServletRequest request, String sessionID)
    {
    	HttpSession session = request.getSession();
        if (sessionID != null)
        {
        	ServletContext srvContext = getServletContext();
        	session = (HttpSession) srvContext.getAttribute(sessionID);
        }
        return session;
    }
}