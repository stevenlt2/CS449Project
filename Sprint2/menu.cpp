#include "menu.h"
#include "library.h"
#include "gameLogic.h"

void menu(game& SOS) {
    string userInput = " ";
    string userBoardType = "Unknown";
    int turnNumber = 0;
    
    
    SOS.setBoardSize(3);
    SOS.setBoardType(userBoardType);
  
    while (userInput != "Q") {
      printf("\033c");
        cout << "\033[1;31m" << "SOS: The Game " << "\033[0m" << endl;
        SOS.printBoard();
        cout << endl;    
        cout << "C - Change Board Size" << endl
                << "G - Change Game Type" << endl
                << "R - Replay Game" << endl
                //<< "S - Start Game" << endl
                << "V - Start Game" << endl
                << "Q - Quit" << endl;
        cout << "Enter an input: ";
        cin >> userInput;
        
        switch (toupper(userInput[0])) {
        case 'C':
            menuBoardSize(SOS);
            break;
        case 'G':
            menuBoardType(SOS);
            break;
        case 'V':
            if(SOS.getBoardType() == "Unknown"){
              menuBoardType(SOS);
            }
            menuComputerGameplay(SOS);
            break;
        case 'R':
            break;
          
        case 'S':
            if(SOS.getBoardType() == "Unknown"){
              menuBoardType(SOS);
            }
            menuGameplay(SOS);
            
            break;
        default:
            if (userInput != "Q") {
                printf("\033c");
                cout << "Not a valid input - Enter another input: " << endl;
                break;
            }
            
        }

    }
    if (userInput == "Q") {
        exit(0);
    }

}


void menuBoardType(game& SOS){
  string userInput;
  while (true) {
            cout << "Please enter 'General' or 'Simple': ";
            cin >> userInput;
            for (char &c : userInput) {
            c = toupper(c);
            }
    
            if (userInput == "GENERAL" || userInput == "SIMPLE") {
              SOS.setBoardType(userInput);
              break; // Input is valid, exit the loop
            } else {
              cout << endl;
              cout << "Invalid input. Please enter 'General' or 'Simple'." << endl;
            }
          }

}

void menuBoardSize(game& SOS){
  int userSize1;
  while (true) {
            cout << "Please input a board size from 3 to 9: ";
            cin >> userSize1;
    
            if (userSize1 >= 3 && userSize1 <= 9) {
              SOS.setBoardSize(userSize1);
              break;
            } 
            else {
              cout << endl;
              cout << "Invalid input. Please enter a number between 3 to 9" << endl;
            }
    }
}

void menuComputerGameplay(game& SOS){
  string userInputPlayer1;
  string userInputPlayer2;
  string userInput;
  
  while (true) {
              cout << "Is Player 1 (RED): Human or Computer? ";
              cin >> userInputPlayer1;
              for (char &c : userInputPlayer1) {
              c = toupper(c);
              }

              if (userInputPlayer1 == "HUMAN" || userInputPlayer1 == "COMPUTER") {
                
               
                break; // Input is valid, exit the loop
              } else {
                cout << endl;
                cout << "Invalid input. Please enter 'Human' or 'Computer'." << endl;
              }
 }

  while (true) {
                cout << "Is Player 2 (BLUE): Human or Computer? ";
                cin >> userInputPlayer2;
                for (char &c : userInputPlayer2) {
                c = toupper(c);
                }

                if (userInputPlayer2 == "HUMAN" || userInputPlayer2 == "COMPUTER") {

                  break; // Input is valid, exit the loop
                } else {
                  cout << endl;
                  cout << "Invalid input. Please enter 'Human' or 'Computer'." << endl;
                }
   }

  Player P1;
  Player P2;

  P1.setPlayerNum(1);
  P1.setPlayerType(userInputPlayer1);

  P2.setPlayerNum(2);
  P2.setPlayerType(userInputPlayer2);
  
  /*if (userInputPlayer1 == "HUMAN") {
      
  } else if (userInputPlayer1 == "COMPUTER") {
      P1 = Computer P1; // Create a Computer object and assign it to P1
  }

  if (userInputPlayer2 == "HUMAN") {
      P2.setPlayerNum(2);
  } else if (userInputPlayer2 == "COMPUTER") {
      P2 = Computer P2; // Create a Computer object and assign it to P2
  }*/
  

  //const type_info& type1 = typeid(P1);
  //const type_info& type2 = typeid(P2);

  SOS.clearTurnHistory();
  while(userInput != "F" && SOS.gameEndCheck(P1,P2) != true){
    string userMove;
    cout << endl;
    cout << "SOS: " << SOS.getBoardType() << endl;
    cout << "P1: " << P1.getPoints() << " P2: " << P2.getPoints() << endl;
    cout << P1.getPlayerType() << " | " << P2.getPlayerType() << endl;
    cout << "Turn #" << SOS.getTurnHistorySize() << endl;
    SOS.printBoard();
    if(SOS.returnPlayerTurn() == 1){
      cout << "\033[1;31m" << "P1 Turn: " << "\033[0m" << endl;
      if(P1.getPlayerType() == "HUMAN"){
        cout << "M - Make a Move " << endl
          << "F - Forfeit" << endl;
        cout << "Enter an input: ";
        cin >> userInput;
      }
      else{
        cout << "P1 COMPUTER PREVIOUS MOVE: " << P1.getMoveChoice() << "," << P1.getRowChoice() << "," << P1.getColChoice() << endl;
      }
    }
    else{
      cout << "\033[1;34m" << "P2 Turn: " << "\033[0m" << endl;
      if(P2.getPlayerType() == "HUMAN"){
        cout << "M - Make a Move " << endl
          << "F - Forfeit" << endl;
        cout << "Enter an input: ";
        cin >> userInput;
      }
      else{
        cout << "P2 COMPUTER PREVIOUS MOVE: " << P2.getMoveChoice() << "," << P2.getRowChoice() << "," << P2.getColChoice() << endl;
      }
    }


    if(SOS.getTurnNumber() % 2 == 1){
      //P1.MakeAMove(userMove);
      MenuMove(P1, SOS);

      SOS.boardPlacement(P1);
      
      //P1.setMoveChoice("S");
      //P1.setMoveRow("0");
      //P1.setMoveCol("2");
      
      //SOS.boardPlacement(P1);
      sleep(1);
      SOS.checkWin(P1, P2);
    }
    else{
      //P2.MakeAMove(userMove);
      MenuMove(P2, SOS);
      SOS.boardPlacement(P2);
      sleep(1);
      SOS.checkWin(P1, P2);
    }



    /*switch (toupper(userInput[0])) {
    case 'M':
      if(SOS.getTurnNumber() % 2 == 1){
        MenuMove(P1, SOS);
        SOS.boardPlacement(P1);
        SOS.checkWin(P1, P2);

      }
      else{
        MenuMove(P2, SOS);
        SOS.boardPlacement(P2);
        SOS.checkWin(P1, P2);

      }
      break;

    case 'F':
      printf("\033c");
      if(SOS.getTurnHistory() % 2 == 1){
        SOS.clearBoard();
        cout << "Player 1 WINS!" << endl;
        sleep(2);
      }
      else{
        SOS.clearBoard();
        cout << "Player 2 WINS!" << endl;
        sleep(2);
      }

      break;
    default:
        if (userInput != "F") {
            cout << "Not a valid input - Enter another input: " << endl;
            break;
        }

    }
  }*/

  
}
}


void menuGameplay(game& SOS){
  string userInput;
  Player P1;
  P1.setPlayerNum(1);
  Player P2;
  P2.setPlayerNum(2);
  SOS.clearTurnHistory();
  //SOS.setTurnNumber(1);
  
  while (userInput != "F" && SOS.gameEndCheck(P1,P2) != true) {
        //printf("\033c");
        cout << endl;
        cout << "SOS: " << SOS.getBoardType() << endl;
        cout << "P1: " << P1.getPoints() << " P2: " << P2.getPoints() << endl;
        cout << "Turn #" << SOS.getTurnHistorySize() << endl;
        SOS.printBoard();

        if(SOS.returnPlayerTurn() == 1){
          cout << "\033[1;31m" << "P1 Turn: " << "\033[0m" << endl;
          
        }
        else{
          cout << "\033[1;34m" << "P2 Turn: " << "\033[0m" << endl;
        }

        cout << "M - Make a Move " << endl
                << "F - Forfeit" << endl;
    
        cout << "Enter an input: ";
        cin >> userInput;
        
        switch (toupper(userInput[0])) {
        case 'M':
          if(SOS.getTurnNumber() % 2 == 1){
            MenuMove(P1, SOS);
            SOS.boardPlacement(P1);
            
            
            //SOS.boardPlacement(P1);
            SOS.checkWin(P1, P2);
            
          }
          else{
            MenuMove(P2, SOS);
            SOS.boardPlacement(P2);
            SOS.checkWin(P1, P2);
            
          }
          break;





          
         /* if(SOS.getTurnNumber() % 2 == 0){
            //P1.playerMove
            MenuMove(P1, SOS);
            SOS.boardPlacement(P1);
            //SOS.addTurnNumber();
          }
          else
            //P2.playerMove 
            MenuMove(P2, SOS);
            SOS.boardPlacement(P2);
            //SOS.addTurnNumber();
          //SOS.addTurnNumber();
          break;*/

          
        case 'F':
          printf("\033c");
          if(SOS.getTurnHistory() % 2 == 1){
            SOS.clearBoard();
            cout << "Player 1 WINS!" << endl;
            sleep(2);
          }
          else{
            SOS.clearBoard();
            cout << "Player 2 WINS!" << endl;
            sleep(2);
          }



          
          /*if(SOS.returnPlayerTurn() == 1){
            
            P1.addPoint();
            SOS.clearBoard();
            cout << "Player 2 WINS!" << endl;
            sleep(2);
            
          }
          else{
            
            P2.addPoint();
            SOS.clearBoard();
            cout << "Player 1 WINS!" << endl;
            sleep(2);
          }*/
          
          break;
        default:
            if (userInput != "F") {
                cout << "Not a valid input - Enter another input: " << endl;
                break;
            }
            
        }

    }

}

void MenuMove(Player& Play, game& SOS){
  string userMove;
  string userInput;
  
  if(Play.getPlayerType() == "HUMAN"){
    string userInput;
    cout << "Enter a move using the format LETTER,X,Y" << endl;

    vector<string> v;
    while(true){
    v.clear();
    userInput = "";
    cin >> userInput;
    stringstream ss(userInput);

      while (ss.good()) {
          string substr = "";
          getline(ss, substr, ',');
          v.push_back(substr);
      }

      if ((v[0] == "S" || v[0] == "O") && (v[0].length() == 1)
        && ((stoi(v[2])) <= SOS.getBoardSize()) 
        && ((stoi(v[1])) <= SOS.getBoardSize())){
        Play.setMoveChoice(v[0]);
        Play.setMoveRow(v[1]);
        Play.setMoveCol(v[2]);
        break;
    }else{
      v.clear();
    }
  }
  }
  else if(Play.getPlayerType() == "COMPUTER"){
    SOS.ComputerMovePlacement(Play);
    /*//Play.ComputerMove(SOS);
    CompMove(Play);
    cout << "C1";
    string bestRow = Play.getMoveRow();
    string bestCol = Play.getMoveCol();
    string bestChoice = Play.getMoveChoice();
    //SOS.calculateMove(bestRow, bestCol, bestChoice, SOS);
    Play.setMoveChoice(bestChoice);
    Play.setMoveRow(bestRow);
    Play.setMoveCol(bestCol);*/

    //int bestRow = -1;
    //int bestCol = -1;

    // Randomly choose 'S' or 'O'
    //string computerChoice = (rand() % 2 == 0) ? "S" : "O";
    
    // Set the best move for the computer opponent
    //Play.setMoveChoice(computerChoice);
    //Play.setMoveRow(to_string(bestRow));
    //Play.setMoveCol(to_string(bestCol));
  }

}
