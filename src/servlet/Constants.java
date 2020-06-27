package servlet;

public class Constants {
    public static final String SEMICOLON = ";";
    public static final String PARAGRAPH_END = "</p>";

    public static final String HTML_START = "<!DOCTYPE html>" + "<html>" + "<head>" + "<meta charset=\"ISO-8859-1\">"
            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\">"
            + "<link rel=\"shortcut icon\" href=\"favicon.ico\"/>" + "<title>Vier gewinnt</title>" + "</head>";
    public static final String HTML_END = "</html>";

    public static final String BODY_START = "<body>";
    public static final String BODY_GAME = "<div id=\"gameCanvas\"></div>" + "<script src=\"js/display.js\"></script>"
            + "<script>";
    public static final String BODY_ONLINE_BTN = "<form action=\"play\" method=\"post\">"
            + "<button name=\"newOnlineGameBtn\">Neues Spiel</button>"
            + "<button name=\"existingGameBtn\">Bestehendem Spiel beitreten</button>" + "</form>";
    public static final String BODY_NEW_ONLINE_GAME_PARAGRAPH_START = "<p style=\"color: white;\">Deine Session ID: ";
    public static final String BODY_HELP = "<h1>Spielregeln</h1>"
            + "<p class=\"spielregeln\">Das Spiel wird auf einem senkrecht stehenden hohlen Spielbrett gespielt, in das die Spieler abwechselnd ihre Spielsteine fallen lassen."
            + "Das Spielbrett besteht aus sieben Spalten (senkrecht) und sechs Reihen (waagerecht). Jeder Spieler besitzt 21 gleichfarbige Spielsteine."
            + "Wenn ein Spieler einen Spielstein in eine Spalte fallen lässt, besetzt dieser den untersten freien Platz der Spalte. Gewinner ist der"
            + "Spieler, der es als erster schafft, vier oder mehr seiner Spielsteine waagerecht, senkrecht oder diagonal in eine Linie zu bringen."
            + "Das Spiel endet unentschieden, wenn das Spielbrett komplett gefüllt ist, ohne dass ein Spieler eine Viererlinie gebildet hat. </p>";
    public static final String BODY_EXISTING_GAME = "<form action=\"play\" method=\"post\">"
            + "<label for=\"sessid\" style=\"color: white;\">Session ID:</label><br>\n"
            + "<input type=\"text\" id=\"sessid\" name=\"sessid\" value=\"\"><br>"
            + "<button name=\"joinGameBtn\">Beitreten!</button>" + "</form>";
    public static final String BODY_END = "</body>";

    public static final String CREATE_TABLE = "createTable(gameTable, document.getElementById(\"gameCanvas\"));</script>";
    public static final String VAR_GAME_TABLE = "var gameTable = ";

    public static final String REMOVE_BUTTON = "<script>removeButtons();</script>";
    public static final String BACK_BUTTON = "<button class=\"backBtn\" onclick=\"location.href = 'index.jsp';\">Zum Hauptmenü</button>";

    public static final String H1_STATUS_TEXT = "<h1 class=\"statusText\">";
    public static final String H1_END = "</h1>";
}
