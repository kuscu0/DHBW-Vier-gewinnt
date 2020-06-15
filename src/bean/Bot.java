package bean;

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
//		System.out.println("Bot Next Round, last PlayerPosition x = " + lastColumn + " y=" + lastRow);
		boolean threeInARow = c.checkInARow(lastColumn, lastRow, 1, 3);
		
		int column = (int)(Math.random() * width);
		
		if (threeInARow)
		{
//			System.out.println("Three in a Row");
			int columnPlayerWin = getColumnWhichPlayerWins(lastColumn, lastRow);
			if (columnPlayerWin >= 0) column = columnPlayerWin;
		}
		
		c.setChip(column, 2);
		System.out.println("BOT setzt auf " + column);
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
//			System.out.println("Left: " + (x - 1));
			return x - 1;
		}
		if (thisColumn) 
		{
//			System.out.println("This: " + (x));
			return x;
		}
		if (columnToTheRight) 
		{
//			System.out.println("Right: " + (x + 1));
			return x + 1;
		}
		
//		System.out.println("Nothing");
		return -1;
	}
}
