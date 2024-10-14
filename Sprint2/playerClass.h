#include "library.h"

class Player {
private:
    int points = 0;

    char moveChoice = '_';

    int moveRow = 0;
    int moveCol = 0;
    int playerNum;
    vector<string> moveHistory;

    string Type = "";

public:
    int getPlayerNum(){
      return this->playerNum;
    };

    void setPlayerType(string userInput){
      this->Type = userInput;
    }

    string getPlayerType(){
      return this->Type;
    }

    void setPlayerNum(int userNum){
      this->playerNum = userNum;
    }

    void setPoints(int Size){
      this->points = Size;
    }

    int getPoints() {
        return this->points;
    };
    void addPoint() {
        this->points += 1;
    }

    int getRowChoice() {
        return this->moveRow;
    }

    int getColChoice() {
        return this->moveCol;
    }

    char getMoveChoice() {
        return this->moveChoice;
    }

    void setMoveChoice(string userMove1) {
        this->moveChoice = userMove1[0];
    }
    void setMoveRow(string userMove1) {
        this->moveRow = stoi(userMove1);
    }
    void setMoveCol(string userMove1) {
        this->moveCol = stoi(userMove1);
    }


    void playerMove(string& userMove, int boardSize);

    //void ComputerMove(game SOS);

};


class Computer: public Player{
  void MakeAMove(string& userMove);

};
