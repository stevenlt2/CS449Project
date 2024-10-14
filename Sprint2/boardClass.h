#include "library.h"
#include "playerClass.h"

class game{
  private:
    int boardSize = 3;
    string boardType = "";
    int turnNumber = 1;
    vector<vector<char>> gameBoard;
    vector<vector<bool>> detectedFormations;
    vector<vector<bool>> detectedOFormations;
    vector<vector<bool>> usedS;
    vector<string> gameMoveHistory;
    vector<int> turnHistory;
    int totalPoints = 0;
    bool gameEnd = false;

    set<pair<int, int>> player1Points;
    set<pair<int, int>> player2Points;

  public:

    const vector<vector<char>>& getGrid() const {
        return this->gameBoard;
    }




    void menu();
    void boardPlacement(Player play);
    
    int getBoardSize(){
      return this->boardSize;
    };

    void pushGameMoveHistory(string user){
      this->gameMoveHistory.push_back(user);
    };

    void getGameHistory(){
      this->gameMoveHistory.back();
    };

    string getBoardType(){
      return this->boardType;
    }




    void setTurnNumber(int userNum){
      this->turnNumber = userNum;
    }
    int getTurnNumber(){
      return this->turnNumber;
    }


    bool returnPlayerTurn(){
      if(turnNumber % 2 == 1){
        return true;
      }
      else
        return false;
    }
    void addTurnNumber(){
      this->turnNumber = this->turnNumber + 1;
    }

    bool isSOS(int row, int col);

    void checkWin(Player& play, Player& play2);




    void setBoardType(string userInput){
      this->boardType = userInput;
    }
    
    void setBoardSize(int userSize);

    void printBoard();

    void gameMove(int Row, int Col, char Choice, int color);

    void pushbackMove(string userMove, int color){
      string playerMove = to_string(color) + userMove;
      this->gameMoveHistory.push_back(playerMove);
    }

    void gameplay();
    void clearBoard();


    int getTurnHistory(){
      return this->turnHistory.back();
    }
    int getTurnHistorySize(){
      return this->turnHistory.size();
    }
    void clearTurnHistory(){
      this->turnHistory.clear();
    }

    template <typename T>
    T getGameBoard(){
      return gameBoard;
    };

  void winDetection(Player &Play);

  bool gameEndCheck(Player P1, Player P2);

  bool isHorizontalSOS(int row, int col);
  bool isVerticalSOS(int row, int col);

  bool isHorizontalSOSForwards(int row, int col);
  bool isHorizontalSOSBackwards(int row, int col);

  bool isVerticalSOSTopDown(int row, int col);  
  bool isVerticalSOSBottomUp(int row, int col);
  bool isDiagonalSOS(int row, int col);
  bool isAntiDiagonalSOS(int row, int col);

  bool isDiagonalSOSDownLeft(int row,int col);
  bool isDiagonalSOSDownRight(int row,int col);
  bool isDiagonalSOSUpLeft(int row,int col);
  bool isDiagonalSOSUpRight(int row,int col);

  bool ComputerTurn(Player& play);
  bool ComputerSTrap(Player&Play);
  bool carefulSPlacement(Player& Play, int i, int j);
  bool carefulOPlacement(Player& Play, int i, int j);
  void ComputerMovePlacement(Player& Play);
  void ComputerRandomPlacement(Player& Play);
  void totallyRandom(Player& Play);
  void setMoveAndReturn(Player& Play, int i, int j, const string& choice);
  bool isWinningMoveForPlayer(int row, int col, Player& Play);
  bool checkWinCondition(int row, int col, const vector<vector<char>>& tempBoard, Player& Play);

  bool isHorizontalSOS2(int row, int col, const vector<vector<char>>& tempBoard);
  bool isVerticalSOS2(int row, int col, const vector<vector<char>>& tempBoard);
  bool isDiagonalSOS2(int row, int col, const vector<vector<char>>& tempBoard);
  bool isAntiDiagonalSOS2(int row, int col, const vector<vector<char>>& tempBoard);

};
