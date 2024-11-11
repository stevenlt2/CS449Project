import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BoardTest {
	int size = 10;
	private Board board = new Board(size, size);
	@Test
	public void testBoard() {
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				assertEquals("", board.getCell(row, col), 0);
			}
		}
		assertEquals("", board.getGameState(), 1);
		assertEquals("", board.getTurn(),'B');
	}
	@Test
	public void testValidSize() {
		board.resizeBoard(5);
		assertEquals("", board.getGameState(), 1);
	}
	@Test
	public void testInvalidSizeLow() {
		board.resizeBoard(2);
		assertEquals("", board.getGameState(), 0);
	}
	@Test
	public void testInvalidSizeHigh() {
		board.resizeBoard(11);
		assertEquals("", board.getGameState(), 0);
	}
	@Test
	public void testFirstTurn() {
		board.setFirstTurn();
		assertEquals("", board.getTurn(), 'B');
	}
	@Test
	public void testResizeBoard() {
		size = 5;
		board.resizeBoard(size);
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				assertEquals("", board.getCell(row, col), 0);
			}
		}
		assertEquals("", board.getSize(), 5);
		assertEquals("", board.getGameState(), 1);
	}
	@Test
	public void testSMoves() {
		board.makeSMove(3, 3);
		assertEquals("", board.getCell(3, 3), 1);
		assertEquals("", board.getTurn(), 'R'); //Turn should change.
		board.makeSMove(2, 2);
		assertEquals("", board.getCell(2, 2), 1);
		assertEquals("", board.getTurn(), 'B'); //Turn should change here too.
		board.makeSMove(3, 3);
		assertEquals("", board.getCell(3, 3), 1);
		assertEquals("", board.getTurn(), 'B'); //Turn should not change here.
		board.makeSMove(2, 2);
		assertEquals("", board.getCell(2, 2), 1);
		assertEquals("", board.getTurn(), 'B'); //Nor should it here.
	}
	@Test
	public void testOMoves() {
		board.makeOMove(3, 3);
		assertEquals("", board.getCell(3, 3), 2);
		assertEquals("", board.getTurn(), 'R');	//Turn should change.
		board.makeOMove(2, 2);
		assertEquals("", board.getCell(2, 2), 2);
		assertEquals("", board.getTurn(), 'B');	//Turn should change here too.
		board.makeOMove(3, 3);
		assertEquals("", board.getCell(3, 3), 2);
		assertEquals("", board.getTurn(), 'B'); //Turn should not change here.
		board.makeOMove(2, 2);
		assertEquals("", board.getCell(2, 2), 2);
		assertEquals("", board.getTurn(), 'B'); //Nor should it here.
	}
	@Test
	public void testSOSBlueWithS() {
		board.makeSMove(0, 0); //Blue
		board.makeOMove(1, 1); //Red
		board.makeSMove(2, 2); //Blue
		assertEquals("", board.getBlueScore(), 1); 
		assertEquals("", board.getRedScore(), 0);
		assertEquals("", board.getTurn(), 'B'); //Blue should be the turn, as Blue made an SOS.
	}
	@Test
	public void testSOSRedWithO() {
		board.makeOMove(5, 5); //Blue
		board.makeSMove(0, 0); //Red
		board.makeSMove(0, 2); //Blue
		board.makeOMove(0, 1); //Red
		assertEquals("", board.getBlueScore(), 0); 
		assertEquals("", board.getRedScore(), 1);
		assertEquals("", board.getTurn(), 'R'); //Red should be the turn, as Red made an SOS.
	}
	@Test
	public void testSOSSimpleBlueWin() {
		size = 3;
		board.resizeBoard(size);
		board.makeSMove(0, 0); //Blue
		board.simpleGameEndCheck();
		assertEquals("", board.getWinner(), 'N');
		board.makeOMove(1, 1); //Red
		board.simpleGameEndCheck();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(2, 2); //Blue
		board.simpleGameEndCheck();
		assertEquals("", board.getBlueScore(), 1); 
		assertEquals("", board.getRedScore(), 0);
		assertEquals("", board.getWinner(), 'B');
		assertEquals("", board.getTurn(), 'B'); //Blue should be the turn, as Blue made an SOS.
	}
	@Test
	public void testSOSSimpleRedWin() {
		size = 3;
		board.resizeBoard(size);
		board.makeSMove(0, 0); //Blue
		board.simpleGameEndCheck();
		assertEquals("", board.getWinner(), 'N');
		board.makeOMove(1, 1); //Red
		board.simpleGameEndCheck();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(0, 2); //Blue
		board.simpleGameEndCheck();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(2, 2); //Red
		board.simpleGameEndCheck();
		assertEquals("", board.getBlueScore(), 0); 
		assertEquals("", board.getRedScore(), 1);
		assertEquals("", board.getWinner(), 'R');
		assertEquals("", board.getTurn(), 'R'); //Red should be the turn, as Red made an SOS.
	}
	@Test
	public void testSOSSimpleDraw() {
		size = 3;
		board.resizeBoard(size);
		board.makeSMove(0, 0); //Blue
		board.simpleGameEndCheck();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(0, 1); //Red
		board.simpleGameEndCheck();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(0, 2); //Blue
		board.simpleGameEndCheck();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(1, 0); //Red
		board.simpleGameEndCheck();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(1, 1); //Blue
		board.simpleGameEndCheck();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(1, 2); //Red
		board.simpleGameEndCheck();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(2, 0); //Blue
		board.simpleGameEndCheck();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(2, 1); //Red
		board.simpleGameEndCheck();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(2, 2); //Blue
		board.simpleGameEndCheck();
		assertEquals("", board.getBlueScore(), 0); 
		assertEquals("", board.getRedScore(), 0);
		assertEquals("", board.getWinner(), 'D');
		assertEquals("", board.getTurn(), 'R'); //Most recent turn done was Blue, but no SOS, so turn will be Red.
	}
	@Test
	public void testSOSGeneralBlueWin() {
		size = 3;
		board.resizeBoard(size);
		board.makeSMove(0, 0); //Blue places S
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		board.makeOMove(0, 1); //Red places O
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(0, 2); //Blue places S
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		assertEquals("", board.getTurn(), 'B');
		board.makeSMove(1, 0); //Blue (Made an SOS) places S
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		board.makeOMove(1, 1); //Red places O
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(1, 2); //Blue places S
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		assertEquals("", board.getTurn(), 'B');
		board.makeSMove(2, 0); //Blue (Made an SOS) places S
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		assertEquals("", board.getTurn(), 'B');
		board.makeOMove(2, 1); //Blue (Made an SOS) places O
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(2, 2); //Red places S, makes 2 SOSes.
		board.gameOver();
		assertEquals("", board.getBlueScore(), 3);
		assertEquals("", board.getRedScore(), 2); 
		assertEquals("", board.getWinner(), 'B');
		assertEquals("", board.getTurn(), 'R'); //Red made final SOS.
	}
	@Test
	public void testSOSGeneralRedWin() {
		size = 3;
		board.resizeBoard(size);
		board.makeSMove(0, 0); //Blue places S
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(1, 1); //Red places S
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		board.makeOMove(0, 1); //Blue places O
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(0, 2); //Red places S
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		assertEquals("", board.getTurn(), 'R');
		board.makeSMove(2, 0); //Red (Made SOS) places S
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		board.makeOMove(1, 2); //Blue places O
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		board.makeOMove(2, 1); //Red places O
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(1, 0); //Blue places S
		board.gameOver();
		assertEquals("", board.getWinner(), 'N');
		board.makeSMove(2, 2); //Red places S
		board.gameOver();
		assertEquals("", board.getBlueScore(), 0);
		assertEquals("", board.getRedScore(), 3); 
		assertEquals("", board.getWinner(), 'R');
		assertEquals("", board.getTurn(), 'R'); //Red made final SOSes.
	}
	@Test
	public void testSOSGeneralDraw() {	
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board.makeSMove(i, j);
				board.gameOver();
			}
		}
		assertEquals("", board.getBlueScore(), 0);
		assertEquals("", board.getRedScore(), 0); 
		assertEquals("", board.getWinner(), 'D');
	}
	@Test
	public void testRedCPUSimpleWin() {
		size = 10;
		board.resizeBoard(size);
		board.makeSMove(0, 0); //Blue places S
		board.simpleGameEndCheck();
		assertEquals("", board.getTurn(), 'R');
		board.redCPU();	//Red makes a random move.
		board.simpleGameEndCheck();
		assertEquals("", board.getTurn(), 'B');
		if (board.getCell(2, 2) != 0) {
			board.makeSMove(2, 0); //Blue places S
			board.simpleGameEndCheck();
			assertEquals("", board.getTurn(), 'R');
		}
		else if (board.getCell(2, 2) == 0) {
			board.makeSMove(2, 2); //Blue places S
			board.simpleGameEndCheck();
			assertEquals("", board.getTurn(), 'R');
		}
		board.redCPU(); //Red should form an SOS with an O move at (1, 1)
		board.simpleGameEndCheck();
		assertEquals("", board.getCell(1, 1), 2);
		assertEquals("", board.getBlueScore(), 0);
		assertEquals("", board.getRedScore(), 1); 
		assertEquals("", board.getWinner(), 'R');
		assertEquals("", board.getTurn(), 'R');
	}
	@Test
	public void testBlueCPUSimpleWin() {
		size = 10;
		board.resizeBoard(size);
		board.blueCPU();    //Blue makes a random move.
		board.simpleGameEndCheck();
		assertEquals("", board.getTurn(), 'R');
		board.makeSMove(0, 0); //Red places S
		board.simpleGameEndCheck();
		assertEquals("", board.getTurn(), 'B');
		board.blueCPU();	//Blue makes a random move.
		board.simpleGameEndCheck();
		assertEquals("", board.getTurn(), 'R');
		if (board.getCell(2, 2) != 0) {
			board.makeSMove(2, 0); //Red places S
			board.simpleGameEndCheck();
			assertEquals("", board.getTurn(), 'B');
		}
		else if (board.getCell(2, 2) == 0) {
			board.makeSMove(2, 2); //Red places S
			board.simpleGameEndCheck();
			assertEquals("", board.getTurn(), 'B');
		}
		board.blueCPU(); //Red should form an SOS with an O move at (1, 1)
		board.simpleGameEndCheck();
		assertEquals("", board.getCell(1, 1), 2);
		assertEquals("", board.getBlueScore(), 1);
		assertEquals("", board.getRedScore(), 0); 
		assertEquals("", board.getWinner(), 'B');
		assertEquals("", board.getTurn(), 'B');
	}
}
