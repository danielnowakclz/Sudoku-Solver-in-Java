import java.util.HashSet;

public class Field {

	public int number = 0;
	public HashSet<Integer> possibleNumbers = new HashSet<>();

	public Field() {
		for (int i = 1; i <= 9; i++)
			possibleNumbers.add(i);
	}

}
