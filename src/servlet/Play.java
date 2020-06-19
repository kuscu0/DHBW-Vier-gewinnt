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


public class Play extends HttpServlet 
{
	private static final long serialVersionUID = 2893048395274292624L;

	Control c;
    private RoundType roundType;
    

    /**
     *  The Overriden Servlet Function
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {	
    	doPost(request, response, null);
    }
    

    
    /**
     * @param sessionID The ID of the existing Session ID, which you want to join (For Online Player mostly)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response, String sessionID) throws ServletException, IOException
    {	
        HttpSession session = getSession(request, sessionID);
        
        if(request.getParameter("insertBtn") != null)
        { 
        	insertCoin(session, request);
        	printHtmlDoc(response, "insertBtn", sessionID);
        }
        if(request.getParameter("playBtn") != null)
        {
        	c = new Control();
    		createBotMatch(session);
    		printHtmlDoc(response, "playBtn", sessionID);
        } 
        else if(request.getParameter("2playersBtn") != null) 
        {
        	c = new Control();
			createLocalMatch(session);
			printHtmlDoc(response, "2playersBtn", sessionID);
    	} 
        else if(request.getParameter("onlineBtn") != null) 
    	{
    		c = new Control();
    		createOnlineMatch(session);
    		printHtmlDoc(response, "onlineBtn", sessionID);
        } 
        else if(request.getParameter("helpBtn") != null)
        {
        	printHtmlDoc(response, "helpBtn", sessionID);
        } 
        else if(request.getParameter("newOnlineGameBtn") != null) 
        {
    		c = new Control();
    		createOnlineMatch(session);
    		printHtmlDoc(response, "newOnlineGameBtn", sessionID);
        }
        else if(request.getParameter("existingGameBtn") != null) 
        {
    		c = new Control();
    		createOnlineMatch(session);
    		printHtmlDoc(response, "existingGameBtn", sessionID);
        } else if(request.getParameter("insertBtn") != null) 
        { 
        	insertCoin(session, request);
        	printHtmlDoc(response, "insertBtn", sessionID);
        }
    }

    /**
     * @param request The HttpRequest where the session should come from
     * @param sessionID An already existing session ID or null
     * @return The Session which the Player will play on the Server
     */
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
    
    /**
     * Creates a Local Game With Two Player on the given Session.
     * Needs to run getSession(HttpServletRequest request, String sessionID) before
     */
    private void createLocalMatch(HttpSession session)
    {
		c.newRound(RoundType.LOCAL);
		c.setRefresh(session);
    }
    
    /**
     * Creates a Local Game against an Bot on the given Session.
     * Needs to run getSession(HttpServletRequest request, String sessionID) before
     */
    private void createBotMatch(HttpSession session)
    {
		c.newRound(RoundType.BOT);
		c.setRefresh(session);
    }
    
    /**
     * Creates a Online Game With Two Player on the given Session.
     * Needs to run getSession(HttpServletRequest request, String sessionID) before
     */
    private void createOnlineMatch(HttpSession session)
    {
    	c.newRound(RoundType.ONLINE);
    	c.setRefresh(session);
    }
    
    /**
     * With this function a round is played
     * 
     * @param session
     * @param request
     */
    private void insertCoin(HttpSession session, HttpServletRequest request)
    {
		c.getRefresh(session);
    	if (roundType == RoundType.ONLINE)
    	{

    	}
    	else
    	{
            c.setChip(Integer.parseInt(request.getParameter("insertBtn")));
            c.nextRound();
    	}
        c.setRefresh(session);
    }
    
    
    /**
     * Prints the HTML Document so the Browser can show us the Output
     */
    private void printHtmlDoc(HttpServletResponse response, String buttonName, String sessionID) throws IOException
    {
        PrintWriter out = response.getWriter();
        System.out.println(buttonName);
        
        if(buttonName.equals("insertBtn") || buttonName.equals("playBtn") || buttonName.equals("2playersBtn")) {
        	out.println(Constants.HTML_START + Constants.BODY_START);
            out.println(Constants.VAR_GAME_TABLE + Arrays.deepToString(c.getField()) + Constants.SEMICOLON + Constants.CREATE_TABLE);
            
            if(c.checkGewonnen()) 
            {
            	if(c.getPlayerWon() == 1)
            	{
            		out.println(Constants.REMOVE_BUTTON +
            					Constants.H1_STATUS_TEXT + "Du hast das Spiel gewonnen!" + Constants.H1_END);
            		
            	}
            	else if(c.getPlayerWon() == 2) 
            	{
            		out.println(Constants.REMOVE_BUTTON +
            					Constants.H1_STATUS_TEXT + "Du hast verloren :( " + Constants.H1_END);
            	}
            }
            
            out.println(Constants.BACK_BUTTON +
            			Constants.BODY_END +
            			Constants.HTML_END);
            
        }
        
        if(buttonName.equals("helpBtn")) {
        	out.println(Constants.HTML_START);
        	
        	out.println("<h1>Regeln</h1>" + 
	        			"<p style=\"color: white; margin: 10%;\">Das Spiel wird auf einem senkrecht stehenden hohlen Spielbrett gespielt, in das die Spieler abwechselnd ihre Spielsteine fallen lassen." + 
	        			"Das Spielbrett besteht aus sieben Spalten (senkrecht) und sechs Reihen (waagerecht). Jeder Spieler besitzt 21 gleichfarbige Spielsteine." + 
	        			"Wenn ein Spieler einen Spielstein in eine Spalte fallen lässt, besetzt dieser den untersten freien Platz der Spalte. Gewinner ist der" + 
	        			"Spieler, der es als erster schafft, vier oder mehr seiner Spielsteine waagerecht, senkrecht oder diagonal in eine Linie zu bringen." + 
	        			"Das Spiel endet unentschieden, wenn das Spielbrett komplett gefüllt ist, ohne dass ein Spieler eine Viererlinie gebildet hat. </p>");
        	
        	out.println(Constants.HTML_END);
        }
    	
        if(buttonName.equals("onlineBtn")) {
        	out.println(Constants.HTML_START);
        	
        	out.println("<body>" +
        					"<form action=\"play\" method=\"post\">" +
        						"<button name=\"newOnlineGameBtn\">Neues Spiel</button>" + 
        						"<button name=\"existingGameBtn\">Bestehendem Spiel beitreten</button>" + 
        					"</form>" + 
        				"</body>");
        	
        	out.println(Constants.HTML_END);
        	
        }
        
        if(buttonName.equals("newOnlineGameBtn")) {
        	out.println(Constants.HTML_START);
        	
        	out.println("<body>" +
        					"<p style=\"color: white;\">Deine Session ID: " + sessionID + "</p>" + 
        				"</body>");
        	
        	out.println(Constants.HTML_END);
        	
        }
        
        if(buttonName.equals("existingGameBtn")) {
        	out.println(Constants.HTML_START);
        	
        	out.println("<body>" +
        					"<form action=\"play\" method=\"post\">" +
        						"<label for=\"sessid\" style=\"color: white;\">Session ID:</label><br>\n" + 
        						"<input type=\"text\" id=\"sessid\" name=\"sessid\" value=\"\"><br>" +
        						"<button name=\"joinGameBtn\">Beitreten!</button>" + 
        					"</form>" + 
        				"</body>");
        	
        	out.println(Constants.HTML_END);
        	
        }
        
        out.close();
    }
    
}