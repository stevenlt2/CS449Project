import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class GUI extends JFrame {
	private Board board;
	
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
	
	//Red Player Declarations
	JPanel p2Panel = new JPanel();
	JLabel playerNameR = new JLabel("Red Player");
	JLabel hiddenTextR = new JLabel("0000000000000000000000");
	JRadioButton p2S = new JRadioButton("S");
	JRadioButton p2O = new JRadioButton("O");
	int redScore = 0;
	JLabel rSCORE = new JLabel("Score: " + redScore);
	
	//Footer Declarations
	JPanel footerPanel = new JPanel();
	JLabel sizeLabel = new JLabel("Choose Size: ");
	JSpinner sizeSet = new JSpinner(new SpinnerNumberModel(10, 3, 10, 1)); 	//Starts at size 10, lowest is size 3.
	JLabel turnDisplay = new JLabel("Current Turn: ");
	JButton newGameBTN = new JButton("New Game");
	boolean playerBlueTurn = true;
	
	//Grid Declarations
	int gridSize = (int) sizeSet.getValue();
	int numOfCells = gridSize * gridSize;
	JButton[][] buttons = new JButton[gridSize][gridSize];
	
	//Action Listener tied to New Game Button
	public class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (simple.isSelected() && !general.isSelected() && e.getSource()==newGameBTN) { 			//Resizes Grid for Simple Mode
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
				simple.setSelected(false);
				general.setSelected(false);
			}
			else if (general.isSelected() && !simple.isSelected() && e.getSource()==newGameBTN) { 		//Resizes Grid for General Mode
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
				simple.setSelected(false);
				general.setSelected(false);
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
		Color color = buttons[i][j].getBackground();
		if (playerBlueTurn == true && color != purple) {
			if (color == Color.RED) {
				buttons[i][j].setBackground(purple);
				buttons[i][j].setForeground(Color.WHITE);
			}
			else {
				buttons[i][j].setBackground(Color.BLUE);
				buttons[i][j].setForeground(Color.WHITE);
			}
		}
		else if (playerBlueTurn == false && color != purple) {
			if (color == Color.BLUE) {
				buttons[i][j].setBackground(purple);
				buttons[i][j].setForeground(Color.WHITE);
			}
			else {
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
			JOptionPane.showMessageDialog(buttons[i][j], "Game Over: Blue is the winner!");
		}
		else if (board.getWinner() == 'R') {
			JOptionPane.showMessageDialog(buttons[i][j], "Game Over: Red is the winner!");
		}
		else if (board.getWinner() == 'D') {
			JOptionPane.showMessageDialog(buttons[i][j], "Game Over: Draw!");
		}
		else if (board.getWinner() == 'N') {
			//Nothing happens.
		}
	}
	
	//Action Listener tied to Simple Game Grid
	public class SimpleCellActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent eventS) {
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
										turnDisplay.setText("Current Turn: Red");
									}
									else if (board.getTurn() == 'B') {
										playerBlueTurn = true;
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
										turnDisplay.setText("Current Turn: Red");
									}
									else if (board.getTurn() == 'B') {
										playerBlueTurn = true;
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
										turnDisplay.setText("Current Turn: Red");
									}
									else if (board.getTurn() == 'B') {
										playerBlueTurn = true;
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
										turnDisplay.setText("Current Turn: Red");
									}
									else if (board.getTurn() == 'B') {
										playerBlueTurn = true;
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
	}
	
	//Action Listener tied to General Game Grid
	public class GeneralCellActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent eventG) {
			//General Game will run here.
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
										turnDisplay.setText("Current Turn: Red");
									}
									else if (board.getTurn() == 'B') {
										playerBlueTurn = true;
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
										turnDisplay.setText("Current Turn: Red");
									}
									else if (board.getTurn() == 'B') {
										playerBlueTurn = true;
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
										turnDisplay.setText("Current Turn: Red");
									}
									else if (board.getTurn() == 'B') {
										playerBlueTurn = true;
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
										turnDisplay.setText("Current Turn: Red");
									}
									else if (board.getTurn() == 'B') {
										playerBlueTurn = true;
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
				buttons[i][j].setForeground(Color.BLACK);
				buttons[i][j].setBackground(Color.WHITE);
				buttons[i][j].setText("");
				buttons[i][j].setFocusable(false);
				buttons[i][j].addActionListener(new SimpleCellActionListener());
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
				buttons[i][j].setForeground(Color.BLACK);
				buttons[i][j].setBackground(Color.WHITE);
				buttons[i][j].setText("");
				buttons[i][j].setFocusable(false);
				buttons[i][j].addActionListener(new GeneralCellActionListener());
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
		p1Panel.setLayout(new GridLayout(5,1));
		p1Panel.setBackground(Color.BLUE);
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
		p1Panel.add(bSCORE);
		p1Panel.add(hiddenTextB);
		
		//P2 Panel
		p2Panel.setLayout(new GridLayout(5,1));
		p2Panel.setBackground(Color.RED);
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
		p2Panel.add(rSCORE);
		p2Panel.add(hiddenTextR);
		
		//Footer Panel.
		footerPanel.setLayout(new GridLayout(1, 4));
		footerPanel.setBackground(Color.BLACK);
		sizeLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
		sizeLabel.setForeground(Color.WHITE);
		footerPanel.add(sizeLabel);
		//sizeSet.setPreferredSize(new Dimension(20, 24));
		footerPanel.add(sizeSet);
		turnDisplay.setFont(new Font("Monospaced", Font.PLAIN, 15));
		turnDisplay.setForeground(Color.WHITE);
		footerPanel.add(turnDisplay);
		footerPanel.add(newGameBTN);
		newGameBTN.setBackground(purple);
		newGameBTN.setForeground(Color.WHITE);
		newGameBTN.addActionListener(new MyActionListener());
		
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
