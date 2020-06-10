package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Control;


public class Play extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	
	Control c = new Control();
	int turns = 0;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		if(turns == 0) {
			c.newRound(true);
		}
		
		// The Actual game
		
		boolean yourTurn = true;
		int[][] field = null;
		
		if(c.getPlayerWon() == 0) {
			
			if (yourTurn && request.getParameter("insertbtn") != null) {
				c.setChip(Integer.parseInt(request.getParameter("insertbtn")));
			}
			
			turns++;
			System.out.println(turns);
			yourTurn = !yourTurn;
			
			field = c.getField();
			
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
					"<jsp:useBean id=\"control\" class=\"bean.Control\">" + 
					"</jsp:useBean>"+
					"<div id=\"gameCanvas\"></div>"+
					"<script src=\"js/display.js\"></script>" +
					"<script>" +
						"var testTableData = " + Arrays.deepToString(field) + ";" + 
						"createTable(testTableData,document.getElementById(\"gameCanvas\"));" + 
					"</script>" +
					"</body>" +
				"	</html>");
			out.close();
			
			c.nextRound();
				
				
				
			}
			
			
			
			// System.out.println(c.getPlayerWonToString());
			
			//Game end
		
	}

}
