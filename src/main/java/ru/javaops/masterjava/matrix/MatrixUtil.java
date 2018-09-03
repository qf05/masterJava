package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        final ExecutorService service = Executors.newFixedThreadPool(8);
        final CompletionService<int[]> completionService = new ExecutorCompletionService<>(service);
        final Future<int[]> futures[] = new Future[matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            int finalI = i;
            futures[i] = completionService.submit(() -> {
                int result[] = new int[matrixSize];
                final int thatColumn[] = new int[matrixSize];
                for (int k = 0; k < matrixSize; k++) {
                    thatColumn[k] = matrixB[k][finalI];
                }

                for (int j = 0; j < matrixSize; j++) {
                    int tt[] = matrixA[j];
                    int summand = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        summand += tt[k] * thatColumn[k];
                    }
                    result[j] = summand;
                }
                return result;
            });
        }

        for (int i = 0; i < matrixSize; i++) {
            int it[] = futures[i].get();
            for (int j = 0; j < matrixSize; j++) {
                matrixC[j][i] = it[j];
            }

        }
        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        final int thatColumn[] = new int[matrixSize];
        try {
            for (int i = 0; ; i++) {
                for (int k = 0; k < matrixSize; k++) {
                    thatColumn[k] = matrixB[k][i];
                }
                for (int j = 0; j < matrixSize; j++) {
                    int tt[] = matrixA[j];
                    int summand = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        summand += tt[k] * thatColumn[k];
                    }
                    matrixC[j][i] = summand;
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
