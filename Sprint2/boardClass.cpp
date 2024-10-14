#include "boardClass.h"

void game::setBoardSize(int userSize){
  this->boardSize = userSize;
  gameBoard.resize(this->boardSize, vector<char>(this->boardSize));
  for (int i = 0; i < boardSize; i++) {
    for (int j = 0; j < boardSize; j++) {
      gameBoard[i][j] = '_';
    }
  }
}
void game::clearBoard(){
  gameBoard.resize(this->boardSize, vector<char>(this->boardSize));
  for (int i = 0; i < boardSize; i++) {
    for (int j = 0; j < boardSize; j++) {
      gameBoard[i][j] = '_';
    }
  }
  player1Points.clear();
  player2Points.clear();
  turnHistory.clear();
  detectedFormations.clear();
  detectedOFormations.clear();
  
  turnNumber = 1;
  gameEnd = false;
}
void game::printBoard(){
  for (int i = 0; i < this->boardSize; i++) {
    for (int j = 0; j < boardSize; j++) {
      if (j == 0) {
        cout << "|" << (i) << "|" << "\t";
      }
      
      cout << gameBoard[i][j] << "\t"; // Print 
      
      /*if(j == (this->boardSize-1)){
        cout << "|" << (i + 1) << "|";
      }*/
      
      if (i == (boardSize - 1) && j == (boardSize - 1)) {
        cout << endl;
        cout << "\t";
        for (int z = 0; z < boardSize; z++) {
          cout << "|" << z << "|" << "\t";
        }
      }
      
    }
  cout << endl;
  }
}

void game::boardPlacement(Player Play){
  int Row = Play.getRowChoice();
  int Col = Play.getColChoice();
  char Choice = Play.getMoveChoice();
  int turnNum = Play.getPlayerNum();
  //this->addTurnNumber();
  //collisions
  if (gameBoard[Row][Col] == 'S' || gameBoard[Row][Col] == 'O' ){
    cout << "There is a letter already there." << endl;
    if(Play.getPlayerType() == "COMPUTER"){
      ComputerMovePlacement(Play);
      //boardPlacement(Play);
    }
  }
  else if(gameBoard[Row][Col] == '_'){
    gameBoard[Row][Col] = Choice;
    this->turnHistory.push_back(turnNum);
    this->addTurnNumber();
  }
}

bool game::isSOS(int row, int col){
int COL = gameBoard.size();
int ROW = gameBoard.size();

   // Check horizontally
   if (col <= COL-3)
      if (gameBoard[row][col]=='S' && gameBoard[row][col+1]=='O' && gameBoard[row][col+2]=='S')
         return true;

   // Check vertically
   if (row <= ROW-3)
      if (gameBoard[row][col]=='S' && gameBoard[row+1][col]=='O' && gameBoard[row+2][col]=='S')
         return true;

   // Check main diagonal
   if (row <= ROW-3 && col <= COL-3)
      if (gameBoard[row][col]=='S' && gameBoard[row+1][col+1]=='O' && gameBoard[row+2][col+2]=='S')
         return true;

   // Check anti-diagonal
   if (row >= 2 && col <= COL-3)
      if (gameBoard[row][col]=='S' && gameBoard[row-1][col+1]=='O' && gameBoard[row-2][col+2]=='S')
         return true;

   return false;
}


bool game::isHorizontalSOS(int row, int col) {
  if (col + 2 >= gameBoard.size()) {
    return false; // Not enough room to form SOS horizontally
  }
  if (gameBoard[row][col] == 'S' && gameBoard[row][col + 1] == 'O' && gameBoard[row][col + 2] == 'S') {
    return true;
  }
  if (col - 1 >= 0 && gameBoard[row][col - 1] == 'S' && gameBoard[row][col] == 'O' && gameBoard[row][col + 1] == 'S') {
    return true;
  }
  return false;
}


bool game::isHorizontalSOSForwards(int row, int col) {
  if (col + 2 >= gameBoard.size()) {
    return false; // Not enough room to form SOS horizontally
  }
  if (gameBoard[row][col] == 'S' && gameBoard[row][col + 1] == 'O' && gameBoard[row][col + 2] == 'S' && detectedFormations[row][col] == 0 && detectedOFormations[row][col+1] == 0) {
    detectedOFormations[row][col+1] = true;
    return true;
  }
  else{
    return false;
  }
  
}

bool game::isHorizontalSOSBackwards(int row, int col) {
  if (col - 2 >= gameBoard.size()) {
    return false; // Not enough room to form SOS horizontally
  }
  if (gameBoard[row][col] == 'S' && gameBoard[row][col - 1] == 'O' && gameBoard[row][col - 2] == 'S' && detectedFormations[row][col-2] == 1 && detectedOFormations[row][col-1] == 0) {
    detectedOFormations[row][col+1] = true;
    return true;
  }
  else{
    return false;
  }

}


bool game::isVerticalSOSBottomUp(int row, int col){
  if (row - 2 < 0) {
    return false; // Not enough room to form SOS vertically
  }
  
  if((gameBoard[row][col] == 'S' && gameBoard[row - 1][col] == 'O' && gameBoard[row - 2][col] == 'S') && detectedFormations[row-2][col] == 1 && detectedOFormations[row-1][col] == 0){
    detectedOFormations[row-1][col] = true;
    return true;
    
  }
  
  else{
    return false;
  }
}

bool game::isVerticalSOSTopDown(int row, int col) {
  if (row + 2 > gameBoard.size()) {
    return false; // Not enough room to form SOS vertically
  }
  //if (row - 2 < 0) {
    //return false; // Not enough room to form SOS vertically
  //}
  
    if(gameBoard[row][col] == 'S' && gameBoard[row + 1][col] == 'O' && gameBoard[row + 2][col] == 'S' && detectedFormations[row][col] == 0 && detectedOFormations[row+1][col] == 0){
      detectedOFormations[row+1][col] = true;
      return true;
    }
    if(gameBoard[row][col] == 'O' && gameBoard[row - 1][col] == 'S' && gameBoard[row + 1][col] == 'S' && detectedOFormations[row][col] == 0){
        detectedOFormations[row][col] = true;
        return true;
    }
  else{
    return false;
  }
}

bool game::isDiagonalSOSDownLeft(int row, int col) {
  if (row + 2 >= gameBoard.size() || col - 2 < 0) {
    return false; // Not enough room to form SOS diagonally
  }
  if(gameBoard[row][col] == 'S' && gameBoard[row + 1][col - 1] == 'O' && gameBoard[row + 2][col - 2] == 'S' && detectedFormations[row + 2][col - 2] == 0 && detectedOFormations[row + 1][col - 1] == 0){
      detectedOFormations[row+1][col-1] = true;
      return true;
  }
  else{
    return false;
  }
}

// Function to check for a diagonal SOS formation going down to the right
bool game::isDiagonalSOSDownRight(int row, int col) {
  if (row + 2 >= gameBoard.size() || col + 2 >= gameBoard.size()) {
    return false; // Not enough room to form SOS diagonally
  }
  if(gameBoard[row][col] == 'S' && gameBoard[row + 1][col + 1] == 'O' && gameBoard[row + 2][col + 2] == 'S' && detectedFormations[row + 2][col + 2] == 0 && detectedOFormations[row + 1][col + 1] == 0){
    detectedOFormations[row+1][col+1] = true;
    return true;
    
  }
  else{
    return false;
  }
}

// Function to check for a diagonal SOS formation going up to the left
bool game::isDiagonalSOSUpLeft(int row, int col) {
  
  if (row - 2 < 0 || col - 2 < 0) {
    return false; // Not enough room to form SOS diagonally
  }
  
  if(gameBoard[row][col] == 'S' && gameBoard[row - 1][col - 1] == 'O' && gameBoard[row - 2][col - 2] == 'S' && detectedFormations[row - 2][col - 2] == 1 && detectedOFormations[row - 1][col -1] == 0){
    detectedOFormations[row-1][col-1] = true;
    return true;
  }
  else{
    return false;
  }

}

// Function to check for a diagonal SOS formation going up to the right
bool game::isDiagonalSOSUpRight(int row, int col) {
  ///
  if (row - 2 < 0 || col + 2 >= gameBoard.size()) {
    return false; // Not enough room to form SOS diagonally
  }
  if(gameBoard[row][col] == 'S' && gameBoard[row - 1][col + 1] == 'O' && gameBoard[row - 2][col + 2] == 'S'&& detectedFormations[row][col] == 0 && detectedOFormations[row - 1][col + 1] == 1){
    detectedOFormations[row-1][col+1] = true;
    return true;
  }
  else{
    return false;
  }
}





// Check for a diagonal SOS formation
bool game::isDiagonalSOS(int row, int col) {
  if (row + 2 >= gameBoard.size() || col + 2 >= gameBoard.size()) {
    return false; // Not enough room to form SOS diagonally
  }
  return (gameBoard[row][col] == 'S' && gameBoard[row + 1][col + 1] == 'O' && gameBoard[row + 2][col + 2] == 'S');
}

// Check for an anti-diagonal SOS formation
bool game::isAntiDiagonalSOS(int row, int col) {
  if (row + 2 >= gameBoard.size() || col - 2 < 0) {
    return false; // Not enough room to form SOS anti-diagonally
  }
  return (gameBoard[row][col] == 'S' && gameBoard[row + 1][col - 1] == 'O' && gameBoard[row + 2][col - 2] == 'S');
}


void game::checkWin(Player& Play1, Player& Play2) {
  printf("\033c");
  int COL = gameBoard.size();
  int ROW = gameBoard.size();
  int boardSpace = (gameBoard.size() * gameBoard.size());
  
  // Create sets to store awarded points for each player

  // Create a 2D array to track detected formations
  detectedFormations.resize(ROW, vector<bool>(COL, false));
  usedS.resize(ROW, vector<bool>(COL, false));
  
  bool extraTurn = false;
  detectedOFormations.resize(ROW, vector<bool>(COL, false));

  // Iterate through the board
  for (int i = 0; i < ROW; i++) {
    for (int j = 0; j < COL; j++) {
      if (!detectedFormations[i][j]) {
        // If the current position is part of an 'S', check for SOS formations in both horizontal and vertical directions
        if (gameBoard[i][j] == 'S') {
          if ( 
            isHorizontalSOSForwards(i,j)||
            isHorizontalSOSBackwards(i,j)||
            isVerticalSOSBottomUp(i,j)||
            isVerticalSOSTopDown(i,j) ||
            
            isDiagonalSOSDownLeft(i,j) || 
            isDiagonalSOSUpRight(i,j) ||
            isDiagonalSOSUpLeft(i,j) ||
            isDiagonalSOSDownRight(i,j)) {
            cout << "Winning SOS formation found at: " << i << ", " << j << endl;

            // Award points to the player whose turn it is
            if (getTurnHistory() % 2 == 1) {
              // Update player1's score or any other relevant logic
              player1Points.insert({i, j});
              Play1.setPoints(player1Points.size());
            } else {
              // Update player2's score or any other relevant logic
              player2Points.insert({i, j});
              Play2.setPoints(player2Points.size());
            }

            // Mark the formation as detected
            detectedFormations[i][j] = true;

            // Set the extra turn flag to true
            extraTurn = true;
            usedS[i][j] = true;
          }
        }
      }
    }
  }

  if (extraTurn == true) {
    // If an SOS was formed and the player gets an extra turn, continue their turn
    turnNumber = turnNumber -1;
  }
}


bool game::gameEndCheck(Player P1, Player P2){
  int boardSpace = boardSize*boardSize;
  if(boardType == "GENERAL"){
    if(boardSpace != turnHistory.size()){
      return false;
    }
    else{
    if(boardSpace == turnHistory.size() && P1.getPoints() == P2.getPoints()){
      cout << "DRAW!" << endl;
      printBoard();
      sleep(2);
      clearBoard();
      return true;
    }
    if(boardSpace == turnHistory.size() && P1.getPoints() > P2.getPoints()){
      cout << "P1 WINS!" << endl;
      printBoard();
      sleep(2);
      clearBoard();
      return true;
    }
    if(boardSpace == turnHistory.size() && P1.getPoints() < P2.getPoints()){
      cout << "P2 WINS!" << endl;
      printBoard();
      sleep(2);
      clearBoard();
      return true;
    }
    }
  }
  else if(boardType == "SIMPLE"){
    if(boardSpace == turnHistory.size() && P1.getPoints() == P2.getPoints()){
      cout << "DRAW!" << endl;
      printBoard();
      sleep(2);
      clearBoard();
      return true;
    }
    if(P1.getPoints() > P2.getPoints()){
      cout << "P1 WINS!" << endl;
      printBoard();
      sleep(2);
      clearBoard();
      
      return true;
    }
    if(P2.getPoints() > P1.getPoints()){
      cout << "P2 WINS!" << endl;
      printBoard();
      sleep(2);
      clearBoard();
      return true;
    }
    return false;
  }

}



void game::ComputerMovePlacement(Player& Play){
  //cout << "Computer made a move" << endl;
  //ComputerTurn(Play)
  if(ComputerTurn(Play) == true){
    //cout << "Computer Offensive Move: " << Play.getColChoice() << "," << Play.getRowChoice() << endl;
  }
  else if(!ComputerSTrap(Play)){
    
    totallyRandom(Play);
    //cout << "Computer Defensive Move: " << Play.getColChoice() << "," << Play.getRowChoice() << endl;
    //randomGrid
  }
}


  bool game::ComputerTurn(Player& Play) {
      for (int i = 0; i < this->boardSize; i++) {
          for (int j = 0; j < this->boardSize; j++) {
              //cout << "Checking-1" << endl;
              if (gameBoard[i][j] == '_') {
                  //cout << "CheckingComputerTurn" << endl;

                  // O Placement
                  // Check Horizontal
                  if (j - 1 >= 0 && j + 1 < this->boardSize &&
                      ((gameBoard[i][j + 1] == 'S' && gameBoard[i][j - 1] == 'S') ||
                       (gameBoard[i][j - 1] == 'S' && gameBoard[i][j + 1] == 'S'))) {
                      cout << "CheckOHORIZONTAL" << endl;
                      setMoveAndReturn(Play, i, j, "O");
                    return true;
                  }

                  // Check Vertical
                  else if (i - 1 >= 0 && i + 1 < this->boardSize &&
                           ((gameBoard[i + 1][j] == 'S' && gameBoard[i - 1][j] == 'S') ||
                            (gameBoard[i - 1][j] == 'S' && gameBoard[i + 1][j] == 'S'))) {
                      cout << "CheckOVERTICAL" << endl;
                      setMoveAndReturn(Play, i, j, "O");
                    return true;
                  }

                  // Check Diagonally bottom Left to top right
                  else if (i - 1 >= 0 && j + 1 < this->boardSize && i + 1 < this->boardSize && j - 1 >= 0 &&
                           ((gameBoard[i - 1][j + 1] == 'S' && gameBoard[i + 1][j - 1] == 'S') ||
                            (gameBoard[i + 1][j - 1] == 'S' && gameBoard[i - 1][j + 1] == 'S'))) {
                      cout << "CheckOBOTLEFTTOPRIGHT" << endl;
                      setMoveAndReturn(Play, i, j, "O");
                    return true;
                  }

                  // Check Diagonally Top Left to bottom Right
                  else if (i - 1 >= 0 && j - 1 >= 0 && i + 1 < this->boardSize && j + 1 < this->boardSize &&
                           ((gameBoard[i - 1][j - 1] == 'S' && gameBoard[i + 1][j + 1] == 'S') ||
                            (gameBoard[i + 1][j + 1] == 'S' && gameBoard[i - 1][j - 1] == 'S'))) {
                      cout << "CheckOLEFTBOTRIGHT" << endl;
                      setMoveAndReturn(Play, i, j, "O");
                    return true;
                  }

                  // S placement
                  // Check Horizontal Left
                  else if (j - 2 >= 0 && j - 1 >= 0 && gameBoard[i][j - 1] == 'O' && gameBoard[i][j - 2] == 'S') {
                      cout << "CheckSLeft" << endl;
                      setMoveAndReturn(Play, i, j, "S");
                    return true;
                  }

                  // Check Horizontal Right
                  else if (j + 2 < this->boardSize && j + 1 < this->boardSize &&
                           gameBoard[i][j + 1] == 'O' && gameBoard[i][j + 2] == 'S') {
                      cout << "CheckSRight" << endl;
                      setMoveAndReturn(Play, i, j, "S");
                    return true;
                  }

                  // Check Diagonal Down Left
                  else if (i + 2 < this->boardSize && j - 2 >= 0 && j - 1 >= 0 &&
                           gameBoard[i + 1][j - 1] == 'O' && gameBoard[i + 2][j - 2] == 'S') {
                      cout << "CheckDDLEFT" << endl;
                      setMoveAndReturn(Play, i, j, "S");
                    return true;
                  }

                  // Check Diagonally Down to the right
                  else if (i + 2 < this->boardSize && j + 2 < this->boardSize && j + 1 < this->boardSize &&
                           gameBoard[i + 1][j + 1] == 'O' && gameBoard[i + 2][j + 2] == 'S') {
                      cout << "CheckDDRIGHT" << endl;
                      setMoveAndReturn(Play, i, j, "S");
                    return true;
                  }

                  // Check Diagonally Up Left
                  else if (i - 2 >= 0 && j - 2 >= 0 && i - 1 >= 0 && j - 1 >= 0 &&
                           gameBoard[i - 1][j - 1] == 'O' && gameBoard[i - 2][j - 2] == 'S') {
                      cout << "CheckDUPLEFT" << endl;
                      setMoveAndReturn(Play, i, j, "S");
                    return true;
                  }

                  // Check Diagonally Up Right
                  else if (i - 2 >= 0 && j + 2 < this->boardSize && i - 1 >= 0 &&
                           gameBoard[i - 1][j + 1] == 'O' && gameBoard[i - 2][j + 2] == 'S') {
                      cout << "CheckDUPRIGHT" << endl;
                      setMoveAndReturn(Play, i, j, "S");
                    return true;
                  }
              }
          }
      }

      //cout << "end";
      return false;
  }

void game::setMoveAndReturn(Player& Play, int i, int j, const string& choice) {
    string bestRow = to_string(i);
    string bestCol = to_string(j);
    Play.setMoveChoice(choice);
    Play.setMoveRow(bestRow);
    Play.setMoveCol(bestCol);
}
