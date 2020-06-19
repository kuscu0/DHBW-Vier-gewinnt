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
        
        if(request.getParameter("insertbtn") != null) insertCoin(session, request);
        else 
        {
        	c = new Control();
        	if(request.getParameter("playBtn") != null)  createBotMatch(session);
    		else if(request.getParameter("2playersBtn") != null) createLocalMatch(session);
    		else if (request.getParameter("") != null) createOnlineMatch(session);
    	}
        
        printHtmlDoc(response);
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
		c.newRound(false);
		c.setRefresh(session);
    	roundType = RoundType.LOCAL;
    }
    
    /**
     * Creates a Local Game against an Bot on the given Session.
     * Needs to run getSession(HttpServletRequest request, String sessionID) before
     */
    private void createBotMatch(HttpSession session)
    {
		c.newRound(true);
		c.setRefresh(session);
    	roundType = RoundType.BOT;
    }
    
    /**
     * Creates a Online Game With Two Player on the given Session.
     * Needs to run getSession(HttpServletRequest request, String sessionID) before
     */
    private void createOnlineMatch(HttpSession session)
    {
    	c.newRound(false);
    	c.setRefresh(session);
    	roundType = RoundType.ONLINE;
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
            c.setChip(Integer.parseInt(request.getParameter("insertbtn")));
            c.nextRound();
    	}
        c.setRefresh(session);
    }
    
    
    /**
     * Prints the HTML Document so the Browser can show us the Output
     */
    private void printHtmlDoc(HttpServletResponse response) throws IOException
    {
        PrintWriter out = response.getWriter();
    	
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
        
        out.close();
    }
}