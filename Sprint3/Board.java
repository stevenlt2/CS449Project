public class Board {
	private int[][] grid; //Array for board,
	public int size = 10; //Chosen size, default at 3.
	private char turn = 'B'; //Turn, starts at B (Blue).
	private int gameState = 1; //If 1, game can start.
	private int sosBlue = 0; //SOS count for Blue
	private int sosRed = 0; //SOS count for Red
	private int prevBlue = 0; //Stores previous Blue score for General Game, so that methods can detect an increase in score. 
	private int prevRed = 0; //Stores previous Blue score for General Game, so that methods can detect an increase in score.
	private char winner = 'N'; //B = Blue, R = Red, N = None, D = Draw.
	
	public Board() {
		grid = new int[size][size];
		winner = 'N';
		sosBlue = 0;
		sosRed = 0;
		prevBlue = 0;
		prevRed = 0;
	}
	public Board(int row, int column) {
		size = row;
		size = column;
		winner = 'N';
		sosBlue = 0;
		sosRed = 0;
		prevBlue = 0;
		prevRed = 0;
		if (size < 3) {
			gameState = 0;
		}
		else if (size > 10) {
			gameState = 0;
		}
		grid = new int[size][size];
	}
	public void resizeBoard(int newSize) {
		size = newSize;
		if (size < 3) {
			gameState = 0;
		}
		else if (size > 10) {
			gameState = 0;
		}
		grid = new int[size][size];
		winner = 'N';
		sosBlue = 0;
		sosRed = 0;
		prevBlue = 0;
		prevRed = 0;
	}
	public int getSize() {
		if (size < 3) {
			gameState = 0;
		}
		else if (size > 10) {
			gameState = 0;
		}
		return size;
	}
	public int getCell(int row, int column) {
		return grid[row][column];
	}
	public int getGameState() {
		return gameState;
	}
	public char getTurn() {
		return turn;
	}
	public void setFirstTurn() {
		turn = 'B';
	}
	
	/*	Cell State
	 * 	0 = Empty
	 * 	1 = Occupied by S
	 * 	2 = Occupied by O
	 *  3 = Marked as Occupied to prevent moves after Simple Game Win.
	 */
	
	public void simpleGameEndCheck() { //Checks if an SOS was made.
		if (sosBlue > 0 || sosRed > 0) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (grid[i][j] == 0) { //if any cell is empty, 3 is placed in the cell to prevent any further moves.
						grid[i][j] = 3;
					}
				}
			}
			gameOver();
		}
		else {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (grid[i][j] != 0) { //if all cells turn out to be filled, standard endgame checking will commence.
						gameOver();
					}
				}
			}
		}
	}
	public void gameOver() { //Method used for Simple and General Games.
		int chkdCell = 0;
		int allCells = size * size;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j] != 0) { //if all cells are filled, checks winner. For Simple Game, cells with 3 satisfy this condition.
					chkdCell++;
					allCellsChecked(chkdCell, allCells); //Method to verify all cells have been checked.
				}
			}
		}
	}
	public void allCellsChecked(int chkdCell, int allCells) {
		if (chkdCell == allCells) {
			checkWinner(); //if all cells are checked, then winner will be checked for.
		}
	}
	public void checkWinner() {
		if (sosBlue > sosRed) {
			winner = 'B'; //Blue.
		}
		else if (sosBlue < sosRed) {
			winner = 'R'; //Red.
		}
		else if (sosBlue == sosRed) {
			winner = 'D'; //Draw.
		}
	}
	
	public char getWinner() {
		return winner;
	}
	
	public void makeSMove(int row, int column) {
		int valid = 1;
		if (grid[row][column] == 0) {
			grid[row][column] = 1;
			checkSOS(row, column);
		}
		else {
			valid = 0;
		}
		turnCheck(valid);
	}
	public void makeOMove(int row, int column) {
		int valid = 1;
		if (grid[row][column] == 0) {
			grid[row][column] = 2;
			checkSOS(row, column);
		}
		else {
			valid = 0;
		}
		turnCheck(valid);
	}
	public void turnCheck(int valid) { //Checks whose turn it will be.
		if (turn == 'B' && valid == 1) { //If Blue made a valid move, SOS status will be checked. This will determine if the turn will change.
			checkScoreBlue();
		}
		else if (turn == 'R' && valid == 1) { //If Red made a valid move, SOS status will be checked. This will determine if the turn will change.
			checkScoreRed();
		}
		else if (valid == 0) { //If neither move is valid, nothing happens.
			//Nothing happens.
		}
	}
	public void checkScoreBlue() {
		if (sosBlue > prevBlue) { //If a score is detected, score will not change and previous score count will update. Turn will not change.
			prevBlue = sosBlue;
		}
		else if (sosBlue == prevBlue) { //If a score is not detected, the turn will change.
			turn = 'R';
		}
	}
	public void checkScoreRed() {
		if (sosRed > prevRed) { //If a score is detected, score will not change and previous score count will update. Turn will not change.
			prevRed = sosRed;
		}
		else if (sosRed == prevRed) { //If a score is not detected, the turn will change.
			turn = 'B';
		}
	}
	//Possible additional conditions for S placement, to prevent out of bounds scans of the array.
	// && row > 1 (N)
	// && row > 1 && column < size - 2 (NE)
	// && column < size - 1 (E)
	// && row < size - 2 && column < size - 2 (SE)
	// && row < size - 2 (S)
	// && row < size - 2 && column > 1 (SW)
	// && column > 1 (W)
	// && row > 1 && column > 1 (NW)
	
	//Possible additional conditions for O placement, to prevent out of bounds scans of the array.
	// && row <= size - 1 && column <= size - 1 && row >= 1 && column >= 1 (Diagonals)
	// && 0 < row < size - 1 (Vertical)
	// && 0 < column < size - 1 (Horizontal)

	public void checkSOS(int row, int column) {
		if (grid[row][column] == 1) { //If S is placed, checks in a clockwise way. 
			sCheckN(row, column);
			sCheckNE(row, column);
			sCheckE(row, column);
			sCheckSE(row, column);
			sCheckS(row, column);
			sCheckSW(row, column);
			sCheckW(row, column);
			sCheckNW(row, column);
		}
		else if (grid[row][column] == 2) { //If O is placed, checks for Ses on either side in clockwise way.
			oCheckNnS(row, column);
			oCheckNEnSW(row, column);
			oCheckEnW(row, column);
			oCheckSEnNW(row, column);
		}
	}
	public void sCheckN(int row, int column) {
		if (row > 1) {
			if (grid[row][column] == 1 && grid[row - 1][column] == 2) {
				if (grid[row - 2][column] == 1) {
					if (turn == 'B') {
						sosBlue++;
						sosSignalN++;
					}
					else if (turn == 'R') {
						sosRed++;
						sosSignalN++;
					}
				}
			}
		}
	}
	public void sCheckNE(int row, int column) {
		if (row > 1 && column < size - 2) {
			if (grid[row][column] == 1 && grid[row - 1][column + 1] == 2) {
				if (grid[row - 2][column + 2] == 1) {
					if (turn == 'B') {
						sosBlue++;
						sosSignalNE++;
					}
					else if (turn == 'R') {
						sosRed++;
						sosSignalNE++;
					}
				}
			}
		}
	}
	public void sCheckE(int row, int column) {
		if (column < size - 2 && column >= 0) {
			if (grid[row][column] == 1 && grid[row][column + 1] == 2) {
				if (grid[row][column + 2] == 1) {
					if (turn == 'B') {
						sosBlue++;
						sosSignalE++;
					}
					else if (turn == 'R') {
						sosRed++;
						sosSignalE++;
					}
				}
			}
		}
	}
	public void sCheckSE(int row, int column) {
		if (row < size - 2 && column < size - 2) { 
			if (grid[row][column] == 1 && grid[row + 1][column + 1] == 2) {
				if (grid[row + 2][column + 2] == 1) {
					if (turn == 'B') {
						sosBlue++;
						sosSignalSE++;
					}
					else if (turn == 'R') {
						sosRed++;
						sosSignalSE++;
					}
				}
			}
		}
	}
	public void sCheckS(int row, int column) {
		if (row < size - 2 && row >= 0) {
			if (grid[row][column] == 1 && grid[row + 1][column] == 2) {
				if (grid[row + 2][column] == 1) {
					if (turn == 'B') {
						sosBlue++;
						sosSignalS++;
					}
					else if (turn == 'R') {
						sosRed++;
						sosSignalS++;
					}
				}
			}
		}
	}
	public void sCheckSW(int row, int column) {
		if (row < size - 2 && column > 1) {
			if (grid[row][column] == 1 && grid[row + 1][column - 1] == 2) {
				if (grid[row + 2][column - 2] == 1) {
					if (turn == 'B') {
						sosBlue++;
						sosSignalSW++;
					}
					else if (turn == 'R') {
						sosRed++;
						sosSignalSW++;
					}
				}
			}
		}
	}
	public void sCheckW(int row, int column) {
		if (column > 1 && column <= size - 1) {
			if (grid[row][column] == 1 && grid[row][column - 1] == 2) {
				if (grid[row][column - 2] == 1) {
					if (turn == 'B') {
						sosBlue++;
						sosSignalW++;
					}
					else if (turn == 'R') {
						sosRed++;
						sosSignalW++;
					}
				}
			}
		}
	}
	public void sCheckNW(int row, int column) {
		if (row > 1 && column > 1) {
			if (grid[row][column] == 1 && grid[row - 1][column - 1] == 2) {
				if (grid[row - 2][column - 2] == 1) {
					if (turn == 'B') {
						sosBlue++;
						sosSignalNW++;
					}
					else if (turn == 'R') {
						sosRed++;
						sosSignalNW++;
					}
				}
			}
		}
	}
	public void oCheckNnS(int row, int column){
		if (row > 0 && row < size - 1) {
			if (grid[row - 1][column] == 1 && grid[row][column] == 2 && grid[row + 1][column] == 1) {
				if (turn == 'B') {
					sosBlue++;
					sosSignalNnS++;
				}
				else if (turn == 'R') {
					sosRed++;
					sosSignalNnS++;
				}
			}
		}
	}
	public void oCheckNEnSW(int row, int column){
		if (row < size - 1 && column < size - 1 && row > 0 && column > 0) {
			if (grid[row + 1][column - 1] == 1 && grid[row][column] == 2 && grid[row - 1][column + 1] == 1) {
				if (turn == 'B') {
					sosBlue++;
					sosSignalNEnSW++;
				}
				else if (turn == 'R') {
					sosRed++;
					sosSignalNEnSW++;
				}
			}
		}
	}
	public void oCheckEnW(int row, int column){
		if (column > 0 && column < size - 1) {
			if (grid[row][column - 1] == 1 && grid[row][column] == 2 && grid[row][column + 1] == 1) {
				if (turn == 'B') {
					sosBlue++;
					sosSignalEnW++;
				}
				else if (turn == 'R') {
					sosRed++;
					sosSignalEnW++;
				}
			}
		}
	}
	public void oCheckSEnNW(int row, int column){
		if (row < size - 1 && column < size - 1 && row > 0 && column > 0) {
			if (grid[row + 1][column + 1] == 1 && grid[row][column] == 2 && grid[row - 1][column - 1] == 1) {
				if (turn == 'B') {
					sosBlue++;
					sosSignalSEnNW++;
				}
				else if (turn == 'R') {
					sosRed++;
					sosSignalSEnNW++;
				}
			}
		}
	}
	public int getBlueScore() {
		return sosBlue;
	}
	public int getRedScore() {
		return sosRed;
	}
	
	//Collects numbers of SOS types to be colored on GUI.
	int sosSignalN = 0;
	int sosSignalNE = 0;
	int sosSignalE = 0;
	int sosSignalSE = 0;
	int sosSignalS = 0;
	int sosSignalSW = 0;
	int sosSignalW = 0;
	int sosSignalNW = 0;
	int sosSignalNnS = 0;
	int sosSignalNEnSW = 0;
	int sosSignalEnW = 0;
	int sosSignalSEnNW = 0;
	
	public void resetSignals() {
		sosSignalN = 0;
		sosSignalNE = 0;
		sosSignalE = 0;
		sosSignalSE = 0;
		sosSignalS = 0;
		sosSignalSW = 0;
		sosSignalW = 0;
		sosSignalNW = 0;
		sosSignalNnS = 0;
		sosSignalNEnSW = 0;
		sosSignalEnW = 0;
		sosSignalSEnNW = 0;
	}
	public int getSOSsignalN() {
		return sosSignalN;
	}
	public int getSOSsignalNE() {
		return sosSignalNE;
	}
	public int getSOSsignalE() {
		return sosSignalE;
	}
	public int getSOSsignalSE() {
		return sosSignalSE;
	}
	public int getSOSsignalS() {
		return sosSignalS;
	}
	public int getSOSsignalSW() {
		return sosSignalSW;
	}
	public int getSOSsignalW() {
		return sosSignalW;
	}
	public int getSOSsignalNW() {
		return sosSignalNW;
	}
	public int getSOSsignalNnS() {
		return sosSignalNnS;
	}
	public int getSOSsignalNEnSW() {
		return sosSignalNEnSW;
	}
	public int getSOSsignalEnW() {
		return sosSignalEnW;
	}
	public int getSOSsignalSEnNW() {
		return sosSignalSEnNW;
	}
}
