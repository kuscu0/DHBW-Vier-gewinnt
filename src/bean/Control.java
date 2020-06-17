package bean;

import javax.servlet.http.HttpSession;

import bean.Control;

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
		clearField();
		if (playAgainstBot) bot = new Bot(this, WIDTH);
			
		activePlayer = 1;
		round = 0;
		playerWon = 0;
	}
	
	public void nextRound() 
	{
		if (activePlayer == 1)
		{
			activePlayer = 2;
			if (bot != null && checkGewonnen() == false) 
			{
				bot.nextRound(lastColumn, lastRow);
			}
		} 
		else 
		{
			activePlayer = 1;
		}
	}

	public boolean setChip(int column)
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
					return true;
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
	
	private void clearField()
	{
		for(int y = 0; y < HEIGHT; y++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				field[y][x] = 0;
			}
		}
	}
	
	
	public boolean checkGewonnen()
	{
		int chipOfLastPosition = field[lastRow][lastColumn];
		return checkGewonnen(lastColumn, lastRow, chipOfLastPosition);
	}
	
	
	private boolean checkGewonnen(int x, int y, int chip)
	{
		boolean finished = checkInARow(x, y, chip, 4);
		
		if (finished) playerWon = chip;
		
		return finished;
	}
	
	
	private boolean checkInARow(int x, int y, int chip, int length)
	{
		return checkInARowWithOffset(x, y, chip, length, 0);
	}
	
	
	private boolean checkInARowWithOffset(int x, int y, int chip, int length, int offset)
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
	
	public void setRefresh(HttpSession session) {
		session.setAttribute("field", field);
		session.setAttribute("activePlayer", activePlayer);
		session.setAttribute("round", round);
		session.setAttribute("playerWon", playerWon);
		session.setAttribute("lastColumn", lastColumn);
		session.setAttribute("lastRow", lastRow);
	}
	
	public void getRefresh(HttpSession session) {
		field = (int[][]) session.getAttribute("field");
		activePlayer = (int) session.getAttribute("activePlayer");
		round = (int) session.getAttribute("round");
		playerWon = (int) session.getAttribute("playerWon");
		lastColumn = (int) session.getAttribute("lastColumn");
		lastRow = (int) session.getAttribute("lastRow");
	}

	//
	//The Bot
	//
	public class Bot 
	{
		private Control c;
		private int width;
		
		public Bot(Control control, int widthOfField)
		{
			c = control;
			width = widthOfField;
		}
		
		public void nextRound(int lastColumn, int lastRow) 
		{	
			boolean threeInARow = c.checkInARow(lastColumn, lastRow, 1, 3);
			
			int column = (int)(Math.random() * width);
			
			if (threeInARow)
			{
//				System.out.println("Three in a Row");
				int columnPlayerWin = getColumnWhichPlayerWins(lastColumn, lastRow);
				if (columnPlayerWin >= 0) column = columnPlayerWin;
			}
			
			if (!c.setChip(column)) nextRound(lastColumn, lastRow);
			else c.nextRound();
		}
		
		
		private int getColumnWhichPlayerWins(int x, int y)
		{
			int[][] field = c.getField();
			int height = field.length;
			int chip = 1;
			int offset = 1;
			
			boolean columnToTheLeft = false;	
			if (x - 1 >= 0)
			{
				for (int i = height - 1; i >= 0; i--)
				{
					if (i == 0)
					{
						columnToTheLeft = c.checkInARowWithOffset(x - 1, i, chip, 4, offset);
						break;
					}
				}
			}			
			boolean thisColumn = (y - 1 >= 0) ? c.checkInARowWithOffset(x, y - 1, chip, 4, offset) : false;
			boolean columnToTheRight = false;
			if (x + 1 < width)
			{
				for (int i = height - 1; i >= 0; i--)
				{
					if (field[i][x + 1] == 0)
					{
						columnToTheRight = c.checkInARowWithOffset(x + 1, i, chip, 4, offset); 
						break;
					}
				}
			}
			
			if (columnToTheLeft) 
			{
//				System.out.println("Left: " + (x - 1));
				return x - 1;
			}
			if (thisColumn) 
			{
//				System.out.println("This: " + (x));
				return x;
			}
			if (columnToTheRight) 
			{
//				System.out.println("Right: " + (x + 1));
				return x + 1;
			}
			
//			System.out.println("Nothing");
			return -1;
		}
	}
}
