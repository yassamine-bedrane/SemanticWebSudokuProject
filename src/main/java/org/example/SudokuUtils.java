package org.example;

public class SudokuUtils {

    public  static  int  getBlockNumber(int row, int column) {
        if (row <= 2 && column <= 3) {
            return 1;
        } else if (row <= 2 && column <= 6) {
            return 2;
        } else if (row <= 4 && column <= 3) {
            return 3;
        } else if (row <= 4 && column <= 6) {
            return 4;
        } else if (row <= 6 && column <= 3) {
            return 5;
        } else if ( row <= 6 && column <= 6) {
            return 6;
        }
        return -1;
    }

    public static int[] getIndexesInBlockMatrix(int blockNumber, int row, int column) {
        int[] indexesInBlockMatrix = new int[2];

        int startRowInGlobalMatrix = 0;
        int startColumnInGlobalMatrix = 0;

        if (blockNumber == 1) {
            startRowInGlobalMatrix = 1;
            startColumnInGlobalMatrix = 1;
        } else if (blockNumber == 2) {
            startRowInGlobalMatrix = 1;
            startColumnInGlobalMatrix = 4;
        } else if (blockNumber == 3) {
            startRowInGlobalMatrix = 3;
            startColumnInGlobalMatrix = 1;
        } else if (blockNumber == 4) {
            startRowInGlobalMatrix = 3;
            startColumnInGlobalMatrix = 4;
        } else if (blockNumber == 5) {
            startRowInGlobalMatrix = 5;
            startColumnInGlobalMatrix = 1;
        } else if (blockNumber == 6) {
            startRowInGlobalMatrix = 5;
            startColumnInGlobalMatrix = 4;
        } else {
            indexesInBlockMatrix[0] = -1;
            indexesInBlockMatrix[1] = -1;
            return indexesInBlockMatrix;
        }

        int rowIndexInBlockMatrix = row - startRowInGlobalMatrix;
        int colIndexInBlockMatrix = column - startColumnInGlobalMatrix;


        indexesInBlockMatrix[0] = rowIndexInBlockMatrix + 1;
        indexesInBlockMatrix[1] = colIndexInBlockMatrix + 1;

        return indexesInBlockMatrix;
    }

    public static int[] getIndexesInOriginalMatrix(int blockNumber, int rowInBlock, int columnInBlock) {
        int[] indexesInOriginalMatrix = new int[2];

        int startRowInGlobalMatrix = 0;
        int startColumnInGlobalMatrix = 0;

        if (blockNumber == 1) {
            startRowInGlobalMatrix = 1;
            startColumnInGlobalMatrix = 1;
        } else if (blockNumber == 2) {
            startRowInGlobalMatrix = 1;
            startColumnInGlobalMatrix = 4;
        } else if (blockNumber == 3) {
            startRowInGlobalMatrix = 3;
            startColumnInGlobalMatrix = 1;
        } else if (blockNumber == 4) {
            startRowInGlobalMatrix = 3;
            startColumnInGlobalMatrix = 4;
        } else if (blockNumber == 5) {
            startRowInGlobalMatrix = 5;
            startColumnInGlobalMatrix = 1;
        } else if (blockNumber == 6) {
            startRowInGlobalMatrix = 5;
            startColumnInGlobalMatrix = 4;
        } else {
            // Invalid block number
            indexesInOriginalMatrix[0] = -1;
            indexesInOriginalMatrix[1] = -1;
            return indexesInOriginalMatrix;
        }

        int rowIndexInOriginalMatrix = startRowInGlobalMatrix + rowInBlock - 1;
        int colIndexInOriginalMatrix = startColumnInGlobalMatrix + columnInBlock - 1;

        indexesInOriginalMatrix[0] = rowIndexInOriginalMatrix;
        indexesInOriginalMatrix[1] = colIndexInOriginalMatrix;

        return indexesInOriginalMatrix;
    }
}
