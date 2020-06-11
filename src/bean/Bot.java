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
	
	public void nextRound(int lastColumn, int lastRow) {
		boolean threeInARow = c.checkInARow(lastColumn, lastRow, 1, 3);
		
		int column = (int)(Math.random() * width);
		
		if (threeInARow)
		{
			int columnPlayerWin = getColumnWhichPlayerWins(lastColumn, lastRow);
			if (columnPlayerWin >= 0) column = columnPlayerWin;
		}
		
		c.setChip(column, 2);
		System.out.println("BOT setzt auf " + column);
	}
	
	
	private int getColumnWhichPlayerWins(int x, int y)
	{
		int[][] field = c.getField();
		
		boolean columnToTheLeft = false;	
		if (x - 1 >= 0)
		{
			for (int i = field.length - 1; i >= 0; i--)
			{
				if (field[x - 1][i] == 0)
				{
					columnToTheLeft = c.checkInARow(x - 1, i, 1, 4); 
					break;
				}
			}
		}			
		boolean thisColumn = (y - 1 >= 0) ? c.checkInARow(x, y - 1, 1, 4) : false;
		boolean columnToTheRight = false;
		if (x + 1 < width)
		{
			for (int i = field.length - 1; i >= 0; i--)
			{
				if (field[x][i] == 0)
				{
					columnToTheLeft = c.checkInARow(x - 1, i, 1, 4); 
					break;
				}
			}
		}
		
		if (columnToTheLeft) return x - 1;
		if (thisColumn) return x;
		if (columnToTheRight) return x + 1;
		
		return -1;
	}
}
