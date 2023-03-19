import java.util.Scanner;

public class Board {
	// Need 9x9 cuz the first index is for scale
	private int inputPos, index1,index2;
	private char [][] board=new char[9][9];
	Position pos=new Position();
	Scanner sc=new Scanner(System.in);
	Game game; ///////directed association to the Game class
	private int numBlack, numWhite;
	private char numScaleV='1';
	private char numScaleH='1';
	UnplayablePosition unPos=new UnplayablePosition();
	
	
	// Board constructor; for load game
	public Board(String positions) {
		//System.out.println(numScaleH);
		for(int a=0;a<=8;a++) {
			for(int b=0;b<=8;b++) {
				if(a==0 && b==0) {
					board[a][b]=' ';
				}			
				//print horizontal scale
				else if(a==0 && b>0) {
					board[a][b]=numScaleH;
					numScaleH=(char)(numScaleH+1);			
				}else if(a>0 && b>0) {
					if(positions.charAt(8*(a-1)+(b-1))=='B'){
						board[a][b]=Position.BLACK;
					}else if(positions.charAt(8*(a-1)+(b-1))=='W') {
						board[a][b]=Position.WHITE;
					}else if(positions.charAt(8*(a-1)+(b-1))==' ') {
						board[a][b]=Position.EMPTY;
					}else {
						board[a][b]=UnplayablePosition.getUnplayable();
					}
				}			
			}
			// print vertical scale
			if(a>0) {				
				board[a][0]=numScaleV;	
				numScaleV=(char)(numScaleV+1);
			}
		}
		Position.setBoard(board);
	}
	// overload constructor for New game
	public Board() {
		
	}	
	
	//copyBoard is to check if there will be a space that can be converted(not checking a specific space)
	public Board copyBoard() {
		Board copyBoard=new Board();
		copyBoard.game=this.game;
		for(int a=1;a<=8;a++) {
			for(int b=1;b<=8;b++) {
				copyBoard.board[a][b]=this.board[a][b];
			}
		}
		return copyBoard;
	}
	
	//check it to see if it will be any available Valid Space, if not show the results
	public boolean checkCopyBoard() {
		Board copy = copyBoard();
		numBlack=0;
		numWhite=0;
		for(int a=1;a<=8;a++) {
			for(int b=1;b<=8;b++) {
				copy.index1=a;
				copy.index2=b;
				if(copy.pos.canPlay1(a, b) && copy.canConverts(a,b)) { //check if it's empty and if it's convertible
					return true;
				}
				else {
					if(copy.board[a][b]==Position.BLACK) {
						numBlack++;
					}else if(copy.board[a][b]==Position.WHITE) {
						numWhite++;
					}
				}
			}
		}
		if(numBlack>numWhite) {
			System.out.println("\n   "+game.getFirstPlayer()+" Win!\n");
		}
		else if(numBlack<numWhite) {
			System.out.println("\n   "+game.getSecondPlayer()+" Win!\n");
		}else {
			System.out.println("\n   Draw!\n");
		}		
		return false;
	}
	
	public void setGame(Game game) {
		this.game = game;   //set the this.game to the game(object) of the Game class
	}
		
	// only print the Board (2d array)
	public void drawBoard() {
		for(char[] arr:board) {
			for(char s :arr) {				
				System.out.print(" "+s+" ");				
			}
			System.out.println();
		}	
	}
	
	// set board(2d array)
	public void initBoard() {	
		//give the board spaces
		Scanner input=new Scanner(System.in);
		int boardChoice1,boardChoice2=0;	
		
		for(int a=0;a<=8;a++) {
			for(int b=0;b<=8;b++) {
				// print horizontal scale (char)
				if(a==0 && b>0) {
					board[a][b]=numScaleH;
					numScaleH=(char)(numScaleH+1);			
				}
				else {
					board[a][b]=Position.EMPTY;					
				}
				if(a==0 && b==0) {
					board[a][b]=' ';
				}						
			}
			// Show the unplayable spots
			board[8][4]=UnplayablePosition.getUnplayable();
			board[8][5]=UnplayablePosition.getUnplayable();
			// print vertical scale
			if(a>0) {				
				board[a][0]=numScaleV;	
				numScaleV=(char)(numScaleV+1);
			}				
		}	
		////////////////////////////////////////////////////
		boolean done=false;
		while(!done) {
			try {
				//Ask player to choose the starting position
				System.out.println("Please choose starting position: \n1. Offset  \n2. Four by Four");
				boardChoice1=input.nextInt();	
				if(boardChoice1==1 || boardChoice1==2) {
					while(!done) {
						try {
							if(boardChoice1==1) {
								while(!done) {
									System.out.println("Please choose Offset position (Enter number between 1-4): ");
									boardChoice2=input.nextInt();
									if(boardChoice2>=1 && boardChoice2<=4) {
										patternChoice(boardChoice1,boardChoice2);
										done=true;
									}
								}
							}
							else if(boardChoice1==2) {
								patternChoice(boardChoice1);
								done=true;
							}
						}catch(Exception e) {
							System.out.println("Please enter number!");
							input.nextLine();
						}
					}
				}
			}catch(Exception e) {
				System.out.println("Please enter number!");
				input.nextLine();
			}
		}	
		Position.setBoard(board);		
	}
		
	public void makeMove(Player currentP) {
		boolean hasMoved=false;		
			do {
				boolean done=false;
				while(!done) {
					try {
						// get the index/position of the board
						System.out.println(game.getCurrentPlayer()+", please enter your position (Vertical scale first, then Horizontal scale), between 1-8");
						inputPos=sc.nextInt();
						index1=inputPos/10;
						index2=inputPos-10*index1;
						if(index1<=8 && index1>=0 && index2<=8 && index2>=0) {
							if(board[index1][index2]==Position.EMPTY) {
								done=pos.canPlay(index1, index2);
							}
						}
						
					}catch(Exception e) {
						System.out.println("Please enter the scale number!");
						sc.nextLine();
					}
				}

				if(checkValidMove()) {
					//true
					board[index1][index2]=currentP.getSymbol();	
					hasMoved=true;
				}
				else {
					//false, noValidMove
				}
			}while(!hasMoved);
		}
	
	// to change the current player
	public void takeTurn(Player current) {
		if(current==game.getFirstPlayer()) {
			game.setCurrentPlayer(false);
		}
		else {
			game.setCurrentPlayer(true);
		}
	}
	
	//draw selected pattern1
	public void patternChoice(int boardChoice1) {
			board[3][3]=Position.WHITE;
			board[3][4]=Position.WHITE;
			board[3][5]=Position.BLACK;
			board[3][6]=Position.BLACK;
			board[4][3]=Position.WHITE;
			board[4][4]=Position.WHITE;
			board[4][5]=Position.BLACK;
			board[4][6]=Position.BLACK;
			
			board[5][3]=Position.BLACK;
			board[5][4]=Position.BLACK;
			board[5][5]=Position.WHITE;
			board[5][6]=Position.WHITE;
			board[6][3]=Position.BLACK;
			board[6][4]=Position.BLACK;
			board[6][5]=Position.WHITE;
			board[6][6]=Position.WHITE;
			
	}
	//draw selected pattern2
	public void patternChoice(int boardChoice1, int boardChoice2) {
		if(boardChoice1==1)
		{
			if(boardChoice2==1) {
				board[3][3]=Position.WHITE;
				board[3][4]=Position.BLACK;
				board[4][3]=Position.WHITE;
				board[4][4]=Position.BLACK;
			}
			else if (boardChoice2==2) {
				board[3][5]=Position.WHITE;
				board[3][6]=Position.BLACK;
				board[4][5]=Position.WHITE;
				board[4][6]=Position.BLACK;
			}
			else if (boardChoice2==3) {
				board[5][3]=Position.WHITE;
				board[5][4]=Position.BLACK;
				board[6][3]=Position.WHITE;
				board[6][4]=Position.BLACK;
			}
			else if (boardChoice2==4) {
				board[5][5]=Position.WHITE;
				board[5][6]=Position.BLACK;
				board[6][5]=Position.WHITE;
				board[6][6]=Position.BLACK;
			}
		}	
	}
	
	public char[][] getBoard() {
		return board;
	}
	
	// check there's any Valid Move (left)
	public boolean checkValidMove() {
		if(pos.canPlay(index1,index2) && canConverts(index1,index2)) {
				//T&T
			return true;
		}
		else {
				// T&F, F&T, F&F		
			return false;
		}	
	}
		
		public boolean canConverts(int index1, int index2) { 	
			//check if it's on the Unplayable Position
			if(index1==8 && (index2==4 || index2==5)){
				return unPos.canPlay(index1, index2);
			}
			//check if it's convert
			else if(convertUp()||convertDown()||convertLeft()||convertRight()||convertDiaUpRight()||convertDiaUpLeft()||convertDiaDownRight()||convertDiaDownLeft()) {
				// check if there's other directions can convert
				convertUp();
				convertDown();
				convertLeft();
				convertRight();
				convertDiaUpLeft();
				convertDiaUpRight();
				convertDiaDownLeft();
				convertDiaDownRight();
							
				return true;
			}
			else {
				// no other pieces can be converted
				return false;
			}	
		}		
		//while checking, if it's return true, it converts at the same time
		public boolean convertUp() { /// depends on index1, index2 don't change
			for(int a=index1;a>1;a--) {
				if(index1>=3) {
					//the previous space (the direction you want to convert) shouldn't be empty and the same colour as the current player
					if(board[a-1][index2] !=Position.EMPTY && board[a-1][index2] !=game.getCurrentPlayer().getSymbol()) {
						//the second space of the checked space needs to be the same colour as the current player
						if(game.getCurrentPlayer().getSymbol()==board[a-2][index2]) {						
							// count=how many pieces is between the 2 currentPlayer's symbol. You reach to the 'board[a-2][index2]', then goes back to flip the pieces	
							for(int count=index1-a+1;count>0;count--) {
								board[a-1][index2]=game.getCurrentPlayer().getSymbol();
								a++;
							}
							return true;
						}
					}else 
						return false;		
				}			
			} 
				return false; // touch the board/ second line
		}
		
		public boolean convertDown() {
			for(int a =index1;a<7;a++) {
				if(index1<=6 && (index2!=4 || index2!=5)) { //normal space except index2=4,5
					if(board[a+1][index2]!=Position.EMPTY && game.getCurrentPlayer().getSymbol()!=board[a+1][index2]) {
						if( game.getCurrentPlayer().getSymbol()==board[a+2][index2]) {
							for(int count=a-index1+1;count>0;count--) {
								board[a+1][index2]=game.getCurrentPlayer().getSymbol();
								a--;
							}
							return true;
						}
					}else
						return false;		
				}
				//Unplayable position
				else if(index1<=5 && (index2==4 || index2==5)) {
					if(game.getCurrentPlayer().getSymbol()!=board[a+1][index2] && board[a+1][index2]!=Position.EMPTY) {
						if( game.getCurrentPlayer().getSymbol()==board[a+2][index2]) {
							for(int count=a-index1+1;count>0;count--) {
							board[a+1][index2]=game.getCurrentPlayer().getSymbol();
							a--;
						}
							return true;
						}
					}
					return false;
				}
			}
			return false;
		}
		
		public boolean convertLeft() {  //depends on index2, index1 dont change
			for(int a =index2;a>1;a--)
			{
				if(index2>=3 && index1!=8) {
					if(game.getCurrentPlayer().getSymbol()!=board[index1][a-1] && board[index1][a-1]!=Position.EMPTY) {
						if( game.getCurrentPlayer().getSymbol()== board[index1][a-2]) {
							for(int count=index2-a+1;count>0;count--) {
								board[index1][a-1]=game.getCurrentPlayer().getSymbol();
								a++;
							}
							return true;
						}
					}else
						return false;			
				}else if(index1==8 && (index2==3 || index2==8) ) {
					if(game.getCurrentPlayer().getSymbol()!=board[index1][a-1] && board[index1][a-1]!= Position.EMPTY) {
						if( game.getCurrentPlayer().getSymbol()== board[index1][a-2]) {					
							for(int count=index2-a+1;count>0;count--) {
								board[index1][a-1]=game.getCurrentPlayer().getSymbol();
								a++;
							}
							return true;
						}
					}else
						return false;				
				}	
			}
			return false;
		}
		
		public boolean convertRight() {
			for(int a= index2;a<=6;a++) {
				if(index2<=6 && index1!=8) {
					if(game.getCurrentPlayer().getSymbol()!=board[index1][a+1] && board[index1][a+1]!=Position.EMPTY ) {
						if( game.getCurrentPlayer().getSymbol()==board[index1][a+2]) {					
							for (int count=a-index2+1;count>0;count--) {
								board[index1][a+1]=game.getCurrentPlayer().getSymbol();
								a--;
							}
							return true;
						}
					}
					else
						return false;				
				}else if(index1==8 && (index2==1 || index2==6)) {
					if(game.getCurrentPlayer().getSymbol()!=board[index1][a+1] && board[index1][a+1]!=Position.EMPTY) {
						if(game.getCurrentPlayer().getSymbol()==board[index1][a+2]) {
							for (int count=a-index2+1;count>0;count--) {
								board[index1][a+1]=game.getCurrentPlayer().getSymbol();
								a--;
							}
							return true;
						}
					}else
						return false;			
				}
			}
			return false;
		}
		
		public boolean convertDiaUpRight() {
			int b=index2;
			for(int a=index1; a>1;a--) {  //a-2>=0 --> a>=2 / a>1
				if(a<=2 || b>=7) {
					break;
				}
				if(index1>=3 && index2<=6 ) {
					if(game.getCurrentPlayer().getSymbol()!=board[a-1][b+1] && board[a-1][b+1]!=Position.EMPTY ) {
						if(game.getCurrentPlayer().getSymbol()==board[a-2][b+2]) {
							for(int count=index1-a+1; count>0;count--) {
								board[a-1][b+1]=game.getCurrentPlayer().getSymbol();
								b--;
								a++;
							}
							return true;
						}
					}else
						return false;
				}		
				b++;
			}
			return false;
		}
		
		public boolean convertDiaUpLeft() {
			int b=index2;
			for(int a=index1; a>1;a--) {
				if(a<=2 ||b<=2) {
					break;
				}
				if(index1>=3 && index2>=3) {
					if(game.getCurrentPlayer().getSymbol()!=board[a-1][b-1] && board[a-1][b-1]!=Position.EMPTY ) {
						if(game.getCurrentPlayer().getSymbol()==board[a-2][b-2]) {
							for(int count=index1-a+1;count>0;count--) {
								board[a-1][b-1]=game.getCurrentPlayer().getSymbol();
								a++;
								b++;
							}
							return true;
						}
					}else
						return false;
				}
				b--;
			}
			return false;
		}
		
		public boolean convertDiaDownRight() {
			int b=index2;
			for(int a=index1; a<8; a++) {  // a+2<=9 -->a<=7/ a<8
				if(a>=7 || b>=7) {
					break;
				}
				if(index1<=6 && index2<=6) {
					if(game.getCurrentPlayer().getSymbol()!=board[a+1][b+1] && board[a+1][b+1]!=Position.EMPTY) {
						if(game.getCurrentPlayer().getSymbol()==board[a+2][b+2]) {
							//System.out.println(a + " " + b);
							for(int count=a-index1+1; count>0;count--) {
								board[a+1][b+1]=game.getCurrentPlayer().getSymbol();
								a--;
								b--;
							}
							return true;
						}
					} else
						return false;
				}
				b++;
			}
			return false;				
		}
		public boolean convertDiaDownLeft() {
			int b=index2;
			for(int a=index1;a<8;a++) {
				if(a>=7 || b<=2) {
					break;
				}
				if(index1<=6 && index2>=3) {
					if(game.getCurrentPlayer().getSymbol()!=board[a+1][b-1] && board[a+1][b-1]!=Position.EMPTY) {
						if(game.getCurrentPlayer().getSymbol()==board[a+2][b-2]) {
							for(int count=a-index1+1;count>0;count--) {
								board[a+1][b-1]=game.getCurrentPlayer().getSymbol();
								a--;
								b++;
							}
							return true;
						}
					}else
						return false;
				}
				b--;
			}
			return false;
		}
}
