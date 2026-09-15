public class Sudoku {
    public int[] grid = new int[81];
    public int[] rowMask = new int[9];
    public int[] colMask = new int[9];
    public int[] boxMask = new int[9];

    public Sudoku() {
        for (int i = 0; i < 9; i++) {
            rowMask[i] = 0b111111111;
            colMask[i] = 0b111111111;
            boxMask[i] = 0b111111111;
        }
    }

    public Sudoku clone() {
        Sudoku s = new Sudoku();
        System.arraycopy(grid, 0, s.grid, 0, 81);
        System.arraycopy(rowMask, 0, s.rowMask, 0, 9);
        System.arraycopy(colMask, 0, s.colMask, 0, 9);
        System.arraycopy(boxMask, 0, s.boxMask, 0, 9);
        return s;
    }

    public void set(int index, int num) {
        int x = index % 9;
        int y = index / 9;
        int b = (y / 3) * 3 + (x / 3);

        int bit = 1 << (num - 1);

        grid[index] = num;
        rowMask[y] &= ~bit;
        colMask[x] &= ~bit;
        boxMask[b] &= ~bit;
    }

    public int possibleMask(int index) {
        int x = index % 9;
        int y = index / 9;
        int b = (y / 3) * 3 + (x / 3);
        return rowMask[y] & colMask[x] & boxMask[b];
    }

    public int findSingle() {
        for (int i = 0; i < 81; i++) {
            if (grid[i] == 0) {
                int mask = possibleMask(i);
                if (Integer.bitCount(mask) == 1)
                    return i;
            }
        }
        return -1;
    }

    public int findBestSplit() {
        int bestIndex = -1;
        int bestCount = 10;

        for (int i = 0; i < 81; i++) {
            if (grid[i] == 0) {
                int mask = possibleMask(i);
                int count = Integer.bitCount(mask);
                if (count > 1 && count < bestCount) {
                    bestCount = count;
                    bestIndex = i;
                }
            }
        }
        return bestIndex;
    }

    public boolean solve() {
        int single = findSingle();
        while (single != -1) {
            int mask = possibleMask(single);
            int num = Integer.numberOfTrailingZeros(mask) + 1;
            set(single, num);
            single = findSingle();
        }

        boolean done = true;
        for (int v : grid) if (v == 0) done = false;
        if (done) return true;

        int index = findBestSplit();
        int mask = possibleMask(index);

        while (mask != 0) {
            int num = Integer.numberOfTrailingZeros(mask) + 1;
            mask &= mask - 1;

            Sudoku clone = this.clone();
            clone.set(index, num);

            if (clone.solve()) {
                System.arraycopy(clone.grid, 0, this.grid, 0, 81);
                return true;
            }
        }

        return false;
    }

    public void print() {
        for (int y = 0; y < 9; y++) {
            if (y == 3 || y == 6) System.out.println();
            for (int x = 0; x < 9; x++) {
                if (x == 3 || x == 6) System.out.print(" ");
                System.out.print(grid[y * 9 + x] + " ");
            }
            System.out.println();
        }
    }
}
