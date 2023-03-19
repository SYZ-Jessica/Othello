
public class Player {
	private String name;
	private static int number=1;
	private char symbol;
	
	public Player() {
		
	}
	
	// Player constructor, assign player's name and symbol
	public Player(String name) {
		
		this.name=name;
		try {
			if(number==2) {
				setSymbolW();
			}
			else if (number==1) {
				setSymbolB();
			}
			// in case the number will be incremented over 2
			else 
				throw new Exception("number more than 2");
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		number++;
	}

	public String getName() {
		return name;
	}
	

	public char getSymbol() {
		return symbol;
	}
	
	public void setSymbolW() {
		this.symbol = 'W';
	}
	public void setSymbolB() {
		this.symbol = 'B';
	}

	@Override
	public String toString() {
		return getName();
	}	
}
