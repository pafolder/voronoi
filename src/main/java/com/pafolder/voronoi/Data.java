package com.pafolder.voronoi;

public class Data {
    static final int NUMBER_OF_CELLS = 20;
    static Task.VoronoiDiagram vd;

    Data() {
        int[][] values = {
                {0, 1, 1, 2, 6, 18},
                {1, 0, 0, 2, 3, 4, 6},
                {2, 1, 0, 3, 5},
                {3, 0, 1, 2, 5, 7, 4},
                {4, 1, 1, 3, 6, 7, 8},
                {5, 1, 2, 3, 7, 9, 13, 19},
                {6, 0, 0, 1, 4, 10, 14, 18},
                {7, 0, 3, 4, 5, 8, 9},
                {8, 1, 4, 6, 7, 9, 10},
                {9, 0, 5, 7, 8, 10, 11, 13},
                {10, 0, 4, 6, 9, 11, 12, 14},
                {11, 1, 9, 10, 13, 15, 12},
                {12, 1, 10, 11, 14, 15},
                {13, 1, 5, 9, 11, 15, 16, 19},
                {14, 1, 6, 10, 12, 15, 17, 18},
                {15, 0, 11, 12, 13, 14, 16, 17},
                {16, 0, 13, 15, 17, 19},
                {17, 0, 14, 15, 16, 18, 19},
                {18, 1, 0, 6, 14, 17, 19},
                {19, 1, 5, 13, 16, 17, 18}
        };

        vd = new Task.VoronoiDiagram();
        for (int[] v : values) {
            int i = v[0];
            vd.cells[i] = new Task.Cell();
            vd.cells[i].isWhite = v[1] == 1;
            for (int j = 2; j < v.length; j++)
                vd.cells[i].adjacentCellsIndexes.add(v[j]);
        }
    }
}
