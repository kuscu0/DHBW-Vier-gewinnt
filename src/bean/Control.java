package bean;

public class Control {
	private static final int WIDTH = 7;
	private static final int HEIGHT = 6;

	private int[][] field = new int[HEIGHT][WIDTH];

	private int activePlayer = 1;
	private int round = 0;
	private int playerWon = 0;

	public Control() {
		fillField(0);
	}


	public void nextRound() {
		if (activePlayer == 1) activePlayer = 2;
		else activePlayer = 1;
	}

	public boolean setChip(int column) {
		round++;

		int chip = activePlayer;

		if (round <= WIDTH * HEIGHT && playerWon == 0) {
			for (int row = HEIGHT - 1; row >= 0; row--) {
				if (field[row][column] == 0) {
					field[row][column] = chip;
					return checkGewonnen(column, row, chip);
				}
			}
		}

		return false;
	}


	public int[][] getField() {
		return field;
	}

	public int getPlayerWon() {
		return playerWon;
	}

	public String getPlayerWonToString() {
		StringBuilder sb = new StringBuilder();
		if (playerWon == 0) sb.append("Nobody ");
		else sb.append("Player " + playerWon + " ");
		sb.append("won the game.");
		return sb.toString();
	}

	private void fillField(int i) {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				field[y][x] = i;
			}
		}
	}


	private boolean checkGewonnen(int x, int y, int chip) {
		boolean vertical = checkVertical(x, y, chip);
		boolean horizontal = checkHorizontal(x, y, chip);
		boolean sidewaysRight = checkSidewaysRight(x, y, chip);
		boolean sidewaysLeft = checkSidewaysLeft(x, y, chip);
		boolean finished = horizontal || vertical || sidewaysLeft || sidewaysRight;

		if (finished) playerWon = chip;

		return finished;
	}

	private boolean checkHorizontal(int x, int y, int chip) {
		if (round == 5)
			round = 5;
		int line = 0;
		for (int i = -3; i < 4; i++) {
			int xi = x + i;
			if (0 <= xi && xi < WIDTH) {
				if (field[y][xi] == chip) line++;
				else if (line >= 4) break;
				else line = 0;
			}
		}
		return line >= 4;
	}

	private boolean checkVertical(int x, int y, int chip) {
		int line = 0;
		if (y <= 2) {
			for (int i = 0; i < 4; i++) {
				if (field[y + i][x] == chip) line++;
			}
		}
		return line >= 4;
	}

	//Right Up
	private boolean checkSidewaysRight(int x, int y, int chip) {
		int line = 0;
		for (int i = -3; i < 4; i++) {
			int xi = x - i;
			int yi = y + i;
			if ((0 <= xi && xi < WIDTH) && (0 <= yi && yi < HEIGHT)) {
				if (field[yi][xi] == chip) line++;
				else if (line >= 4) break;
				else line = 0;
			}
		}
		return line >= 4;
	}

	//Left Up
	private boolean checkSidewaysLeft(int x, int y, int chip) {
		int line = 0;
		for (int i = -3; i < 4; i++) {
			int xi = x + i;
			int yi = y + i;
			if ((0 <= xi && xi < WIDTH) && (0 <= yi && yi < HEIGHT)) {
				if (field[yi][xi] == chip) line++;
				else if (line >= 4) break;
				else line = 0;
			}
		}
		return line >= 4;
	}
}