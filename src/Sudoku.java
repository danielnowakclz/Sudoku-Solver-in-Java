import java.util.Iterator;
import java.util.Vector;

public class Sudoku {

	public Vector<Field> sudoku = new Vector<>();

	public Sudoku() {
		for (int i = 1; i <= 9 * 9; i++)
			sudoku.add(new Field());
	}

	public void clone(final Sudoku from) {
		for (int index = 0; index < from.sudoku.size(); index++) {
			final Field fFrom = from.sudoku.elementAt(index);
			final Field fTo = sudoku.elementAt(index);
			fTo.number = fFrom.number;
			fTo.possibleNumbers.clear();
			final Iterator<Integer> iter = fFrom.possibleNumbers.iterator();
			while (iter.hasNext())
				fTo.possibleNumbers.add(iter.next());
		}
	}

	public int nextSolvedFieldIndex() {
		int result = 0;
		for (final Field element : sudoku)
			if (element.possibleNumbers.size() == 1)
				return result;
			else
				result++;
		return -1;
	}

	public int nextSplitFieldIndex() {
		int result = -1;
		int max = 9;
		for (int i = 0; i < 9 * 9; i++) {
			final int check = sudoku.elementAt(i).possibleNumbers.size();
			if (check > 1 && check < max) {
				result = i;
				max = check;
			}
		}
		return result;
	}

	public void print() {
		for (int y = 0; y < 9; y++) {
			if (y == 3 || y == 6)
				System.out.println();
			for (int x = 0; x < 9; x++) {
				if (x == 3 || x == 6)
					System.out.print("  ");
				final int index = y * 9 + x;
				System.out.print(sudoku.elementAt(index).number + " ");
			}
			System.out.println();
		}
	}

	public void setIN(final int index, final int number) {
		int y = index / 9;
		int x = index - 9 * y;
		y++;
		x++;
		setXYN(x, y, number);
	}

	public void setXYN(final int x, final int y, final int number) {
		final int hx = x - 1;
		final int hy = y - 1;
		int index = hy * 9 + hx;
		sudoku.elementAt(index).number = number;
		sudoku.elementAt(index).possibleNumbers.clear();
		for (int px = 0; px <= 8; px++) {
			index = hy * 9 + px;
			sudoku.elementAt(index).possibleNumbers.remove(number);
		}
		for (int py = 0; py <= 8; py++) {
			index = py * 9 + hx;
			sudoku.elementAt(index).possibleNumbers.remove(number);
		}
		final int xa = 3 * (hx / 3);
		final int ya = 3 * (hy / 3);
		final int xe = xa + 2;
		final int ye = ya + 2;
		for (int px = xa; px <= xe; px++)
			for (int py = ya; py <= ye; py++) {
				index = py * 9 + px;
				sudoku.elementAt(index).possibleNumbers.remove(number);
			}
	}

	public boolean solved() {
		for (final Field element : sudoku)
			if (element.number < 1)
				return false;
		return true;
	}

}
