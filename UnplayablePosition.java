
public class UnplayablePosition extends Position{
	final public static char Unplayable='*';
	
	@Override
	public boolean canPlay(int index1, int index2) {
		return false;
	}

	public static char getUnplayable() {
		return Unplayable;
	}

	
}
