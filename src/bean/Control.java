package bean;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import bean.Control;
import servlet.RoundType;


/**
 * @author pascalsimon
 *
 */
public class Control implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 7;
	private static final int HEIGHT = 6;
		
	private int[][] field = new int[HEIGHT][WIDTH];
	
	private boolean matchFull = true;
	private int activePlayer = 1;
	private int round = 0;
	private int playerWon = 0;
	private int lastColumn, lastRow;
	
	private Bot bot;
	private RoundType roundType;
	
	

	/**
	 * This function needs to be called when starting an round
	 * 
	 * @param roundType The type of the Round
	 */
	public void newRound(RoundType roundType)
	{
		clearField();
		
		if (roundType == RoundType.BOT) bot = new Bot(this, WIDTH);
		else if (roundType == RoundType.ONLINE) matchFull = false;
			
		this.roundType = roundType;
		activePlayer = 1;
		round = 0;
		playerWon = 0;
	}
	
	/**
	 * This Method needs to be called after setting an Chip
	 */
	public void nextRound() 
	{
		if (!matchFull) return;
		
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

	
	/**
	 * This function sets an Chip on the field. Needs to call nextRound() afterwards.
	 * 
	 * @param column The Coulum in which the chip should be placed
	 * @return If the Chip could be set in this Column
	 */
	public boolean setChip(int column)
	{	
		if (!matchFull) return false;
		
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
	
	
	/**
	 * @return The field.
	 */
	public int[][] getField()
	{
		return field;
	}
	
	
	/**
	 * @return The player which has won. 0 if the game is still active.
	 */
	public int getPlayerWon()
	{
		return playerWon;
	}
	
	
	/**
	 * @return The type of the Match
	 */
	public RoundType getRoundType()
	{
		return roundType;
	}
	
	/**
	 * @return The layer which won as an String.
	 */
	public String getPlayerWonToString()
	{
		StringBuilder sb = new StringBuilder();
		if (playerWon == 0) sb.append("Nobody ");
		else if (bot == null || playerWon == 1) sb.append("Player " + playerWon + " ");
		else sb.append("The Bot ");
		sb.append("won the game.");
		return sb.toString();
	}
	
	/**
	 * Sets the filed on default.
	 */
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
	
	
	/**
	 * @return if the Player which of this round has won.
	 */
	public boolean checkGewonnen()
	{
		int chipOfLastPosition = field[lastRow][lastColumn];
		return checkGewonnen(lastColumn, lastRow, chipOfLastPosition);
	}
	
	
	/**
	 * @param x Column
	 * @param y Row
	 * @param chip Chipcolor of Player
	 * @return If the Player with this Chip Won on the given Position.
	 */
	private boolean checkGewonnen(int x, int y, int chip)
	{
		boolean finished = checkInARow(x, y, chip, 4);
		
		if (finished) playerWon = chip;
		
		return finished;
	}
	
	
	/**
	 * @param x Column
	 * @param y Row
	 * @param chip Chipcolor of Player
	 * @param length The length of chips in a Row, after which should be checked
	 * @return If there is a row with the given length
	 */
	private boolean checkInARow(int x, int y, int chip, int length)
	{
		return checkInARowWithOffset(x, y, chip, length, 0);
	}
	
	
	/**
	 * @param x Column
	 * @param y Row
	 * @param chip Chipcolor of Player
	 * @param length The length of chips in a Row, after which should be checked
	 * @param offset An offset added to the real length of the row.
	 * @return If there is a Row with the given length and a offset.
	 */
	private boolean checkInARowWithOffset(int x, int y, int chip, int length, int offset)
	{
		if (!(0 <= y && y < HEIGHT) || !(0 <= x && x < WIDTH)) return false;
		
		boolean vertical = checkVertical(x, y, chip, length, offset);
		boolean horizontal = checkHorizontal(x, y, chip, length, offset);
		boolean sidewaysRight = checkSidewaysRight(x, y, chip, length, offset);
		boolean sidewaysLeft = checkSidewaysLeft(x, y, chip, length, offset);	
		
		return horizontal || vertical || sidewaysLeft || sidewaysRight;
	}
	
	/**
	 * @param x Column
	 * @param y Row
	 * @param chip Chipcolor of Player
	 * @param length The length of chips in a Row, after which should be checked
	 * @param offset An offset added to the real length of the row.
	 * @return If there is a Horizontal Row with the given length and a offset.
	 */
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
	
	/**
	 * @param x Column
	 * @param y Row
	 * @param chip Chipcolor of Player
	 * @param length The length of chips in a Row, after which should be checked
	 * @param offset An offset added to the real length of the row.
	 * @return If there is a Vertical Row with the given length and a offset.
	 */
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
	
	/**
	 * This Method checks after Rows from DownLeft to UpRight
	 * @param x Column
	 * @param y Row
	 * @param chip Chipcolor of Player
	 * @param length The length of chips in a Row, after which should be checked
	 * @param offset An offset added to the real length of the row.
	 * @return If there is a Diagonal Row with the given length and a offset.
	 */
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
	
	/**
	 * This Method checks after Rows from UpLeft to DownRight
	 * @param x Column 
	 * @param y Row
	 * @param chip Chipcolor of Player
	 * @param length The length of chips in a Row, after which should be checked
	 * @param offset An offset added to the real length of the row.
	 * @return If there is a Diagonal Row with the given length and a offset.
	 */
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
	
	
	/**
	 * This Method has to be called after nextRound().
	 * 
	 * @param session The Session which should be refreshed.
	 */
	public void setRefresh(HttpSession session) 
	{
		session.setAttribute(Constants.ATTRIBUTE_FIELD, field);
		session.setAttribute(Constants.ATTRIBUTE_ACTIVE_PLAYER, activePlayer);
		session.setAttribute(Constants.ATTRIBUTE_ROUND, round);
		session.setAttribute(Constants.ATTRIBUTE_PLAYER_WON, playerWon);
		session.setAttribute(Constants.ATTRIBUTE_LAST_COLUMN, lastColumn);
		session.setAttribute(Constants.ATTRIBUTE_LAST_ROW, lastRow);
		session.setAttribute(Constants.ATTRIBUTE_ROUND_TYPE, roundType);
	}
	
	
	/**
	 *  This Method has to be called before setChip().
	 * 
	 * @param session The session which the Match is played in.
	 */
	public void getRefresh(HttpSession session) 
	{
		field = (int[][]) session.getAttribute(Constants.ATTRIBUTE_FIELD);
		activePlayer = (int) session.getAttribute(Constants.ATTRIBUTE_ACTIVE_PLAYER);
		round = (int) session.getAttribute(Constants.ATTRIBUTE_ROUND);
		playerWon = (int) session.getAttribute(Constants.ATTRIBUTE_PLAYER_WON);
		lastColumn = (int) session.getAttribute(Constants.ATTRIBUTE_LAST_COLUMN);
		lastRow = (int) session.getAttribute(Constants.ATTRIBUTE_LAST_ROW);
		roundType = (RoundType) session.getAttribute(Constants.ATTRIBUTE_ROUND_TYPE);
	}

	/**
	 * @author pascalsimon
	 *
	 * The Bot which the player plays against.
	 */
	public class Bot 
	{
		private Control c;
		private int width;
		
		public Bot(Control control, int widthOfField)
		{
			c = control;
			width = widthOfField;
		}
		
		/**
		 * This Method let the Bot play a Rpound and sets his chip.
		 * 
		 * @param lastColumn The Last Column of the Player before
		 * @param lastRow The Last Row of the Player before
		 */
		public void nextRound(int lastColumn, int lastRow) 
		{	
			boolean threeInARow = c.checkInARow(lastColumn, lastRow, 1, 3);
			
			int column = (int)(Math.random() * width);
			
			if (threeInARow)
			{
				int columnPlayerWin = getColumnWhichPlayerWins(lastColumn, lastRow);
				if (columnPlayerWin >= 0) column = columnPlayerWin;
			}
			
			if (!c.setChip(column)) nextRound(lastColumn, lastRow);
			else c.nextRound();
		}
		
		
		/**
		 * This Method calculates the Column which the Bpt has to set his chip, so the player wont win.
		 * 
		 * @param x Column
		 * @param y Row
		 * @return The Column which the bot has to set a chip or -1
		 */
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
				return x - 1;
			}
			if (thisColumn) 
			{
				return x;
			}
			if (columnToTheRight) 
			{
				return x + 1;
			}
			
			return -1;
		}
	}
}
