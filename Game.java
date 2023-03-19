import java.util.Scanner;
import java.io.*;

public class Game {
	static Player firstPlayer, secondPlayer, currentPlayer;
	Position pos=new Position();
	private Board board;
	private int choice;	
	Scanner sc=new Scanner(System.in);
	
	private int menuChoice;
	String playerName1, playerName2,fileName;	
	Scanner input=new Scanner(System.in);
	
	public Game(Player p1,Player p2) {
		firstPlayer=p1;
		secondPlayer=p2;
		currentPlayer=p1;
		board=new Board();
		board.setGame(this); //this means the instance of the Game class
		
	}
	// get called from Driver
	public Game() {
		
	}
	
	public void start() {
		boolean done=false;
		while(!done) {
			try {
				//print menu, ask user to choose
				System.out.println("Please choose: \n1. Start a new game \n2. Load a Game \n3. Quit ");
				menuChoice=input.nextInt();
				if(menuChoice>=1 && menuChoice<=3) {
					done=true;
				}
			}catch(Exception e) {
				System.out.println("Please input number!");
				input.nextLine();
			}
		}
			//use switch statement to select the menu
			switch(menuChoice) {
				case 1:
					//clear the Scanner
					input.nextLine();
					System.out.println("Please enter Player1's name: ");
					playerName1=input.nextLine();
					System.out.println("Please enter Player2's name: ");
					playerName2=input.nextLine();
					
					Player player1=new Player(playerName1);
					Player player2=new Player(playerName2);
					Game game=new Game(player1,player2);
					game.board.initBoard();
					//run the game
					game.play();				
					break;
					
				case 2:
					//load game				
					Game ga=new Game(firstPlayer,secondPlayer);	
					ga.board=load();
					ga.board.setGame(ga);					
					ga.play();
					break;
				case 3:
					System.out.println("Quit Game.");
					break;
			}
	}

	//needs to loop
	public void play() {
		// if user can move  1. save 2.  concede 3. make a move
			while(board.checkCopyBoard()) { //check CopyBoard so that it will stop when it finds there's no space that is playable(can convert)
				//printBoard
				board.drawBoard();
				boolean done=false;
				while(!done) {
					try {
						System.out.println("\n"+currentPlayer.getName()+", please choose \n1. Save \n2. Concede \n3. Make a move \n4. Forfeit");
						choice=sc.nextInt();
						done=true;
					}catch(Exception e) {
						System.out.println("Please enter numebr!");
						sc.nextLine();
					}
				}				
				if(choice==1) {
					// Save game
					save();
					System.out.println("Saved");
					System.exit(0); //end the game
				}
				else if(choice==2) {
					// concede
					System.out.println(currentPlayer.getName()+" Lose");
					System.out.println("Game End.");
					System.exit(0);
				}
				else if(choice==3) {
					// make a move,then change the currentPlayer
					board.makeMove(currentPlayer);
					board.takeTurn(currentPlayer);		
				}
				else if(choice==4) {
					// forfeit and switch the currentPlayer
					board.takeTurn(currentPlayer);
					System.out.println("\nSwitch Player\n");		
				}
			}
		// Show the state of the board after the game is finished
		board.drawBoard();
	}	
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	public void setCurrentPlayer(boolean isFirstPlayer) {
		currentPlayer = (isFirstPlayer) ? firstPlayer : secondPlayer;
	}
	
	//Save Game
	private void save() {
		try {
			System.out.println("Please enter file name: ");
			String fileName=input.nextLine();
			File file=new File(fileName+".txt");
			if(!file.exists()) {
				// create a new file
				System.out.println("File does not exist!");
				System.out.println("New File Created!");
				file.createNewFile();
			}				
				Scanner scan=new Scanner(file);
				StringBuffer sb=new StringBuffer();
				FileWriter fw=new FileWriter(file,false); //false: it will clear the file every time before you save the new stuff
				PrintWriter pw=new PrintWriter(fw);
				String info;
				info=firstPlayer+"\n"+secondPlayer+"\n"+currentPlayer+"\n";
				char[][] fileArr=board.getBoard();
				String arr="";
				for(int a=1;a<=8;a++) {
					for(int b=1;b<=8;b++) {
						if(fileArr[a][b]==Position.EMPTY) {
							arr+=Character.toString(fileArr[a][b]);
						}else if(fileArr[a][b]==Position.BLACK) {
							arr+=Character.toString(fileArr[a][b]);
						}
						else if(fileArr[a][b]==Position.WHITE) {
							arr+=Character.toString(fileArr[a][b]);
						}
						else {
							arr+=Character.toString(fileArr[a][b]);
						}
					}
				}
				pw.write(info);
				pw.write(arr);
				pw.flush();			
		}catch(IOException e) {
			System.out.println("File error.");
		}catch(Exception e) {
			System.out.println("Error.");
		}
	}
	// load file
	public static Board load() {
		Scanner in=new Scanner(System.in);
		boolean done=false;
		while(!done) {
			try {
				System.out.println("Please enter a saved File name: ");
				String fileName=in.nextLine();	
				File savedFile=new File(fileName+".txt");
				Scanner scan1=new Scanner(savedFile);
					//throw new Exception("File not found!");
				if(scan1.hasNextLine()) {
					firstPlayer=new Player(scan1.nextLine());
					firstPlayer.setSymbolB();
				}
				if(scan1.hasNextLine()) {
					secondPlayer=new Player(scan1.nextLine());
					secondPlayer.setSymbolW();
				}
				if(scan1.hasNextLine()) {
					String thirdLine=scan1.nextLine();
					if(thirdLine.equals(firstPlayer.getName())) {
						currentPlayer=firstPlayer;
					}else {
						currentPlayer=secondPlayer;
					}
				}
				if(scan1.hasNextLine()) {
					//call constructor in Board
					Board loadBoard=new Board(scan1.nextLine());			
					return loadBoard;
				}
				done=true;
			}catch(FileNotFoundException e) {
				System.out.println("File Not Found!");
			}
			catch(IOException e) {
				System.out.println("Something is wrong...");
			}
			catch(Exception e) {
				System.out.println("Error.");
			}
		}		
		return null;		
	}
	public Player getFirstPlayer() {
		return firstPlayer;
	}
	public void setFirstPlayer(Player p1) {
		firstPlayer = p1;
	}
	public Player getSecondPlayer() {
		return secondPlayer;
	}
	public void setSecondPlayer(Player p2) {
		secondPlayer = p2;
	}
	
	
}
