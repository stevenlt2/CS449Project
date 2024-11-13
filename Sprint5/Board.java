import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;

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
		gameState = 1;
		grid = new int[size][size];
		winner = 'N';
		sosBlue = 0;
		sosRed = 0;
		prevBlue = 0;
		prevRed = 0;
	}
	public Board(int row, int column) {
		gameState = 1;
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
		gameState = 1;
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
			gameState = 0;
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
			gameState = 0;
			winner = 'B'; //Blue.
		}
		else if (sosBlue < sosRed) {
			gameState = 0;
			winner = 'R'; //Red.
		}
		else if (sosBlue == sosRed) {
			gameState = 0;
			winner = 'D'; //Draw.
		}
	}
	
	public char getWinner() {
		return winner;
	}
	
	public void makeSMove(int row, int column) {
		int valid = 1;
		if (grid[row][column] == 0 && gameState == 1) {
			grid[row][column] = 1;
			checkSOS(row, column);
			if (recordSignal == 1) {
				recMoveCnt++;
				record(row, column, 'S');
			}
		}
		else {
			valid = 0;
		}
		turnCheck(valid);
	}
	public void makeOMove(int row, int column) {
		int valid = 1;
		if (grid[row][column] == 0 && gameState == 1) {
			grid[row][column] = 2;
			checkSOS(row, column);
			if (recordSignal == 1) {
				recMoveCnt++;
				record(row, column, 'O');
			}
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
			turn = 'B';
		}
		else if (sosBlue == prevBlue) { //If a score is not detected, the turn will change.
			turn = 'R';
		}
	}
	public void checkScoreRed() {
		if (sosRed > prevRed) { //If a score is detected, score will not change and previous score count will update. Turn will not change.
			prevRed = sosRed;
			turn = 'R';
		}
		else if (sosRed == prevRed) { //If a score is not detected, the turn will change.
			turn = 'B';
		}
	}
	
	int blueCPURow, blueCPUCol, redCPURow, redCPUCol;
	public int getBlueCPURow() {
		return blueCPURow;
	}
	public int getBlueCPUCol() {
		return blueCPUCol;
	}
	public int getRedCPURow() {
		return redCPURow;
	}
	public int getRedCPUCol() {
		return redCPUCol;
	}
	
	char blueCPUTurn = 'B';
	char redCPUTurn = 'R';
	
	public void blueCPU() {
		//blue CPU behavior will go here.
		blueCPUTurn = 'B';
		//randomSignal = 0;
		if (turn == blueCPUTurn && getWinner() == 'N' && gameState == 1) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					//Search for any SOS opportunities, the following checks all possible scenarios when an SOS can be made.
						searchN(i, j, blueCPUTurn);
						searchNE(i, j, blueCPUTurn);
						searchE(i, j, blueCPUTurn);
						searchSE(i, j, blueCPUTurn);
						searchS(i, j, blueCPUTurn);
						searchSW(i, j, blueCPUTurn);
						searchW(i, j, blueCPUTurn);
						searchNW(i, j, blueCPUTurn);
						searchNnS(i, j, blueCPUTurn);
						searchNEnSW(i, j, blueCPUTurn);
						searchEnW(i, j, blueCPUTurn);
						searchSEnNW(i, j, blueCPUTurn);	
				}
			}
			makeRandomMove(blueCPUTurn);
		}
	}	
	
	public void redCPU() {
		//Red CPU behavior will go here.
		redCPUTurn = 'R';
		//randomSignal = 0;
		if (turn == redCPUTurn && getWinner() == 'N' && gameState == 1) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					//Search for any SOS opportunities, the following checks all possible scenarios when an SOS can be made.
						searchN(i, j, redCPUTurn);
						searchNE(i, j, redCPUTurn);
						searchE(i, j, redCPUTurn);
						searchSE(i, j, redCPUTurn);
						searchS(i, j, redCPUTurn);
						searchSW(i, j, redCPUTurn);
						searchW(i, j, redCPUTurn);
						searchNW(i, j, redCPUTurn);
						searchNnS(i, j, redCPUTurn);
						searchNEnSW(i, j, redCPUTurn);
						searchEnW(i, j, redCPUTurn);
						searchSEnNW(i, j, redCPUTurn);	
				}
			}
			makeRandomMove(redCPUTurn);
		}
	}
	
	Random randR = new Random(); //Random Row
	Random randC = new Random(); //Random Column
	Random randMove = new Random(); //Random Move Type
	
	public void makeRandomMove(char cpuTurn) {
		int randRow = randR.nextInt(size);
		int randCol = randC.nextInt(size);
		int moveType = randMove.nextInt(1);
		if (getCell(randRow, randCol) == 0 && gameState == 1 && turn == cpuTurn) {
			if (moveType == 0) {
				makeSMove(randRow, randCol);
				redCPUTurn = 'N';
				blueCPUTurn = 'N';
				//randomSignal = 1;
			}
			else if (moveType == 1) {
				makeOMove(randRow, randCol);
				redCPUTurn = 'N';
				blueCPUTurn = 'N';
				//randomSignal = 1;
			}
		}
		else if (getCell(randRow, randCol) != 0 && turn == cpuTurn) {
			makeRandomMove(cpuTurn);
		}
	}
	
	//Search Methods
	public void searchN(int row, int column, char cpuTurn) {
		if (row > 1 && turn == cpuTurn) {
			if (grid[row][column] == 1 && grid[row - 1][column] == 2) {
				if (grid[row - 2][column] == 0) {
					makeSMove(row - 2, column);
					if (cpuTurn == 'R') {
						redCPURow = row - 2;
						redCPUCol = column;
						redCPUTurn = 'N';
					}
					if (cpuTurn == 'B') {
						blueCPURow = row - 2;
						blueCPUCol = column;
						blueCPUTurn = 'N';
					}
				}
			}
		}
	}
	public void searchNE(int row, int column, char cpuTurn) {
		if (row > 1 && column < size - 2 && turn == cpuTurn) {
			if (grid[row][column] == 1 && grid[row - 1][column + 1] == 2) {
				if (grid[row - 2][column + 2] == 0) {
					makeSMove(row - 2, column + 2);
					if (cpuTurn == 'R') {
						redCPURow = row - 2;
						redCPUCol = column + 2;
						redCPUTurn = 'N';
					}
					if (cpuTurn == 'B') {
						blueCPURow = row - 2;
						blueCPUCol = column + 2;
						blueCPUTurn = 'N';
					}
				}
				
			}
		}
	}
	public void searchE(int row, int column, char cpuTurn) {
		if (column < size - 2 && column >= 0 && turn == cpuTurn) {
			if (grid[row][column] == 1 && grid[row][column + 1] == 2) {
				if (grid[row][column + 2] == 0) {
					makeSMove(row, column + 2);
					if (cpuTurn == 'R') {
						redCPURow = row;
						redCPUCol = column + 2;
						redCPUTurn = 'N';
					}
					if (cpuTurn == 'B') {
						blueCPURow = row;
						blueCPUCol = column + 2;
						blueCPUTurn = 'N';
					}
				}
			}
		}
	}
	public void searchSE(int row, int column, char cpuTurn) {
		if (row < size - 2 && column < size - 2 && turn == cpuTurn) {
			if (grid[row][column] == 1 && grid[row + 1][column + 1] == 2) {
				if (grid[row + 2][column + 2] == 0) {
					makeSMove(row + 2, column + 2);
					if (cpuTurn == 'R') {
						redCPURow = row + 2;
						redCPUCol = column + 2;
						redCPUTurn = 'N';
					}
					if (cpuTurn == 'B') {
						blueCPURow = row + 2;
						blueCPUCol = column + 2;
						blueCPUTurn = 'N';
					}
				}
			}
		}
	}
	public void searchS(int row, int column, char cpuTurn) {
		if (row < size - 2 && row >= 0 && turn == cpuTurn) {
			if (grid[row][column] == 1 && grid[row + 1][column] == 2) {
				if (grid[row + 2][column] == 0) {
					makeSMove(row + 2, column);
					if (cpuTurn == 'R') {
						redCPURow = row + 2;
						redCPUCol = column;
						redCPUTurn = 'N';
					}
					if (cpuTurn == 'B') {
						blueCPURow = row + 2;
						blueCPUCol = column;
						blueCPUTurn = 'N';
					}
				}
			}
		}
	}
	public void searchSW(int row, int column, char cpuTurn) {
		if (row < size - 2 && column > 1 && turn == cpuTurn) {
			if (grid[row][column] == 1 && grid[row + 1][column - 1] == 2) {
				if (grid[row + 2][column - 2] == 0) {
					makeSMove(row + 2, column - 2);
					if (cpuTurn == 'R') {
						redCPURow = row + 2;
						redCPUCol = column - 2;
						redCPUTurn = 'N';
					}
					if (cpuTurn == 'B') {
						blueCPURow = row + 2;
						blueCPUCol = column - 2;
						blueCPUTurn = 'N';
					}
				}
			}
		}
	}
	public void searchW(int row, int column, char cpuTurn) {
		if (column > 1 && column <= size - 1 && turn == cpuTurn) {
			if (grid[row][column] == 1 && grid[row][column - 1] == 2) {
				if (grid[row][column - 2] == 0) {
					makeSMove(row, column - 2);
					if (cpuTurn == 'R') {
						redCPURow = row;
						redCPUCol = column - 2;
						redCPUTurn = 'N';
					}
					if (cpuTurn == 'B') {
						blueCPURow = row;
						blueCPUCol = column - 2;
						blueCPUTurn = 'N';
					}
				}
			}
		}
	}
	public void searchNW(int row, int column, char cpuTurn) {
		if (column > 1 && row > 1 && turn == cpuTurn) {
			if (grid[row][column] == 1 && grid[row - 1][column - 1] == 2) {
				if (grid[row - 2][column - 2] == 0) {
					makeSMove(row - 2, column - 2);
					if (cpuTurn == 'R') {
						redCPURow = row - 2;
						redCPUCol = column - 2;
						redCPUTurn = 'N';
					}
					if (cpuTurn == 'B') {
						blueCPURow = row - 2;
						blueCPUCol = column - 2;
						blueCPUTurn = 'N';
					}
				}
			}
		}
	}
	public void searchNnS(int row, int column, char cpuTurn) {
		if (row > 0 && row < size - 1 && turn == cpuTurn) {
			if (grid[row - 1][column] == 1 && grid[row][column] == 0 && grid[row + 1][column] == 1) {
				makeOMove(row, column);
				if (cpuTurn == 'R') {
					redCPURow = row;
					redCPUCol = column;
					redCPUTurn = 'N';
				}
				if (cpuTurn == 'B') {
					blueCPURow = row;
					blueCPUCol = column;
					blueCPUTurn = 'N';
				}
			}
		}
	}
	public void searchNEnSW(int row, int column, char cpuTurn) {
		if (row < size - 1 && column < size - 1 && row > 0 && column > 0 && turn == cpuTurn) {
			if (grid[row + 1][column - 1] == 1 && grid[row][column] == 0 && grid[row - 1][column + 1] == 1) {
				makeOMove(row, column);
				if (cpuTurn == 'R') {
					redCPURow = row;
					redCPUCol = column;
					redCPUTurn = 'N';
				}
				if (cpuTurn == 'B') {
					blueCPURow = row;
					blueCPUCol = column;
					blueCPUTurn = 'N';
				}
			}
		}
	}
	public void searchEnW(int row, int column, char cpuTurn) {
		if (column > 0 && column < size - 1 && turn == cpuTurn) {
			if (grid[row][column - 1] == 1 && grid[row][column] == 0 && grid[row][column + 1] == 1) {
				makeOMove(row, column);
				if (cpuTurn == 'R') {
					redCPURow = row;
					redCPUCol = column;
					redCPUTurn = 'N';
				}
				if (cpuTurn == 'B') {
					blueCPURow = row;
					blueCPUCol = column;
					blueCPUTurn = 'N';
				}
			}
		}
	}
	public void searchSEnNW(int row, int column, char cpuTurn) {
		if (row < size - 1 && column < size - 1 && row > 0 && column > 0 && turn == cpuTurn) {
			if (grid[row + 1][column + 1] == 1 && grid[row][column] == 0 && grid[row - 1][column - 1] == 1) {
				makeOMove(row, column);
				if (cpuTurn == 'R') {
					redCPURow = row;
					redCPUCol = column;
					redCPUTurn = 'N';
				}
				if (cpuTurn == 'B') {
					blueCPURow = row;
					blueCPUCol = column;
					blueCPUTurn = 'N';
				}
			}
		}
	}
	
	//Possible additional conditions for S placement, to prevent out of bounds scans of the array.
	// && row > 1 (N)
	// && row > 1 && column < size - 2 (NE)
	// && column < size - 2 (E)
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

	//Everything required for Record/Replay.
	Stack<Integer> recRow = new Stack<Integer>(); 
	Stack<Integer> recCol = new Stack<Integer>();
	Stack<Character> recType = new Stack<Character>();
	Stack<Integer> repRow = new Stack<Integer>(); 
	Stack<Integer> repCol = new Stack<Integer>();
	Stack<Character> repType = new Stack<Character>();
	int recordSignal = 0; //Affected by GUI.
	int recMoveCnt = 0; //Increment after each move, regardless of type.

	void setRecordSignal(){
		recordSignal = 1;
	}
	int getRecordSignal() {
		return recordSignal;
	}
	void resetRecordSignal() {
		recordSignal = 0;
	}
	int getMoveCount() {
		return recMoveCnt;
	}

	int storedBoardSize;
	
	//Called during each move if the record signal is on (1).
	void record(int row, int col, char type) {
		storedBoardSize = size;
		recRow.push(row);
		recCol.push(col);
		recType.push(type);
		System.out.print("Move: " + recRow.peek() + "," + recCol.peek() + " -  Placed an " + recType.peek() + "\n");
	}

	void recordFile() {
		try {
			BufferedWriter recordTextFile = new BufferedWriter(new FileWriter("recordingDoc.txt"));
			for (int i = 0; i < recMoveCnt; i++) {
				recordTextFile.write("Move: " + recRow.get(i) + "," + recCol.get(i) + " -  Placed an " + recType.get(i) + "\n");
			}
			recordTextFile.write("Game Result: " + winner);
			recordTextFile.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	int storedRecordingSignal = 0;
	
	void endRecording() {
		//If the game is over, then recorded moves are placed in another pair of stacks, so that replay will make the exact same moves in the exact same order.
		if (winner != 'N' && checkRecStackEmpty() != true) { //This part is added in for extra security, so that this doesn't end the recording too early.
			recordFile();
			for (int i = 0; i < recMoveCnt; i++) {
				repRow.push(recRow.pop());
				repCol.push(recCol.pop());
				repType.push(recType.pop());
				storedRecordingSignal = 1;
				if (checkRecStackEmpty() == true) {
					if (winner == 'B') {
						System.out.print("The winner is Player Blue!\n");
					}
					else if (winner == 'R') {
						System.out.print("The winner is Player Red!\n");
					}
					else if (winner == 'D') {
						System.out.print("This was a Draw!\n");
					}
				}
			}
			storedCnt = recMoveCnt;
			recMoveCnt = 0;
		}
	}
	
	int storedCnt = 0;
	
	void deleteRecording() {
		//System.out.print("Move: " + repRow.peek() + "," + repCol.peek() + " -  Placed an " + repType.peek() + "\n");
			for (int i = 0; i < storedCnt; i++) {
				repRow.pop();
				repCol.pop();
				repType.pop();
				storedRecordingSignal = 0;
			}
			storedCnt = 0;
	}
	
	boolean checkRecEmpty() {
		if (repRow.empty() == true && repCol.empty() == true && repType.empty() == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	void resetRecording() {
		for (int i = 0; i < recMoveCnt; i++) {
			recRow.pop();
			recCol.pop();
			recType.pop();
			storedRecordingSignal = 0;
		}
		recMoveCnt = 0;
	}
	
	boolean checkRecStackEmpty() {
		if (recRow.empty() == true && recCol.empty() == true && recType.empty() == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	int getStoredBoardSize() {
		return storedBoardSize;
	}
	
	//This is called repeatedly by the GUI, which will automatically know whose turn it is. 
	//The recording is basically like a game played by a CPU player playing for both players, using notes (data within the stacks) taken from a prior game.
	void replay() {
		if (checkRecEmpty() != true) {
			//System.out.print("Move: " + repRow.peek() + "," + repCol.peek() + " -  Placed an " + repType.peek() + "\n");
			char recordedMove = repType.pop();
			if (recordedMove == 'S') {
				makeSMove(repRow.pop(), repCol.pop());
			}
			else if (recordedMove == 'O') {
				makeOMove(repRow.pop(), repCol.pop());
			}
		}
	}
}
