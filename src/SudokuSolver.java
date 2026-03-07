import java.util.Iterator;
import java.util.Vector;

public class SudokuSolver {

	private static Vector<Sudoku> sudokuField = new Vector<>();

	public static void main(final String[] args) {
		final Sudoku sudoku = new Sudoku();
		
		final int[] field = { 
				
				0,0,0, 0,0,0, 0,0,0,
				0,0,0, 0,0,0, 0,0,0,
				0,0,0, 0,0,0, 0,0,0,
				
				0,0,0, 0,0,0, 0,0,0,
				0,0,0, 0,0,0, 0,0,0,
				0,0,0, 0,0,0, 0,0,0,
				
				0,0,0, 0,0,0, 0,0,0,
				0,0,0, 0,0,0, 0,0,0,
				0,0,0, 0,0,0, 0,0,0 
				
		};

		for (int i = 0; i < field.length; i++)
			if (field[i] != 0)
				sudoku.setIN(i, field[i]);
		sudokuField.add(sudoku);
		solve();
	}

	private static void solve() {
		while (sudokuField.size() > 0) {
			final Sudoku sudoku = sudokuField.remove(0);
			int index = sudoku.nextSolvedFieldIndex();
			while (index > -1) {
				int y = index / 9;
				int x = index - 9 * y;
				y++;
				x++;
				final int number = sudoku.sudoku.elementAt(index).possibleNumbers.iterator().next();
				sudoku.setXYN(x, y, number);
				index = sudoku.nextSolvedFieldIndex();
			}
			if (sudoku.solved()) {
				sudoku.print();
				System.exit(0);
			} else {
				index = sudoku.nextSplitFieldIndex();
				if (index > -1) {
					final Field split = sudoku.sudoku.elementAt(index);
					final Iterator<Integer> iter = split.possibleNumbers.iterator();
					while (iter.hasNext()) {
						final int number = iter.next();
						final Sudoku element = new Sudoku();
						element.clone(sudoku);
						int y = index / 9;
						int x = index - 9 * y;
						y++;
						x++;
						element.setXYN(x, y, number);
						sudokuField.add(element);
					}
				}
			}
		}
		System.out.println("Not solvable.");
	}

}
