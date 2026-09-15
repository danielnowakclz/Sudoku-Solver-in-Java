public class SudokuSolver {

    static class Sudoku {
        int[] num   = new int[81];   // gesetzte Zahlen (0 = leer)
        int[] mask  = new int[81];   // mögliche Zahlen je Feld (Bits 0..8)
        int[] row   = new int[9];    // freie Zahlen je Zeile
        int[] col   = new int[9];    // freie Zahlen je Spalte
        int[] box   = new int[9];    // freie Zahlen je Block

        Sudoku() {
            for (int i = 0; i < 81; i++)
                mask[i] = 0b111111111;
            for (int i = 0; i < 9; i++) {
                row[i] = 0b111111111;
                col[i] = 0b111111111;
                box[i] = 0b111111111;
            }
        }

        Sudoku copy() {
            Sudoku s = new Sudoku();
            System.arraycopy(num,  0, s.num,  0, 81);
            System.arraycopy(mask, 0, s.mask, 0, 81);
            System.arraycopy(row,  0, s.row,  0, 9);
            System.arraycopy(col,  0, s.col,  0, 9);
            System.arraycopy(box,  0, s.box,  0, 9);
            return s;
        }

        int boxIndex(int x, int y) {
            return (y / 3) * 3 + (x / 3);
        }

        void set(int index, int val) {
            int x = index % 9;
            int y = index / 9;
            int b = boxIndex(x, y);
            int bit = 1 << (val - 1);

            num[index] = val;
            mask[index] = 0;

            row[y] &= ~bit;
            col[x] &= ~bit;
            box[b] &= ~bit;

            // Zeile
            for (int px = 0; px < 9; px++) {
                int idx = y * 9 + px;
                if (num[idx] == 0) mask[idx] &= ~bit;
            }
            // Spalte
            for (int py = 0; py < 9; py++) {
                int idx = py * 9 + x;
                if (num[idx] == 0) mask[idx] &= ~bit;
            }
            // Block
            int xa = 3 * (x / 3);
            int ya = 3 * (y / 3);
            for (int px = xa; px < xa + 3; px++) {
                for (int py = ya; py < ya + 3; py++) {
                    int idx = py * 9 + px;
                    if (num[idx] == 0) mask[idx] &= ~bit;
                }
            }
        }

        boolean propagateSingles() {
            boolean changed;
            do {
                changed = false;
                for (int i = 0; i < 81; i++) {
                    if (num[i] == 0) {
                        int m = mask[i];
                        if (m == 0) return false; // Dead end
                        if (Integer.bitCount(m) == 1) {
                            int val = Integer.numberOfTrailingZeros(m) + 1;
                            set(i, val);
                            changed = true;
                        }
                    }
                }
            } while (changed);
            return true;
        }

        boolean isSolved() {
            for (int i = 0; i < 81; i++)
                if (num[i] == 0) return false;
            return true;
        }

        int bestSplitIndex() {
            int bestIdx = -1;
            int bestCount = 10;
            for (int i = 0; i < 81; i++) {
                if (num[i] == 0) {
                    int m = mask[i];
                    if (m == 0) return -2; // Dead end
                    int c = Integer.bitCount(m);
                    if (c > 1 && c < bestCount) {
                        bestCount = c;
                        bestIdx = i;
                    }
                }
            }
            return bestIdx;
        }

        boolean solve() {
            if (!propagateSingles()) return false;
            if (isSolved()) return true;

            int idx = bestSplitIndex();
            if (idx == -2 || idx == -1) return false;

            int m = mask[idx];
            while (m != 0) {
                int val = Integer.numberOfTrailingZeros(m) + 1;
                m &= m - 1;

                Sudoku branch = copy();
                branch.set(idx, val);
                if (branch.solve()) {
                    System.arraycopy(branch.num, 0, this.num, 0, 81);
                    return true;
                }
            }
            return false;
        }

        void print() {
            for (int y = 0; y < 9; y++) {
                if (y == 3 || y == 6) System.out.println();
                for (int x = 0; x < 9; x++) {
                    if (x == 3 || x == 6) System.out.print(" ");
                    System.out.print(num[y * 9 + x] + " ");
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        Sudoku s = new Sudoku();

        int[] field = {
            0,0,0, 0,0,0, 0,0,0,
            0,0,0, 0,0,3, 0,8,5,
            0,0,1, 0,2,0, 0,0,0,

            0,0,0, 5,0,7, 0,0,0,
            0,0,4, 0,0,0, 1,0,0,
            0,9,0, 0,0,0, 0,0,0,

            5,0,0, 0,0,0, 0,7,3,
            0,0,2, 0,1,0, 0,0,0,
            0,0,0, 0,4,0, 0,0,9
        };

        for (int i = 0; i < 81; i++)
            if (field[i] != 0)
                s.set(i, field[i]);

        long t0 = System.nanoTime();
        boolean ok = s.solve();
        long t1 = System.nanoTime();

        if (ok) {
            s.print();
            System.out.println("\nTime: " + (t1 - t0) / 1_000_000.0 + " ms");
        } else {
            System.out.println("Not solvable.");
        }
    }
}
