import java.util.*;

class MatrixMultiplier extends Thread {
    private int[][] A;
    private int[][] B;
    private int[][] C;
    private int row;
    private long start;
    private long end;

    public MatrixMultiplier(int[][] A, int[][] B, int[][] C, int row) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.row = row;
    }

    public long getExecutionTime() {
        return end - start;
    }

    public void run() {
        start = System.nanoTime(); // İş parçacığı çalışmaya başlar
        for (int col = 0; col < C[0].length; col++) {
            for (int i = 0; i < A[0].length; i++) {
                C[row][col] += A[row][i] * B[i][col];
            }
        }
        end = System.nanoTime(); // İş parçacığı çalışmayı bitirir
    }
}


public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Kullanim: java is_parcacigi_sayisi <rowsA> <colsA> <colsB>");
            return;
        }

        int rowsA = Integer.parseInt(args[0]);
        int colsA = Integer.parseInt(args[1]);
        int colsB = Integer.parseInt(args[2]);

        int rowsB = colsA; // B'nin satır sayısı, A'nın sütun sayısına eşit olmalı
        int threadSayisi = rowsA;

        int[][] A = new int[rowsA][colsA];
        int[][] B = new int[rowsB][colsB];
        int[][] C = new int[rowsA][colsB];

        Random rand = new Random();

        // A ve B matrislerini rastgele değerlerle doldurur
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsA; j++) {
                A[i][j] = rand.nextInt(10);
            }
        }

        for (int i = 0; i < rowsB; i++) {
            for (int j = 0; j < colsB; j++) {
                B[i][j] = rand.nextInt(10);
            }
        }

        // İş parçacıklarını oluşturma ve başlatma
        MatrixMultiplier[] threads = new MatrixMultiplier[threadSayisi];

        long start = System.nanoTime(); // Toplam işlemin başlangıç zamanı

        for (int i = 0; i < threadSayisi; i++) {
            threads[i] = new MatrixMultiplier(A, B, C, i);
            threads[i].start();
        }

        // Tüm iş parçacıklarının bitmesini bekler
        for (int i = 0; i < threadSayisi; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        long end = System.nanoTime(); // Toplam işlemin bitiş zamanı


        //Matrislerin içeriğini yazdırır
        System.out.println("Matrix A:");
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsA; j++) {
                System.out.print(A[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Matrix B:");
        for (int i = 0; i < rowsB; i++) {
            for (int j = 0; j < colsB; j++) {
                System.out.print(B[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Matrix C (A x B):");
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                System.out.print(C[i][j] + " ");
            }
            System.out.println();
        }

        // İş parçacıklarının sürelerini yazdırır
        for (int i = 0; i < rowsA; i++) {
            System.out.println("Thread " + i + " calisma suresi: " + threads[i].getExecutionTime() + " ns");
        }

        // Toplam işlem süresini yazdırır
        System.out.println("Toplam calisma suresi: " + (end - start) + " ns");
    }
}

