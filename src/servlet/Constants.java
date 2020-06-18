package servlet;

public class Constants 
{
	public static final String SEMICOLON = ";";
	
	public static final String HTML_START = "<!DOCTYPE html>" +
            "<html>" +
        	"<head>" +
        		"<meta charset=\"UTF-8\">" +
        		"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>" +
        		"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\">" +
        		"<link rel=\"shortcut icon\" href=\"favicon.ico\"/>" +
        		"<title>Vier gewinnt</title>" +
        	"</head>";
	public static final String HTML_END = "</html>";
	
	public static final String BODY_START = "<body>" +
    		"<div id=\"gameCanvas\"></div>" +
    		"<script src=\"js/display.js\"></script>" +
    		"<script>";	
	public static final String BODY_END = "</body>";
	
	public static final String CREATE_TABLE = "createTable(gameTable, document.getElementById(\"gameCanvas\"));</script>";
	public static final String VAR_GAME_TABLE = "var gameTable = ";
	
	public static final String REMOVE_BUTTON = "<script>removeButtons();</script>";
	public static final String BACK_BUTTON = "<button class=\"backBtn\" onclick=\"location.href = 'index.jsp';\">Zur Startseite</button>";
	
	public static final String H1_STATUS_TEXT = "<h1 class=\"statusText\">";
	public static final String H1_END = "</h1>";
}
