package bean;

public class Control
{
	private static final int WIDTH = 7;
	private static final int HEIGHT = 6;
		
	private int[][] field = new int[HEIGHT][WIDTH];
	
	private int activePlayer = 1;
	private int round = 0;
	private int playerWon = 0;
	private int lastColumn, lastRow;
	
	private Bot bot;
	
	
	public void newRound(boolean playAgainstBot)
	{
		fillField(0);
		if (playAgainstBot) bot = new Bot(this, WIDTH);
			
		activePlayer = 1;
		round = 0;
		playerWon = 0;
	}
	
	public void botRound() {
		if (bot != null) {
			bot.nextRound(lastColumn, lastRow);
		}
	}
	
	public void nextRound() {
		if (activePlayer == 1) {
			activePlayer = 2;
			botRound();
		} else {
			activePlayer = 1;
		}
	}

	public boolean setChip(int column, int activePlayer)
	{	
		round++;
		
		int chipColor = activePlayer;
		
		if (round <= WIDTH * HEIGHT && playerWon == 0)
		{
			for (int row = HEIGHT - 1; row >= 0; row--)
			{
				if (field[row][column] == 0)
				{
					field[row][column] = chipColor;
					lastColumn = column;
					lastRow = row;
					return checkGewonnen(column, row, chipColor);
				}
			}
		}

		return false;
	}
	
	
	public int[][] getField()
	{
		return field;
	}
	
	public int getPlayerWon()
	{
		return playerWon;
	}
	
	public String getPlayerWonToString()
	{
		StringBuilder sb = new StringBuilder();
		if (playerWon == 0) sb.append("Nobody ");
		else if (bot == null || playerWon == 1) sb.append("Player " + playerWon + " ");
		else sb.append("The Bot ");
		sb.append("won the game.");
		return sb.toString();
	}
	
	private void fillField(int i)
	{
		for(int y = 0; y < HEIGHT; y++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				field[y][x] = i;
			}
		}
	}
	
	
	private boolean checkGewonnen(int x, int y, int chip)
	{
		boolean finished = checkInARow(x, y, chip, 4);
		
		if (finished) playerWon = chip;
		
		return finished;
	}
	
	
	public boolean checkInARow(int x, int y, int chip, int length)
	{
		return checkInARowWithOffset(x, y, chip, length, 0);
	}
	
	
	public boolean checkInARowWithOffset(int x, int y, int chip, int length, int offset)
	{
		if (!(0 <= y && y < HEIGHT) || !(0 <= x && x < WIDTH)) return false;
		
		boolean vertical = checkVertical(x, y, chip, length, offset);
		boolean horizontal = checkHorizontal(x, y, chip, length, offset);
		boolean sidewaysRight = checkSidewaysRight(x, y, chip, length, offset);
		boolean sidewaysLeft = checkSidewaysLeft(x, y, chip, length, offset);	
		
		return horizontal || vertical || sidewaysLeft || sidewaysRight;
	}
	
	
	private boolean checkHorizontal(int x, int y, int chip, int length, int offset)
	{
		int line = offset;
		for (int i = -1 * (length - 1); i < length; i++)
		{
			int xi = x + i;
			if (0 <= xi && xi < WIDTH)
			{
				if (field[y][xi] == chip) line++;
				else if (line >= length) break;
				else line = offset;
			}
		}
		return line >= length;
	}
	
	private boolean checkVertical(int x, int y, int chip, int length, int offset)
	{
		int line = offset;
		if (y <= HEIGHT - length)
		{
			for (int i = 0; i < length; i++)
			{
				if (field[y + i][x] == chip) line++;
			}
		}
		return line >= length;
	}
	
	//Right Up
	private boolean checkSidewaysRight(int x, int y, int chip, int length, int offset)
	{
		int line = offset;
		for (int i = -1 * (length - 1); i < length; i++)
		{
			int xi = x - i;
			int yi = y + i;
			if ((0 <= xi && xi < WIDTH) && (0 <= yi && yi < HEIGHT))
			{
				if (field[yi][xi] == chip) line++;
				else if (line >= length) break;
				else line = offset;
			}
		}
		return line >= length;
	}
	
	//Left Up
	private boolean checkSidewaysLeft(int x, int y, int chip, int length, int offset)
	{
		int line = offset;
		for (int i = -1 * (length - 1); i < length; i++)
		{
			int xi = x + i;
			int yi = y + i;
			if ((0 <= xi && xi < WIDTH) && (0 <= yi && yi < HEIGHT))
			{
				if (field[yi][xi] == chip) line++;
				else if (line >= length) break;
				else line = offset;
			}
		}
		return line >= length;
	}
}
