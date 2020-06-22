package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;

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

	private Control c;
    

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
        PrintWriter out = response.getWriter();
        /*
        Iterator<String> params = request.getParameterNames().asIterator();
        
        while (params.hasNext())
        {
        	System.out.println(params.next());
        }
*/
        if(request.getParameter("playBtn") != null)
        {
        	c = new Control();
    		createBotMatch(session);
    		printHtmlGame(out, session.getId());
        } 
        else if(request.getParameter("2playersBtn") != null) 
        {
        	c = new Control();
			createLocalMatch(session);
			printHtmlGame(out, session.getId());
    	} 
        else if(request.getParameter("onlineBtn") != null) 
    	{
    		c = new Control();
    		createOnlineMatch(session);
    		printHtmlOnlineBtn(out, session.getId());
        } 
        else if(request.getParameter("helpBtn") != null)
        {
        	printHtmlHelp(out, session.getId());
        } 
        else if(request.getParameter("newOnlineGameBtn") != null) 
        {
    		c = new Control();
    		createOnlineMatch(session);
    		printHtmlNewOnlineGame(out, session.getId());
        }
        else if(request.getParameter("existingGameBtn") != null) 
        {
    		printHtmlExistingGame(out, session.getId());
        } 
        else if(request.getParameter("insertBtn") != null) 
        { 
        	insertCoin(session, request);
        	printHtmlGame(out, session.getId());
        }
        else if(request.getParameter("joinGameBtn") != null)
        {
        	printHtmlGame(out, session.getId());
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
    	if (c.getRoundType() == RoundType.ONLINE)
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
     * Prints the HTML Document of the Game so the Browser can show us the Output
     * 
     * @param out The PrintWriter of the ServletResponse
     * @param sessionID The Session ID of the Match
     */
    private void printHtmlGame(PrintWriter out, String sessionID) throws IOException
    {
      	out.println(Constants.HTML_START + Constants.BODY_START + Constants.BODY_GAME);
        out.println(Constants.VAR_GAME_TABLE + Arrays.deepToString(c.getFieldWithNewestChip()) + Constants.SEMICOLON + Constants.CREATE_TABLE);
        
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
    
    
    /**
     * Prints the HTML Document of Document when you want to join an Online Match so the Browser can show us the Output
     * 
     * @param out The PrintWriter of the ServletResponse
     * @param sessionID The Session ID of the Match
     */
    private void printHtmlOnlineBtn(PrintWriter out, String sessionID)
    {
    	out.println(Constants.HTML_START);
    	out.println(Constants.BODY_START +
    					Constants.BODY_ONLINE_BTN +
    					Constants.BACK_BUTTON +
    				Constants.BODY_END);
    	out.println(Constants.HTML_END);
    	
    	out.close();
    }
    
    /**
     * Prints the HTML Document of an New Online Game so the Browser can show us the Output
     * 
     * @param out The PrintWriter of the ServletResponse
     * @param sessionID The Session ID of the Match
     */
    private void printHtmlNewOnlineGame(PrintWriter out, String sessionID)
    {
    	out.println(Constants.HTML_START);
    	out.println(Constants.BODY_START +
    					Constants.BODY_NEW_ONLINE_GAME_PARAGRAPH_START + sessionID + Constants.PARAGRAPH_END + 
    					Constants.BACK_BUTTON + 
    				Constants.BODY_END);
    	out.println(Constants.HTML_END);
    	
    	out.close();
    }
    
    /**
     * Prints the HTML Document of Help Page so the Browser can show us the Output
     * 
     * @param out The PrintWriter of the ServletResponse
     * @param sessionID The Session ID of the Match
     */
    private void printHtmlHelp(PrintWriter out, String sessionID)
    {
    	out.println(Constants.HTML_START);
    	out.println(Constants.BODY_START +
    					Constants.BODY_HELP +
    					Constants.BACK_BUTTON +
    				Constants.BODY_END);
    	out.println(Constants.HTML_END);
    	
    	out.close();
    }
    
    /**
     * Prints the HTML Document of an Existing Online Game so the Browser can show us the Output
     * 
     * @param out The PrintWriter of the ServletResponse
     * @param sessionID The Session ID of the Match
     */
    private void printHtmlExistingGame(PrintWriter out, String sessionID)
    {
    	out.println(Constants.HTML_START);
    	
    	out.println(Constants.BODY_START +
    					Constants.BODY_EXISTING_GAME +
    					Constants.BACK_BUTTON +  
    				Constants.BODY_END);
    	
    	out.println(Constants.HTML_END);
    	
    	out.close();
    }
}