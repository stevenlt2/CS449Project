import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;

public class GUI extends JFrame {
	private Board board;
	
	//CPU Status
	int blueOn = 0; //Signals that a Blue CPU player is in the game, and GUI will change its behavior accordingly.
	int redOn = 0;	//Signals that a Red CPU player is in the game, and GUI will change its behavior accordingly.
	int simpleCPU = 0; //Determines how end game condition is checked after each CPU move, which is when an SOS is achieved.
	int generalCPU = 0;//Determines how end game condition is checked after each CPU move, which is when the board is full.
	
	//Record Type
	int generalRec = 0;
	int simpleRec = 0;
	
	//Declarations
	JFrame frame = new JFrame();
	JPanel buttonPanel = new JPanel();
	
	//Title Declarations
	JPanel titlePanel = new JPanel();
	JLabel textfield = new JLabel();
	JLabel modeText = new JLabel();
	JRadioButton general = new JRadioButton("General");
	JRadioButton simple = new JRadioButton("Simple");
	String modeMessage = "";
	
	//Blue Player Declarations
	JPanel p1Panel = new JPanel();
	JLabel playerNameB = new JLabel("Blue Player");
	JLabel hiddenTextB = new JLabel("0000000000000000000000");
	JRadioButton p1S = new JRadioButton("S");
	JRadioButton p1O = new JRadioButton("O");
	int blueScore = 0;
	JLabel bSCORE = new JLabel("Score: " + blueScore);
	JRadioButton blueCPUbtn = new JRadioButton("CPU");
	
	//Red Player Declarations
	JPanel p2Panel = new JPanel();
	JLabel playerNameR = new JLabel("Red Player");
	JLabel hiddenTextR = new JLabel("0000000000000000000000");
	JRadioButton p2S = new JRadioButton("S");
	JRadioButton p2O = new JRadioButton("O");
	int redScore = 0;
	JLabel rSCORE = new JLabel("Score: " + redScore);
	JRadioButton redCPUbtn = new JRadioButton("CPU");
	
	//Footer Declarations
	JPanel footerPanel = new JPanel();
	JLabel sizeLabel = new JLabel("Choose Size ----->");
	JSpinner sizeSet = new JSpinner(new SpinnerNumberModel(10, 3, 10, 1)); 	//Starts at size 10, lowest is size 3.
	JLabel turnDisplay = new JLabel("Current Turn: ");
	JButton newGameBTN = new JButton("New Game");
	boolean playerBlueTurn = true;
	JRadioButton recordBTN = new JRadioButton("Record");
	JLabel recordStatus = new JLabel("Not Recording"); //Alternate status: Recording...
	JLabel replayStatus = new JLabel("Not Ready ->"); //Alternate status: "Ready ->"
	JButton replayBTN = new JButton("Replay?");
	
	//Grid Declarations
	int gridSize = (int) sizeSet.getValue();
	int numOfCells = gridSize * gridSize;
	JButton[][] buttons = new JButton[gridSize][gridSize];
	
	//Action Listener tied to New Game Button
	public class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (replayGUISignal == 1 && e.getSource()==newGameBTN) {
				modeMessage = "Replay in progress, please wait...";
				JOptionPane.showMessageDialog(newGameBTN, modeMessage);
			}
			else if (simple.isSelected() && !general.isSelected() && e.getSource()==newGameBTN && !blueCPUbtn.isSelected() && !redCPUbtn.isSelected()) { 			//Resizes Grid for Simple Mode (No CPU)
				modeText.setText("Simple");
				gridSize = (int) sizeSet.getValue();
				numOfCells = gridSize * gridSize;
				board.resizeBoard(gridSize);	//Resizes array.
				destroyGrid();
				firstTurn();
				board.setFirstTurn();
				makeSimpleGrid(gridSize);
				blueScore = 0;
				bSCORE.setText("Score: " + blueScore);
				redScore = 0;
				rSCORE.setText("Score: " + redScore);
				playerNameB.setText("Blue Player");
				playerNameR.setText("Red Player");
				turnDisplay.setBackground(Color.BLUE);
				simple.setSelected(false);
				general.setSelected(false);
				if (recordBTN.isSelected() == true) {
					if (board.checkRecEmpty() != true) {
						board.deleteRecording();
						replayStatus.setText("Not Ready ->");
					}
					if (board.checkRecStackEmpty() != true) {
						board.resetRecording();
					}
					board.setRecordSignal();
					recordStatus.setBackground(Color.RED);
					recordStatus.setText("Recording...");
					simpleRec = 1;
					generalRec = 0;
				}
				else if (recordBTN.isSelected() == false) {
					board.resetRecordSignal();
					recordStatus.setBackground(Color.BLACK);
					recordStatus.setText("Not Recording");
				}
				blueOn = 0;
				redOn = 0;
				simpleCPU = 0;
				generalCPU = 0;
			}
			else if (general.isSelected() && !simple.isSelected() && e.getSource()==newGameBTN && !blueCPUbtn.isSelected() && !redCPUbtn.isSelected()) { 		//Resizes Grid for General Mode (No CPU)
				modeText.setText("General");
				gridSize = (int) sizeSet.getValue();
				numOfCells = gridSize * gridSize;
				board.resizeBoard(gridSize);	//Resizes array.
				destroyGrid();
				firstTurn();
				board.setFirstTurn();
				makeGeneralGrid(gridSize);
				blueScore = 0;
				bSCORE.setText("Score: " + blueScore);
				redScore = 0;
				rSCORE.setText("Score: " + redScore);
				playerNameB.setText("Blue Player");
				playerNameR.setText("Red Player");
				turnDisplay.setBackground(Color.BLUE);
				simple.setSelected(false);
				general.setSelected(false);
				if (recordBTN.isSelected() == true) {
					if (board.checkRecEmpty() != true) {
						board.deleteRecording();
						replayStatus.setText("Not Ready ->");
					}
					if (board.checkRecStackEmpty() != true) {
						board.resetRecording();
					}
					board.setRecordSignal();
					recordStatus.setBackground(Color.RED);
					recordStatus.setText("Recording...");
					simpleRec = 0;
					generalRec = 1;
				}
				else if (recordBTN.isSelected() == false) {
					board.resetRecordSignal();
					recordStatus.setBackground(Color.BLACK);
					recordStatus.setText("Not Recording");
				}
				blueOn = 0;
				redOn = 0;
				simpleCPU = 0;
				generalCPU = 0;
			}
			else if (simple.isSelected() && !general.isSelected() && e.getSource()==newGameBTN && !blueCPUbtn.isSelected() && redCPUbtn.isSelected()) { 			//Resizes Grid for Simple Mode (Red CPU)
				modeText.setText("Simple");
				gridSize = (int) sizeSet.getValue();
				numOfCells = gridSize * gridSize;
				board.resizeBoard(gridSize);	//Resizes array.
				destroyGrid();
				firstTurn();
				board.setFirstTurn();
				makeSimpleGrid(gridSize);
				blueScore = 0;
				bSCORE.setText("Score: " + blueScore);
				redScore = 0;
				rSCORE.setText("Score: " + redScore);
				playerNameB.setText("Blue Player");
				playerNameR.setText("CPU");
				turnDisplay.setBackground(Color.BLUE);
				simple.setSelected(false);
				general.setSelected(false);
				if (recordBTN.isSelected() == true) {
					if (board.checkRecEmpty() != true) {
						board.deleteRecording();
						replayStatus.setText("Not Ready ->");
					}
					if (board.checkRecStackEmpty() != true) {
						board.resetRecording();
					}
					board.setRecordSignal();
					recordStatus.setBackground(Color.RED);
					recordStatus.setText("Recording...");
					simpleRec = 1;
					generalRec = 0;
				}
				else if (recordBTN.isSelected() == false) {
					board.resetRecordSignal();
					recordStatus.setBackground(Color.BLACK);
					recordStatus.setText("Not Recording");
				}
				blueOn = 0;
				redOn = 1;
				simpleCPU = 1;
				generalCPU = 0;
			}
			else if (simple.isSelected() && !general.isSelected() && e.getSource()==newGameBTN && blueCPUbtn.isSelected() && !redCPUbtn.isSelected()) { 		//Resizes Grid for Simple Mode (Blue CPU)
				modeText.setText("Simple");
				gridSize = (int) sizeSet.getValue();
				numOfCells = gridSize * gridSize;
				board.resizeBoard(gridSize);	//Resizes array.
				destroyGrid();
				firstTurn();
				board.setFirstTurn();
				makeSimpleGrid(gridSize);
				blueScore = 0;
				bSCORE.setText("Score: " + blueScore);
				redScore = 0;
				rSCORE.setText("Score: " + redScore);
				playerNameB.setText("CPU");
				playerNameR.setText("Red Player");
				turnDisplay.setBackground(Color.BLUE);
				simple.setSelected(false);
				general.setSelected(false);
				if (recordBTN.isSelected() == true) {
					if (board.checkRecEmpty() != true) {
						board.deleteRecording();
						replayStatus.setText("Not Ready ->");
					}
					if (board.checkRecStackEmpty() != true) {
						board.resetRecording();
					}
					board.setRecordSignal();
					recordStatus.setBackground(Color.RED);
					recordStatus.setText("Recording...");
					simpleRec = 1;
					generalRec = 0;
				}
				else if (recordBTN.isSelected() == false) {
					board.resetRecordSignal();
					recordStatus.setBackground(Color.BLACK);
					recordStatus.setText("Not Recording");
				}
				blueOn = 1;
				redOn = 0;
				simpleCPU = 1;
				generalCPU = 0;
				//Blue will make first move.
				cpuBLUE();
			}
			else if (simple.isSelected() && !general.isSelected() && e.getSource()==newGameBTN && blueCPUbtn.isSelected() && redCPUbtn.isSelected()) { 		//Resizes Grid for Simple Mode (Both CPU)
				modeText.setText("Simple");
				gridSize = (int) sizeSet.getValue();
				numOfCells = gridSize * gridSize;
				board.resizeBoard(gridSize);	//Resizes array.
				destroyGrid();
				firstTurn();
				board.setFirstTurn();
				makeSimpleGrid(gridSize);
				blueScore = 0;
				bSCORE.setText("Score: " + blueScore);
				redScore = 0;
				rSCORE.setText("Score: " + redScore);
				playerNameB.setText("CPU");
				playerNameR.setText("CPU");
				turnDisplay.setBackground(Color.BLUE);
				simple.setSelected(false);
				general.setSelected(false);
				if (recordBTN.isSelected() == true) {
					if (board.checkRecEmpty() != true) {
						board.deleteRecording();
						replayStatus.setText("Not Ready ->");
					}
					if (board.checkRecStackEmpty() != true) {
						board.resetRecording();
					}
					board.setRecordSignal();
					recordStatus.setBackground(Color.RED);
					recordStatus.setText("Recording...");
					simpleRec = 1;
					generalRec = 0;
				}
				else if (recordBTN.isSelected() == false) {
					board.resetRecordSignal();
					recordStatus.setBackground(Color.BLACK);
					recordStatus.setText("Not Recording");
				}
				blueOn = 1;
				redOn = 1;
				simpleCPU = 1;
				generalCPU = 0;
				//Timed moves for CPU
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						for (int i = 0; i < gridSize; i++) {
							for (int j = 0; j < gridSize; j++) {
								if (playerBlueTurn == true) {
									cpuBLUE();
									board.simpleGameEndCheck();
									timer.purge();
								}
							}
						}
						if (board.getWinner() != 'N') {
							cpuWinCheck();
							timer.cancel();
						}
					}
				};
				TimerTask task2 = new TimerTask() {
					@Override
					public void run() {
						for (int i = 0; i < gridSize; i++) {
							for (int j = 0; j < gridSize; j++) {
								if (playerBlueTurn == false) {
									cpuRED();
									board.simpleGameEndCheck();
									timer.purge();
								}
							}
						}
						if (board.getWinner() != 'N') {
							cpuWinCheck();
							timer.cancel();
						}
					}
				};
				//Blue will make first move.
				//General Game (Both CPU) will run here, as button source is New Game Button.
				timer.schedule(task, 2000, 5000);
				timer.schedule(task2, 2000, 6000);
			}
			else if (!simple.isSelected() && general.isSelected() && e.getSource()==newGameBTN && !blueCPUbtn.isSelected() && redCPUbtn.isSelected()) { 			//Resizes Grid for General Mode (Red CPU)
				modeText.setText("General");
				gridSize = (int) sizeSet.getValue();
				numOfCells = gridSize * gridSize;
				board.resizeBoard(gridSize);	//Resizes array.
				destroyGrid();
				firstTurn();
				board.setFirstTurn();
				makeGeneralGrid(gridSize);
				blueScore = 0;
				bSCORE.setText("Score: " + blueScore);
				redScore = 0;
				rSCORE.setText("Score: " + redScore);
				playerNameB.setText("Blue Player");
				playerNameR.setText("CPU");
				turnDisplay.setBackground(Color.BLUE);
				simple.setSelected(false);
				general.setSelected(false);
				if (recordBTN.isSelected() == true) {
					if (board.checkRecEmpty() != true) {
						board.deleteRecording();
						replayStatus.setText("Not Ready ->");
					}
					if (board.checkRecStackEmpty() != true) {
						board.resetRecording();
					}
					board.setRecordSignal();
					recordStatus.setBackground(Color.RED);
					recordStatus.setText("Recording...");
					simpleRec = 0;
					generalRec = 1;
				}
				else if (recordBTN.isSelected() == false) {
					board.resetRecordSignal();
					recordStatus.setBackground(Color.BLACK);
					recordStatus.setText("Not Recording");
				}
				blueOn = 0;
				redOn = 1;
				simpleCPU = 0;
				generalCPU = 1;
			}
			else if (!simple.isSelected() && general.isSelected() && e.getSource()==newGameBTN && blueCPUbtn.isSelected() && !redCPUbtn.isSelected()) { 		//Resizes Grid for General Mode (Blue CPU)
				modeText.setText("General");
				gridSize = (int) sizeSet.getValue();
				numOfCells = gridSize * gridSize;
				board.resizeBoard(gridSize);	//Resizes array.
				destroyGrid();
				firstTurn();
				board.setFirstTurn();
				makeGeneralGrid(gridSize);
				blueScore = 0;
				bSCORE.setText("Score: " + blueScore);
				redScore = 0;
				rSCORE.setText("Score: " + redScore);
				playerNameB.setText("CPU");
				playerNameR.setText("Red Player");
				turnDisplay.setBackground(Color.BLUE);
				simple.setSelected(false);
				general.setSelected(false);
				if (recordBTN.isSelected() == true) {
					if (board.checkRecEmpty() != true) {
						board.deleteRecording();
						replayStatus.setText("Not Ready ->");
					}
					if (board.checkRecStackEmpty() != true) {
						board.resetRecording();
					}
					board.setRecordSignal();
					recordStatus.setBackground(Color.RED);
					recordStatus.setText("Recording...");
					simpleRec = 0;
					generalRec = 1;
				}
				else if (recordBTN.isSelected() == false) {
					board.resetRecordSignal();
					recordStatus.setBackground(Color.BLACK);
					recordStatus.setText("Not Recording");
				}
				blueOn = 1;
				redOn = 0;
				simpleCPU = 0;
				generalCPU = 1;
				//Blue will make first move.
				cpuBLUE();
			}
			else if (!simple.isSelected() && general.isSelected() && e.getSource()==newGameBTN && blueCPUbtn.isSelected() && redCPUbtn.isSelected()) { 		//Resizes Grid for General Mode (Both CPU)
				modeText.setText("General");
				gridSize = (int) sizeSet.getValue();
				numOfCells = gridSize * gridSize;
				board.resizeBoard(gridSize);	//Resizes array.
				destroyGrid();
				firstTurn();
				board.setFirstTurn();
				makeGeneralGrid(gridSize);
				blueScore = 0;
				bSCORE.setText("Score: " + blueScore);
				redScore = 0;
				rSCORE.setText("Score: " + redScore);
				playerNameB.setText("CPU");
				playerNameR.setText("CPU");
				turnDisplay.setBackground(Color.BLUE);
				simple.setSelected(false);
				general.setSelected(false);
				if (recordBTN.isSelected() == true) {
					if (board.checkRecEmpty() != true) {
						board.deleteRecording();
						replayStatus.setText("Not Ready ->");
					}
					if (board.checkRecStackEmpty() != true) {
						board.resetRecording();
					}
					board.setRecordSignal();
					recordStatus.setBackground(Color.RED);
					recordStatus.setText("Recording...");
					simpleRec = 0;
					generalRec = 1;
				}
				else if (recordBTN.isSelected() == false) {
					board.resetRecordSignal();
					recordStatus.setBackground(Color.BLACK);
					recordStatus.setText("Not Recording");
				}
				blueOn = 1;
				redOn = 1;
				simpleCPU = 0;
				generalCPU = 1;
				//Timed moves for CPU
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						for (int i = 0; i < gridSize; i++) {
							for (int j = 0; j < gridSize; j++) {
								if (playerBlueTurn == true) {
									cpuBLUE();
									board.gameOver();
									timer.purge();
								}
							}
						}
						if (board.getWinner() != 'N') {
							cpuWinCheck();
							timer.cancel();
						}
					}
				};
				TimerTask task2 = new TimerTask() {
					@Override
					public void run() {
						for (int i = 0; i < gridSize; i++) {
							for (int j = 0; j < gridSize; j++) {
								if (playerBlueTurn == false) {
									cpuRED();
									board.gameOver();
									timer.purge();
								}
							}
						}
						if (board.getWinner() != 'N') {
							cpuWinCheck();
							timer.cancel();
						}
					}
				};
				//Blue will make first move.
				//Simple Game (Both CPU) will run here, as button source is New Game Button.
				timer.schedule(task, 2000, 5000);
				timer.schedule(task2, 2000, 6000);
			}
			else if (simple.isSelected() && general.isSelected()) { 									//If both modes are selected.
				modeMessage = "Choose only one mode.";
				simple.setSelected(false);
				general.setSelected(false);
				JOptionPane.showMessageDialog(newGameBTN, modeMessage);
			}
			else if (!simple.isSelected() && !general.isSelected()) {									//If no modes are selected.
				modeMessage = "Choose a mode.";
				JOptionPane.showMessageDialog(newGameBTN, modeMessage);
			}
		}
	}
	
	int replayGUISignal = 0;

	public class ReplayListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent r) {
			if (replayGUISignal == 1 && r.getSource()==replayBTN) {
				modeMessage = "Replay in progress, please wait...";
				JOptionPane.showMessageDialog(replayBTN, modeMessage);
			}
			else if (board.storedRecordingSignal == 0 && r.getSource()==replayBTN) {
				modeMessage = "Nothing has been recorded, please record a game before attempting to use the replay feature!";
				JOptionPane.showMessageDialog(replayBTN, modeMessage);
			}
			else if (r.getSource()==replayBTN && simpleRec == 1) {
				replayGUISignal = 1;
				modeText.setText("S. REPLAY");
				gridSize = board.getStoredBoardSize();
				numOfCells = gridSize * gridSize;
				board.resizeBoard(gridSize);	//Resizes array.
				destroyGrid();
				firstTurn();
				board.setFirstTurn();
				remakeSimpleGrid(gridSize);
				blueScore = 0;
				bSCORE.setText("Score: " + blueScore);
				redScore = 0;
				rSCORE.setText("Score: " + redScore);
				playerNameB.setText("Blue Player");
				playerNameR.setText("Red Player");
				turnDisplay.setBackground(Color.BLUE);
				if (recordBTN.isSelected() == true) {
					if (board.checkRecStackEmpty() != true) {
						board.resetRecording();
					}
					board.setRecordSignal();
					recordStatus.setBackground(Color.RED);
					recordStatus.setText("Recording...");
				}
				else if (recordBTN.isSelected() == false) {
					board.setRecordSignal();
					recordStatus.setBackground(Color.BLACK);
					recordStatus.setText("Not Recording");
				}
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
							simpleGUIReplay();
						if (board.getWinner() != 'N') {
							timer.cancel();
							cpuWinCheck();
							replayGUISignal = 0;
						}
					}
				};
				timer.schedule(task, 1000, 1000);
			}
			else if (r.getSource()==replayBTN && generalRec == 1) {
				replayGUISignal = 1;
				modeText.setText("G. REPLAY");
				gridSize = board.getStoredBoardSize();
				numOfCells = gridSize * gridSize;
				board.resizeBoard(gridSize);	//Resizes array.
				destroyGrid();
				firstTurn();
				board.setFirstTurn();
				remakeGeneralGrid(gridSize);
				blueScore = 0;
				bSCORE.setText("Score: " + blueScore);
				redScore = 0;
				rSCORE.setText("Score: " + redScore);
				playerNameB.setText("Blue Player");
				playerNameR.setText("Red Player");
				turnDisplay.setBackground(Color.BLUE);
				if (recordBTN.isSelected() == true) {
					if (board.checkRecStackEmpty() != true) {
						board.resetRecording();
					}
					board.setRecordSignal();
					recordStatus.setBackground(Color.RED);
					recordStatus.setText("Recording...");
				}
				else if (recordBTN.isSelected() == false) {
					board.setRecordSignal();
					recordStatus.setBackground(Color.BLACK);
					recordStatus.setText("Not Recording");
				}
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
							generalGUIReplay();
						if (board.getWinner() != 'N') {
							timer.cancel();
							cpuWinCheck();
							replayGUISignal = 0;
						}
					}
				};
				timer.schedule(task, 1000, 1000);
			}
		}
	}
	
	public void guiRecord() {
		if (board.getRecordSignal() == 1) {
			board.endRecording();
		}
	}
	
	public void recordCompleteMSG() {
		if (board.getRecordSignal() == 1) {
			recordStatus.setBackground(Color.BLUE);
			recordStatus.setText("Recorded!");
			replayStatus.setText("Ready ->");
		}
	}
	
	public void simpleGUIReplay() {
		if (playerBlueTurn == true) {
			replayBLUE();
		}
		else if (playerBlueTurn == false) {
			replayRED();
		}
		board.simpleGameEndCheck();
	}
	
	public void generalGUIReplay() {
		if (playerBlueTurn == true) {
			replayBLUE();
		}
		else if (playerBlueTurn == false) {
			replayRED();
		}
		board.gameOver();
	}
	
	public void replayBLUE() {
		board.replay();
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (board.getCell(i, j) == 1 && buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.RED) {
					if (buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.RED) {
						buttons[i][j].setText("S");
						buttons[i][j].setForeground(Color.BLUE);
						highlightSOS(i, j);
						cellColorCheck();
						if (board.getTurn() == 'B') {
							playerBlueTurn = true;
							turnDisplay.setBackground(Color.BLUE);
							turnDisplay.setText("Current Turn: Blue");
						}
						else if (board.getTurn() == 'R') {
							playerBlueTurn = false;
							turnDisplay.setBackground(Color.RED);
							turnDisplay.setText("Current Turn: Red");
						}
						blueScore = board.getBlueScore();
						bSCORE.setText("Score: " + blueScore);
					}
				}
				if (board.getCell(i, j) == 2 && buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.RED) {
					if (buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.RED) {
						buttons[i][j].setText("O");
						buttons[i][j].setForeground(Color.BLUE);
						highlightSOS(i, j);
						cellColorCheck();
						if (board.getTurn() == 'B') {
							playerBlueTurn = true;
							turnDisplay.setBackground(Color.BLUE);
							turnDisplay.setText("Current Turn: Blue");
						}
						else if (board.getTurn() == 'R') {
							playerBlueTurn = false;
							turnDisplay.setBackground(Color.RED);
							turnDisplay.setText("Current Turn: Red");
						}
						blueScore = board.getBlueScore();
						bSCORE.setText("Score: " + blueScore);
					}
				}
			}
		}
	}
	
	public void replayRED() {
		board.replay();
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (board.getCell(i, j) == 1 && buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.BLUE) {
					if (buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.BLUE) {
						buttons[i][j].setText("S");
						buttons[i][j].setForeground(Color.RED);
						highlightSOS(i, j);
						cellColorCheck();
						if (board.getTurn() == 'R') {
							playerBlueTurn = false;
							turnDisplay.setBackground(Color.RED);
							turnDisplay.setText("Current Turn: Red");
						}
						else if (board.getTurn() == 'B') {
							playerBlueTurn = true;
							turnDisplay.setBackground(Color.BLUE);
							turnDisplay.setText("Current Turn: Blue");
						}
						redScore = board.getRedScore();
						rSCORE.setText("Score: " + redScore);
					}
				}
				if (board.getCell(i, j) == 2 && buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.BLUE) {
					if (buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.BLUE) {
						buttons[i][j].setText("O");
						buttons[i][j].setForeground(Color.RED);
						highlightSOS(i, j);
						cellColorCheck();
						if (board.getTurn() == 'R') {
							playerBlueTurn = false;
							turnDisplay.setBackground(Color.RED);
							turnDisplay.setText("Current Turn: Red");
						}
						else if (board.getTurn() == 'B') {
							playerBlueTurn = true;
							turnDisplay.setBackground(Color.BLUE);
							turnDisplay.setText("Current Turn: Blue");
						}
						redScore = board.getRedScore();
						rSCORE.setText("Score: " + redScore);
					}
				}
			}
		}
	}
	
	//Method will check SOS types, receiving a signal (0 or 1). Will color any SOS type that has given a signal of 1.
	public void highlightSOS(int i, int j) {
		checkN(i, j);
		checkNE(i, j);
		checkE(i, j);
		checkSE(i, j);
		checkS(i, j);
		checkSW(i, j);
		checkW(i, j);
		checkNW(i, j);
		checkNnS(i, j);
		checkNEnSW(i, j);
		checkEnW(i, j);
		checkSEnNW(i, j);
		board.resetSignals();
	}
	
	Color purple = new Color(102, 0, 153);
	
	public void checkColor(int i, int j) {
		Color cell = buttons[i][j].getBackground();
		if (playerBlueTurn == true && cell != purple) {
			if (cell == Color.RED) {
				buttons[i][j].setBackground(purple);
				buttons[i][j].setForeground(Color.WHITE);
			}
			else if (cell == Color.BLACK) {
				buttons[i][j].setBackground(Color.BLUE);
				buttons[i][j].setForeground(Color.WHITE);
			}
		}
		else if (playerBlueTurn == false && cell != purple) {
			if (cell == Color.BLUE) {
				buttons[i][j].setBackground(purple);
				buttons[i][j].setForeground(Color.WHITE);
			}
			else if (cell == Color.BLACK) {
				buttons[i][j].setBackground(Color.RED);
				buttons[i][j].setForeground(Color.WHITE);
			}
		}
	}
	
	public void checkN(int i, int j) {
		if (board.getSOSsignalN() != 0) {
			checkColor(i, j);
			checkColor(i - 1, j);
			checkColor(i - 2, j);
		}
	}
	public void checkNE(int i, int j) {
		if (board.getSOSsignalNE() != 0) {
			checkColor(i, j);
			checkColor(i - 1, j + 1);
			checkColor(i - 2, j + 2);
		}
	}
	public void checkE(int i, int j) {
		if (board.getSOSsignalE() != 0) {
			checkColor(i, j);
			checkColor(i, j + 1);
			checkColor(i, j + 2);
		}
	}
	public void checkSE(int i, int j) {
		if (board.getSOSsignalSE() != 0) {
			checkColor(i, j);
			checkColor(i + 1, j + 1);
			checkColor(i + 2, j + 2);
		}
	}
	public void checkS(int i, int j) {
		if (board.getSOSsignalS() != 0) {
			checkColor(i, j);
			checkColor(i + 1, j);
			checkColor(i + 2, j);
		}
	}
	public void checkSW(int i, int j) {
		if (board.getSOSsignalSW() != 0) {
			checkColor(i, j);
			checkColor(i + 1, j - 1);
			checkColor(i + 2, j - 2);
		}
	}
	public void checkW(int i, int j) {
		if (board.getSOSsignalW() != 0) {
			checkColor(i, j);
			checkColor(i, j - 1);
			checkColor(i, j - 2);
		}
	}
	public void checkNW(int i, int j) {
		if (board.getSOSsignalNW() != 0) {
			checkColor(i, j);
			checkColor(i - 1, j - 1);
			checkColor(i - 2, j - 2);
		}
	}
	public void checkNnS(int i, int j) {
		if (board.getSOSsignalNnS() != 0) {
			checkColor(i - 1, j);
			checkColor(i, j);
			checkColor(i + 1, j);
		}
	}
	public void checkNEnSW(int i, int j) {
		if (board.getSOSsignalNEnSW() != 0) {
			checkColor(i + 1, j - 1);
			checkColor(i, j);
			checkColor(i - 1, j + 1);
		}
	}
	public void checkEnW(int i, int j) {
		if (board.getSOSsignalEnW() != 0) {
			checkColor(i, j - 1);
			checkColor(i, j);
			checkColor(i, j + 1);
		}
	}
	public void checkSEnNW(int i, int j) {
		if (board.getSOSsignalSEnNW() != 0) {
			checkColor(i + 1, j + 1);
			checkColor(i, j);
			checkColor(i - 1, j - 1);
		}
	}
	
	public void winCheck(int i, int j) {
		if (board.getWinner() == 'B') {
			humanChecked = 1;
			guiRecord();
			recordCompleteMSG();
			JOptionPane.showMessageDialog(buttons[i][j], "Game Over: Blue is the winner!");
		}
		else if (board.getWinner() == 'R') {
			humanChecked = 1;
			guiRecord();
			recordCompleteMSG();
			JOptionPane.showMessageDialog(buttons[i][j], "Game Over: Red is the winner!");
		}
		else if (board.getWinner() == 'D') {
			humanChecked = 1;
			guiRecord();
			recordCompleteMSG();
			JOptionPane.showMessageDialog(buttons[i][j], "Game Over: Draw!");
		}
		else if (board.getWinner() == 'N') {
			//Nothing happens.
		}
	}
	
	int cpuChecked = 0; //Made to prevent duplicate Win Dialog, verifies CPU checked the win first and therefore no similar action should be taken by another player.
	int humanChecked = 0; //Made to prevent duplicate Win Dialog, verifies Human checked the win first and therefore no similar action should be taken by another player.
	
	public void cpuWinCheck() {
		if (board.getWinner() == 'B') {
			cpuChecked = 1;
			guiRecord();
			recordCompleteMSG();
			JOptionPane.showMessageDialog(null, "Game Over: Blue is the winner!");
		}
		else if (board.getWinner() == 'R') {
			cpuChecked = 1;
			guiRecord();
			recordCompleteMSG();
			JOptionPane.showMessageDialog(null, "Game Over: Red is the winner!");
		}
		else if (board.getWinner() == 'D') {
			cpuChecked = 1;
			guiRecord();
			recordCompleteMSG();
			JOptionPane.showMessageDialog(null, "Game Over: Draw!");
		}
		else if (board.getWinner() == 'N') {
			//Nothing happens.
		}
	}
	public void cpuBLUE() {
		int r, c;
		if (playerBlueTurn == true && blueOn == 1) {
			board.blueCPU();
			r = board.getBlueCPURow();
			c = board.getBlueCPUCol();
			for (int i = 0; i < gridSize; i++) {
				for (int j = 0; j < gridSize; j++) {
					if (board.getCell(i, j) == 1 && buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.RED) {
						if (buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.RED) {
							buttons[i][j].setText("S");
							buttons[i][j].setForeground(Color.BLUE);
							highlightSOS(r, c);
							cellColorCheck();
							if (board.getTurn() == 'B') {
								playerBlueTurn = true;
								turnDisplay.setBackground(Color.BLUE);
								turnDisplay.setText("Current Turn: Blue");
							}
							else if (board.getTurn() == 'R') {
								playerBlueTurn = false;
								turnDisplay.setBackground(Color.RED);
								turnDisplay.setText("Current Turn: Red");
							}
							blueScore = board.getBlueScore();
							bSCORE.setText("Score: " + blueScore);
							if (simpleCPU == 1 && generalCPU != 1) {
								board.simpleGameEndCheck();
							}
							else if (simpleCPU != 1 && generalCPU == 1) {
								board.gameOver();
							}
						}
					}
					if (board.getCell(i, j) == 2 && buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.RED) {
						if (buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.RED) {
							buttons[i][j].setText("O");
							buttons[i][j].setForeground(Color.BLUE);
							highlightSOS(r, c);
							cellColorCheck();
							if (board.getTurn() == 'B') {
								playerBlueTurn = true;
								turnDisplay.setBackground(Color.BLUE);
								turnDisplay.setText("Current Turn: Blue");
							}
							else if (board.getTurn() == 'R') {
								playerBlueTurn = false;
								turnDisplay.setBackground(Color.RED);
								turnDisplay.setText("Current Turn: Red");
							}
							blueScore = board.getBlueScore();
							bSCORE.setText("Score: " + blueScore);
							if (simpleCPU == 1 && generalCPU != 1) {
								board.simpleGameEndCheck();
							}
							else if (simpleCPU != 1 && generalCPU == 1) {
								board.gameOver();
							}
						}
					}
				}
			}
		}
	}
	public void cpuRED() {
		int r, c;
		if (playerBlueTurn == false && redOn == 1) {
			board.redCPU();
			r = board.getRedCPURow();
			c = board.getRedCPUCol();
			for (int i = 0; i < gridSize; i++) {
				for (int j = 0; j < gridSize; j++) {
					if (board.getCell(i, j) == 1 && buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.BLUE) {
						if (buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.BLUE) {
							buttons[i][j].setText("S");
							buttons[i][j].setForeground(Color.RED);
							highlightSOS(r, c);
							cellColorCheck();
							if (board.getTurn() == 'R') {
								playerBlueTurn = false;
								turnDisplay.setBackground(Color.RED);
								turnDisplay.setText("Current Turn: Red");
							}
							else if (board.getTurn() == 'B') {
								playerBlueTurn = true;
								turnDisplay.setBackground(Color.BLUE);
								turnDisplay.setText("Current Turn: Blue");
							}
							redScore = board.getRedScore();
							rSCORE.setText("Score: " + redScore);
							if (simpleCPU == 1 && generalCPU != 1) {
								board.simpleGameEndCheck();
							}
							else if (simpleCPU != 1 && generalCPU == 1) {
								board.gameOver();
							}
						}
					}
					if (board.getCell(i, j) == 2 && buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.BLUE) {
						if (buttons[i][j].getText() == "" && buttons[i][j].getForeground() != Color.BLUE) {
							buttons[i][j].setText("O");
							buttons[i][j].setForeground(Color.RED);
							highlightSOS(r, c);
							cellColorCheck();
							if (board.getTurn() == 'R') {
								playerBlueTurn = false;
								turnDisplay.setBackground(Color.RED);
								turnDisplay.setText("Current Turn: Red");
							}
							else if (board.getTurn() == 'B') {
								playerBlueTurn = true;
								turnDisplay.setBackground(Color.BLUE);
								turnDisplay.setText("Current Turn: Blue");
							}
							redScore = board.getRedScore();
							rSCORE.setText("Score: " + redScore);
							if (simpleCPU == 1 && generalCPU != 1) {
								board.simpleGameEndCheck();
							}
							else if (simpleCPU != 1 && generalCPU == 1) {
								board.gameOver();
							}
						}
					}
				}
			}
		}
	}
	
	//This method aims to fix a minor and rare bug where the text color and cell background color are the same (Only occurs in CPU games, caused by CPU players).
	public void cellColorCheck() {
			for (int i = 0 ; i < gridSize; i++) {
				for (int j = 0; j < gridSize; j++) {
					if (buttons[i][j].getBackground() != Color.BLACK) {
						buttons[i][j].setForeground(Color.WHITE);
					}
				}
			}
	}
	
	//Action Listener tied to Simple Game Grid
	public class SimpleCellActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent eventS) {
			if (!redCPUbtn.isSelected() && !blueCPUbtn.isSelected() && blueOn == 0 && redOn == 0) {
				//Simple Game will run here.
				for (int i = 0; i < gridSize; i++) {
					for (int j = 0; j < gridSize; j++) {
						if(eventS.getSource()==buttons[i][j]) {
							if (playerBlueTurn == true) {
								if(buttons[i][j].getText()=="" && board.getCell(i, j) == 0) {
									if (p1S.isSelected() && !p1O.isSelected()) {
										buttons[i][j].setText("S");
										buttons[i][j].setForeground(Color.BLUE);
										board.makeSMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
										}
										else if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
										}
										blueScore = board.getBlueScore();
										bSCORE.setText("Score: " + blueScore);
										board.simpleGameEndCheck();
										winCheck(i, j);
										//p1S.setSelected(false);
										//p1O.setSelected(false);
									}
									if (p1O.isSelected() && !p1S.isSelected()) {
										buttons[i][j].setText("O");
										buttons[i][j].setForeground(Color.BLUE);
										board.makeOMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
										}
										else if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
										}
										blueScore = board.getBlueScore();
										bSCORE.setText("Score: " + blueScore);
										board.simpleGameEndCheck();
										winCheck(i, j);
										//p1S.setSelected(false);
										//p1O.setSelected(false);
									}
									if (p1S.isSelected() && p1O.isSelected()) {
										playerBlueTurn = true;
										p1S.setSelected(false);
										p1O.setSelected(false);
									}
									if (!p1S.isSelected() && !p1O.isSelected()) {
										playerBlueTurn = true;
									}
								}
							}
							else if (playerBlueTurn == false) {
								if(buttons[i][j].getText()=="" && board.getCell(i, j) == 0) {
									if (p2S.isSelected() && !p2O.isSelected()) {
										buttons[i][j].setText("S");
										buttons[i][j].setForeground(Color.RED);
										board.makeSMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
										}
										else if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
										}
										redScore = board.getRedScore();
										rSCORE.setText("Score: " + redScore);
										board.simpleGameEndCheck();
										winCheck(i, j);
										//p2S.setSelected(false);
										//p2O.setSelected(false);
									}
									if (p2O.isSelected() && !p2S.isSelected()) {
										buttons[i][j].setText("O");
										buttons[i][j].setForeground(Color.RED);
										board.makeOMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
										}
										else if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
										}
										redScore = board.getRedScore();
										rSCORE.setText("Score: " + redScore);
										board.simpleGameEndCheck();
										winCheck(i, j);
										//p2S.setSelected(false);
										//p2O.setSelected(false);
									}
									if (p2S.isSelected() && p2O.isSelected()) {
										playerBlueTurn = false;
										p2S.setSelected(false);
										p2O.setSelected(false);
									}
									if (!p2S.isSelected() && !p2O.isSelected()) {
										playerBlueTurn = false;
									}
								}
							}
						}
					}
				}
			}
			else if (redCPUbtn.isSelected() && !blueCPUbtn.isSelected() && blueOn == 0 && redOn == 1) {
				//Simple Game (Red CPU) will run here.
				cpuChecked = 0;
				humanChecked = 0;
				for (int i = 0; i < gridSize; i++) {
					for (int j = 0; j < gridSize; j++) {
						if(eventS.getSource()==buttons[i][j]) {
							if (playerBlueTurn == true) {
								if(buttons[i][j].getText()=="" && board.getCell(i, j) == 0) {
									if (p1S.isSelected() && !p1O.isSelected()) {
										buttons[i][j].setText("S");
										buttons[i][j].setForeground(Color.BLUE);
										board.makeSMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
											Timer timerSR = new Timer();
											TimerTask taskSR = new TimerTask() {
												@Override
												public void run() {
													if (board.getTurn() == 'R') {
														cpuRED(); //Main command
														timerSR.purge();
														if (board.getWinner() != 'N' && cpuChecked == 0 && humanChecked != 1) {
															timerSR.cancel();
															board.simpleGameEndCheck();
															cpuWinCheck();
														}
													}
												}
											};
											timerSR.schedule(taskSR, 1000, 4000);
										}
										else if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
										}
										blueScore = board.getBlueScore();
										bSCORE.setText("Score: " + blueScore);
										board.simpleGameEndCheck();
										if (cpuChecked != 1 && humanChecked == 0) {
											winCheck(i, j);
										}
										//p1S.setSelected(false);
										//p1O.setSelected(false);
									}
									if (p1O.isSelected() && !p1S.isSelected()) {
										buttons[i][j].setText("O");
										buttons[i][j].setForeground(Color.BLUE);
										board.makeOMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
											Timer timerSR = new Timer();
											TimerTask taskSR = new TimerTask() {
												@Override
												public void run() {
													if (board.getTurn() == 'R') {
														cpuRED();
														timerSR.purge();
														if (board.getWinner() != 'N'  && cpuChecked == 0 && humanChecked != 1) {
															timerSR.cancel();
															board.simpleGameEndCheck();
															cpuWinCheck();
														}
													}
												}
											};
											timerSR.schedule(taskSR, 1000, 4000);
										}
										else if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
										}
										blueScore = board.getBlueScore();
										bSCORE.setText("Score: " + blueScore);
										board.simpleGameEndCheck();
										if (cpuChecked != 1 && humanChecked == 0) {
											winCheck(i, j);
										}
										//p1S.setSelected(false);
										//p1O.setSelected(false);
									}
									if (p1S.isSelected() && p1O.isSelected()) {
										playerBlueTurn = true;
										p1S.setSelected(false);
										p1O.setSelected(false);
									}
									if (!p1S.isSelected() && !p1O.isSelected()) {
										playerBlueTurn = true;
									}
								}
							}
						}
					}
				}
			}
			else if (!redCPUbtn.isSelected() && blueCPUbtn.isSelected() && blueOn == 1 && redOn == 0) {
				//Simple Game (Blue CPU) will run here.
				cpuChecked = 0;
				humanChecked = 0;
				for (int i = 0; i < gridSize; i++) {
					for (int j = 0; j < gridSize; j++) {
						if(eventS.getSource()==buttons[i][j]) {	
							if (playerBlueTurn != true) {
								if(buttons[i][j].getText()=="" && board.getCell(i, j) == 0) {
									if (p2S.isSelected() && !p2O.isSelected()) {
										buttons[i][j].setText("S");
										buttons[i][j].setForeground(Color.RED);
										board.makeSMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
											Timer timerSB = new Timer();
											TimerTask taskSB = new TimerTask() {
												@Override
												public void run() {
													if (board.getTurn() == 'B') {
														cpuBLUE();
														timerSB.purge();
														if (board.getWinner() != 'N'  && cpuChecked == 0 && humanChecked != 1) {
															timerSB.cancel();
															board.simpleGameEndCheck();
															cpuWinCheck();
														}
													}
												}
											};
											timerSB.schedule(taskSB, 1000, 4000);
										}
										else if (board.getTurn() == 'R' && humanChecked == 0) {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
										}
										redScore = board.getRedScore();
										rSCORE.setText("Score: " + redScore);
										board.simpleGameEndCheck();
										if (cpuChecked != 1) {
											winCheck(i, j);
										}
									}
									if (p2O.isSelected() && !p2S.isSelected()) {
										buttons[i][j].setText("O");
										buttons[i][j].setForeground(Color.RED);
										board.makeOMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
											Timer timerSB = new Timer();
											TimerTask taskSB = new TimerTask() {
												@Override
												public void run() {
													if (board.getTurn() == 'B') {
														cpuBLUE();
														timerSB.purge();
														if (board.getWinner() != 'N'  && cpuChecked == 0 && humanChecked != 1) {
															timerSB.cancel();
															board.simpleGameEndCheck();
															cpuWinCheck();
														}
													}
												}
											};
											timerSB.schedule(taskSB, 1000, 4000);
										}
										else if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
										}
										redScore = board.getRedScore();
										rSCORE.setText("Score: " + redScore);
										board.simpleGameEndCheck();
										if (cpuChecked != 1 && humanChecked == 0) {
											winCheck(i, j);
										}
									}
									if (p2S.isSelected() && p2O.isSelected()) {
										playerBlueTurn = false;
										p2S.setSelected(false);
										p2O.setSelected(false);
									}
									if (!p2S.isSelected() && !p2O.isSelected()) {
										playerBlueTurn = false;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	//Action Listener tied to General Game Grid
	public class GeneralCellActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent eventG) {
			if (!redCPUbtn.isSelected() && !blueCPUbtn.isSelected() && blueOn == 0 && redOn == 0) {
				//General Game (No CPU) will run here.
				for (int i = 0; i < gridSize; i++) {
					for (int j = 0; j < gridSize; j++) {
						if(eventG.getSource()==buttons[i][j]) {
							if (playerBlueTurn == true) {
								if(buttons[i][j].getText()=="") {
									if (p1S.isSelected() && !p1O.isSelected()) {
										buttons[i][j].setText("S");
										buttons[i][j].setForeground(Color.BLUE);
										board.makeSMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
										}
										else if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
										}
										blueScore = board.getBlueScore();
										bSCORE.setText("Score: " + blueScore);
										board.gameOver();
										winCheck(i, j);
										//p1S.setSelected(false);
										//p1O.setSelected(false);
									}
									if (p1O.isSelected() && !p1S.isSelected()) {
										buttons[i][j].setText("O");
										buttons[i][j].setForeground(Color.BLUE);
										board.makeOMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
										}
										else if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
										}
										blueScore = board.getBlueScore();
										bSCORE.setText("Score: " + blueScore);
										board.gameOver();
										winCheck(i, j);
										//p1S.setSelected(false);
										//p1O.setSelected(false);
									}
									if (p1S.isSelected() && p1O.isSelected()) {
										playerBlueTurn = true;
										p1S.setSelected(false);
										p1O.setSelected(false);
									}
									if (!p1S.isSelected() && !p1O.isSelected()) {
										playerBlueTurn = true;
									}
								}
							}
							else if (playerBlueTurn == false) {
								if(buttons[i][j].getText()=="") {
									if (p2S.isSelected() && !p2O.isSelected()) {
										buttons[i][j].setText("S");
										buttons[i][j].setForeground(Color.RED);
										board.makeSMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
										}
										else if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
										}
										redScore = board.getRedScore();
										rSCORE.setText("Score: " + redScore);
										board.gameOver();
										winCheck(i, j);
										//p2S.setSelected(false);
										//p2O.setSelected(false);
									}
									if (p2O.isSelected() && !p2S.isSelected()) {
										buttons[i][j].setText("O");
										buttons[i][j].setForeground(Color.RED);
										board.makeOMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
										}
										else if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
										}
										redScore = board.getRedScore();
										rSCORE.setText("Score: " + redScore);
										board.gameOver();
										winCheck(i, j);
										//p2S.setSelected(false);
										//p2O.setSelected(false);
									}
									if (p2S.isSelected() && p2O.isSelected()) {
										playerBlueTurn = false;
										p2S.setSelected(false);
										p2O.setSelected(false);
									}
									if (!p2S.isSelected() && !p2O.isSelected()) {
										playerBlueTurn = false;
									}
								}
							}
						}
					}
				}
			}
			else if (redCPUbtn.isSelected() && !blueCPUbtn.isSelected() && blueOn == 0 && redOn == 1) {
				//General Game (Red CPU) will run here.
				cpuChecked = 0;
				humanChecked = 0;
				for (int i = 0; i < gridSize; i++) {
					for (int j = 0; j < gridSize; j++) {
						if(eventG.getSource()==buttons[i][j]) {
							if (playerBlueTurn == true) {
								if(buttons[i][j].getText()=="" && board.getCell(i, j) == 0) {
									if (p1S.isSelected() && !p1O.isSelected()) {
										buttons[i][j].setText("S");
										buttons[i][j].setForeground(Color.BLUE);
										board.makeSMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
											Timer timerSR = new Timer();
											TimerTask taskSR = new TimerTask() {
												@Override
												public void run() {
													if (board.getTurn() == 'R') {
														cpuRED();
														timerSR.purge();
														if (board.getWinner() != 'N' && cpuChecked == 0 && humanChecked != 1) {
															timerSR.cancel();
															board.gameOver();
															cpuWinCheck();
														}
													}
												}
											};
											timerSR.schedule(taskSR, 1000, 4000);
										}
										else if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
										}
										blueScore = board.getBlueScore();
										bSCORE.setText("Score: " + blueScore);
										board.gameOver();
										if (cpuChecked != 1 && humanChecked == 0) {
											winCheck(i, j);
										}
										//p1S.setSelected(false);
										//p1O.setSelected(false);
									}
									if (p1O.isSelected() && !p1S.isSelected()) {
										buttons[i][j].setText("O");
										buttons[i][j].setForeground(Color.BLUE);
										board.makeOMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
											Timer timerSR = new Timer();
											TimerTask taskSR = new TimerTask() {
												@Override
												public void run() {
													if (board.getTurn() == 'R') {
														cpuRED();
														timerSR.purge();
														if (board.getWinner() != 'N'  && cpuChecked == 0 && humanChecked != 1) {
															timerSR.cancel();
															board.gameOver();
															cpuWinCheck();
														}
													}
												}
											};
											timerSR.schedule(taskSR, 1000, 4000);
										}
										else if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
										}
										blueScore = board.getBlueScore();
										bSCORE.setText("Score: " + blueScore);
										board.gameOver();
										if (cpuChecked != 1 && humanChecked == 0) {
											winCheck(i, j);
										}
										//p1S.setSelected(false);
										//p1O.setSelected(false);
									}
									if (p1S.isSelected() && p1O.isSelected()) {
										playerBlueTurn = true;
										p1S.setSelected(false);
										p1O.setSelected(false);
									}
									if (!p1S.isSelected() && !p1O.isSelected()) {
										playerBlueTurn = true;
									}
								}
							}
						}
					}
				}
			}
			else if (!redCPUbtn.isSelected() && blueCPUbtn.isSelected() && blueOn == 1 && redOn == 0) {
				//General Game (Blue CPU) will run here.
				cpuChecked = 0;
				humanChecked = 0;
				for (int i = 0; i < gridSize; i++) {
					for (int j = 0; j < gridSize; j++) {
						if(eventG.getSource()==buttons[i][j]) {	
							if (playerBlueTurn != true) {
								if(buttons[i][j].getText()=="" && board.getCell(i, j) == 0) {
									if (p2S.isSelected() && !p2O.isSelected()) {
										buttons[i][j].setText("S");
										buttons[i][j].setForeground(Color.RED);
										board.makeSMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
											Timer timerSB = new Timer();
											TimerTask taskSB = new TimerTask() {
												@Override
												public void run() {
													if (board.getTurn() == 'B') {
														cpuBLUE();
														timerSB.purge();
														if (board.getWinner() != 'N'  && cpuChecked == 0 && humanChecked != 1) {
															timerSB.cancel();
															board.gameOver();
															cpuWinCheck();
														}
													}
												}
											};
											timerSB.schedule(taskSB, 1000, 4000);
										}
										else if (board.getTurn() == 'R' && humanChecked == 0) {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
										}
										redScore = board.getRedScore();
										rSCORE.setText("Score: " + redScore);
										board.gameOver();
										if (cpuChecked != 1) {
											winCheck(i, j);
										}
									}
									if (p2O.isSelected() && !p2S.isSelected()) {
										buttons[i][j].setText("O");
										buttons[i][j].setForeground(Color.RED);
										board.makeOMove(i, j);
										highlightSOS(i, j);
										if (board.getTurn() == 'B') {
											playerBlueTurn = true;
											turnDisplay.setBackground(Color.BLUE);
											turnDisplay.setText("Current Turn: Blue");
											Timer timerSB = new Timer();
											TimerTask taskSB = new TimerTask() {
												@Override
												public void run() {
													if (board.getTurn() == 'B') {
														cpuBLUE();
														timerSB.purge();
														if (board.getWinner() != 'N'  && cpuChecked == 0 && humanChecked != 1) {
															timerSB.cancel();
															board.gameOver();
															cpuWinCheck();
														}
													}
												}
											};
											timerSB.schedule(taskSB, 1000, 4000);
										}
										else if (board.getTurn() == 'R') {
											playerBlueTurn = false;
											turnDisplay.setBackground(Color.RED);
											turnDisplay.setText("Current Turn: Red");
										}
										redScore = board.getRedScore();
										rSCORE.setText("Score: " + redScore);
										board.gameOver();
										if (cpuChecked != 1 && humanChecked == 0) {
											winCheck(i, j);
										}
									}
									if (p2S.isSelected() && p2O.isSelected()) {
										playerBlueTurn = false;
										p2S.setSelected(false);
										p2O.setSelected(false);
									}
									if (!p2S.isSelected() && !p2O.isSelected()) {
										playerBlueTurn = false;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	//Defines first turn for a new game or reset game. Blue is always first.
	public void firstTurn() {
		playerBlueTurn = true;
		turnDisplay.setText("Current Turn: Blue");
	}
	
	//Grid Creator for Simple Game
	public void makeSimpleGrid(int gSize) {
		buttonPanel.setLayout(new GridLayout(gSize, gSize));
		//buttonPanel.setBackground(Color.BLACK);
		for (int i = 0; i < gSize; i++) {
			for (int j = 0; j < gSize; j++) {
				buttons[i][j] = new JButton();
				buttonPanel.add(buttons[i][j]);
				buttons[i][j].setForeground(Color.WHITE);
				buttons[i][j].setBackground(Color.BLACK);
				buttons[i][j].setText("");
				buttons[i][j].setFont(new Font("Monospaced", Font.BOLD, 70 - gSize));
				buttons[i][j].setFocusable(false);
				buttons[i][j].addActionListener(new SimpleCellActionListener());
			}
		}
		buttonPanel.setOpaque(true);
	}
	//Simple Replay Grid
	public void remakeSimpleGrid(int gSize) {
		buttonPanel.setLayout(new GridLayout(gSize, gSize));
		//buttonPanel.setBackground(Color.BLACK);
		for (int i = 0; i < gSize; i++) {
			for (int j = 0; j < gSize; j++) {
				buttons[i][j] = new JButton();
				buttonPanel.add(buttons[i][j]);
				buttons[i][j].setForeground(Color.WHITE);
				buttons[i][j].setBackground(Color.BLACK);
				buttons[i][j].setText("");
				buttons[i][j].setFont(new Font("Monospaced", Font.BOLD, 70 - gSize));
				buttons[i][j].setFocusable(false);
			}
		}
		buttonPanel.setOpaque(true);
	}
	//Grid Creator for General Game
	public void makeGeneralGrid(int gSize) {
		buttonPanel.setLayout(new GridLayout(gSize, gSize));
		//buttonPanel.setBackground(Color.BLACK);
		for (int i = 0; i < gSize; i++) {
			for (int j = 0; j < gSize; j++) {
				buttons[i][j] = new JButton();
				buttonPanel.add(buttons[i][j]);
				buttons[i][j].setForeground(Color.WHITE);
				buttons[i][j].setBackground(Color.BLACK);
				buttons[i][j].setText("");
				buttons[i][j].setFont(new Font("Monospaced", Font.BOLD, 70 - gSize));
				buttons[i][j].setFocusable(false);
				buttons[i][j].addActionListener(new GeneralCellActionListener());
			}
		}
		buttonPanel.setOpaque(true);
	}
	//Grid Creator for General Game
		public void remakeGeneralGrid(int gSize) {
			buttonPanel.setLayout(new GridLayout(gSize, gSize));
			//buttonPanel.setBackground(Color.BLACK);
			for (int i = 0; i < gSize; i++) {
				for (int j = 0; j < gSize; j++) {
					buttons[i][j] = new JButton();
					buttonPanel.add(buttons[i][j]);
					buttons[i][j].setForeground(Color.WHITE);
					buttons[i][j].setBackground(Color.BLACK);
					buttons[i][j].setText("");
					buttons[i][j].setFont(new Font("Monospaced", Font.BOLD, 70 - gSize));
					buttons[i][j].setFocusable(false);
				}
			}
			buttonPanel.setOpaque(true);
		}
	//Grid Destroyer
	public void destroyGrid() {
		buttonPanel.removeAll();
		buttonPanel.revalidate();
		buttonPanel.repaint();
	}

	//The GUI itself
	public GUI(Board board) {
		this.board = board;
		
		//Frame Settings
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 800);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setLayout(new BorderLayout());
		frame.setTitle("SOS");
		frame.setVisible(true); 
		
		//Text Label for Title
		textfield.setBackground(Color.BLACK);
		textfield.setForeground(Color.WHITE);
		textfield.setFont(new Font("Monospaced", Font.BOLD, 75));
		textfield.setHorizontalAlignment(JLabel.CENTER);
		textfield.setText("SOS");
		textfield.setOpaque(true);
		
		//Cosmetic Settings for Mode Buttons
		simple.setBackground(Color.BLACK);
		simple.setForeground(Color.WHITE);
		general.setBackground(Color.BLACK);
		general.setForeground(Color.WHITE);
		
		//Mode Selection for Title
		modeText.setBackground(purple);
		modeText.setForeground(Color.WHITE);
		modeText.setHorizontalAlignment(JLabel.CENTER);
		modeText.setFont(new Font("Monospaced", Font.BOLD, 25));
		modeText.setText("Choose Mode");
		modeText.setOpaque(true);
		
		
		//Title
		titlePanel.setLayout(new GridLayout(1, 4));
		titlePanel.setBounds(0, 0, 600, 100);
		titlePanel.add(textfield);
		titlePanel.add(modeText);
		titlePanel.add(simple);
		titlePanel.add(general);
		
		//P1 Panel
		p1Panel.setLayout(new GridLayout(6,1));
		p1Panel.setBackground(Color.BLUE);
		playerNameB.setHorizontalAlignment(JLabel.CENTER);
		bSCORE.setHorizontalAlignment(JLabel.CENTER);
		p1S.setHorizontalAlignment(JLabel.CENTER);
		p1O.setHorizontalAlignment(JLabel.CENTER);
		playerNameB.setForeground(Color.WHITE);
		hiddenTextB.setForeground(Color.BLUE);
		playerNameB.setFont(new Font("Monospaced", Font.BOLD, 20));
		p1Panel.add(playerNameB);
		p1S.setBackground(Color.BLUE);
		p1S.setForeground(Color.WHITE);
		p1Panel.add(p1S);
		p1O.setBackground(Color.BLUE);
		p1O.setForeground(Color.WHITE);
		p1Panel.add(p1O);
		p1Panel.setOpaque(true);
		bSCORE.setForeground(Color.WHITE);
		bSCORE.setFont(new Font("Monospaced", Font.BOLD, 20));
		blueCPUbtn.setForeground(Color.WHITE);
		blueCPUbtn.setBackground(Color.BLUE);
		blueCPUbtn.setHorizontalAlignment(JLabel.CENTER);
		p1Panel.add(bSCORE);
		p1Panel.add(hiddenTextB);
		p1Panel.add(blueCPUbtn);
		
		//P2 Panel
		p2Panel.setLayout(new GridLayout(6,1));
		p2Panel.setBackground(Color.RED);
		playerNameR.setHorizontalAlignment(JLabel.CENTER);
		rSCORE.setHorizontalAlignment(JLabel.CENTER);
		p2S.setHorizontalAlignment(JLabel.CENTER);
		p2O.setHorizontalAlignment(JLabel.CENTER);
		playerNameR.setForeground(Color.WHITE);
		hiddenTextR.setForeground(Color.RED);
		playerNameR.setFont(new Font("Monospaced", Font.BOLD, 20));
		p2Panel.add(playerNameR);
		p2S.setBackground(Color.RED);
		p2S.setForeground(Color.WHITE);
		p2Panel.add(p2S);
		p2O.setBackground(Color.RED);
		p2O.setForeground(Color.WHITE);
		p2Panel.add(p2O);
		p2Panel.setOpaque(true);
		rSCORE.setForeground(Color.WHITE);
		rSCORE.setFont(new Font("Monospaced", Font.BOLD, 20));
		redCPUbtn.setForeground(Color.WHITE);
		redCPUbtn.setBackground(Color.RED);
		redCPUbtn.setHorizontalAlignment(JLabel.CENTER);
		p2Panel.add(rSCORE);
		p2Panel.add(hiddenTextR);
		p2Panel.add(redCPUbtn);
		
		//Footer Panel.
		footerPanel.setLayout(new GridLayout(2, 4));
		footerPanel.setBackground(Color.BLACK);
		sizeLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
		sizeLabel.setForeground(Color.WHITE);
		footerPanel.add(sizeLabel);
		//sizeSet.setPreferredSize(new Dimension(20, 24));
		footerPanel.add(sizeSet);
		turnDisplay.setFont(new Font("Monospaced", Font.BOLD, 15));
		turnDisplay.setForeground(Color.WHITE);
		turnDisplay.setHorizontalAlignment(JLabel.CENTER);
		turnDisplay.setBackground(Color.BLACK);
		turnDisplay.setOpaque(true);
		footerPanel.add(turnDisplay);
		newGameBTN.setBackground(purple);
		newGameBTN.setForeground(Color.WHITE);
		newGameBTN.addActionListener(new MyActionListener());
		footerPanel.add(newGameBTN);
		recordStatus.setFont(new Font("Monospaced", Font.BOLD, 15));
		recordStatus.setForeground(Color.WHITE);
		recordStatus.setHorizontalAlignment(JLabel.CENTER);
		recordStatus.setOpaque(true);
		recordStatus.setBackground(Color.BLACK);
		recordBTN.setForeground(Color.WHITE);
		recordBTN.setHorizontalAlignment(JLabel.CENTER);
		recordBTN.setBackground(Color.BLACK);
		footerPanel.add(recordBTN);
		footerPanel.add(recordStatus);
		replayStatus.setFont(new Font("Monospaced", Font.BOLD, 15));
		replayStatus.setForeground(Color.WHITE);
		replayStatus.setHorizontalAlignment(JLabel.CENTER);
		replayStatus.setBackground(Color.BLACK);
		footerPanel.add(replayStatus);
		replayBTN.setBackground(purple);
		replayBTN.setForeground(Color.WHITE);
		replayBTN.addActionListener(new ReplayListener());
		footerPanel.add(replayBTN);
		
		//Items on Frame.
		frame.add(titlePanel, BorderLayout.PAGE_START);
		frame.add(p1Panel, BorderLayout.LINE_START);
		frame.add(p2Panel, BorderLayout.LINE_END);
		buttonPanel.setBackground(Color.BLACK);
		frame.add(buttonPanel); //This is placed in the center, but will not interfere with panels.
		frame.add(footerPanel, BorderLayout.PAGE_END);
	}
	
	public Board getBoard() {
		return board;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GUI(new Board()); 
			}
		});
	}
}
