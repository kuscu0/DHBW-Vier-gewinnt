package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Control;


public class Play extends HttpServlet {
	private static final long serialVersionUID = 2893048395274292624L;
	
	
	Control c = new Control();
    boolean yourTurn = true;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        PrintWriter out = response.getWriter();

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


        
        if(request.getParameter("insertbtn") != null) {
        	
        	if (c.getPlayerWon() == 0) {
        		
                c.setChip(Integer.parseInt(request.getParameter("insertbtn")));
                // System.out.println("SPIELER setzt auf " + Integer.parseInt(request.getParameter("insertbtn")));
            	
                c.nextRound();
                
        	}
        } else {
        	if(request.getParameter("playBtn") != null) {
        		c.newRound(true);
        		System.out.println("Anfrage kam von playBtn als " + request.getParameter("playBtn"));
        	} else if(request.getParameter("2playersBtn") != null) {
        		c.newRound(false);
        		System.out.println("Anfrage kam von 2playersBtn als " + request.getParameter("2playersBtn"));
        	}
        	
    	}

        out.println(			"var gameTable = " + Arrays.deepToString(c.getField()) + ";" +
								"createTable(gameTable, document.getElementById(\"gameCanvas\"));</script>");
        
        if(c.getPlayerWon() == 1){
    		out.println("<script>removeButtons();</script>" +
    					"<h1>Du hast das Spiel gewonnen!</h1>" +
    				"</body>" +
        		"</html>");
    		
		
    		c = new Control();
    	    yourTurn = true;
    	   
    	    
    	} else if(c.getPlayerWon() == 2) {
    		out.println("<script>removeButtons();</script>" +
    					"<h1>Der Bot hat das Spiel gewonnen :(</h1>" +
    				"</body>" +
    			"</html>");
    		
    		c = new Control();
    	    yourTurn = true;
    		
    	}
        
        out.close();
        

        // System.out.println(c.getPlayerWonToString());

    }

}