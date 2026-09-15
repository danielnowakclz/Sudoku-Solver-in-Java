public class SudokuSolver {
    public static void main(String[] args) {
        Sudoku s = new Sudoku();

        int[] field = {
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

        for (int i = 0; i < 81; i++)
            if (field[i] != 0)
                s.set(i, field[i]);

        if (s.solve())
            s.print();
        else
            System.out.println("Not solvable.");
    }
}
