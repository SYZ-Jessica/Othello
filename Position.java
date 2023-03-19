
 public class Position { ///PlayablePosition class
	public static final char EMPTY=' ';
	public static final char BLACK='B';
	public static final char WHITE='W';
	private static Board board;
	private static char[][] copyBoard;

	
	public Position() {
		
	}	
	// have a shallow copy of the board, to check is there's an empty space
	public static void setBoard(char[][] board) {
		copyBoard = board;
	}
	
	// check if a specific place is empty
	public boolean canPlay(int index1, int index2) {
			return true;
	}
	
	// this one is used to check is the specific place one the copyBoard is empty
	public boolean canPlay1(int index1, int index2) {
		//check if it's out of boundary
		if(index1<0 ||index2<0 || index1>8 || index2>8) {
			return false;
		}
		// check if it's choose on an empty space
		else if(copyBoard[index1][index2]!=EMPTY) {
			return false;
		}
		//if it's empty space
		else {
			return true;
		}	
	}	
}

