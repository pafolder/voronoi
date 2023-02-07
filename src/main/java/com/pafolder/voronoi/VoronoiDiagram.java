package com.pafolder.voronoi;

import java.util.*;
import java.util.List;

public class VoronoiDiagram {
    private static final int OFFSET_OF_COLOR_IN_DATA_ARRAY = 1;
    private static final int OFFSET_OF_ADJACENT_INDEXES_IN_DATA_ARRAY = 2;

    private CellWithAdjacents[] cellsWithAdjacents;
    private int numberOfCells;
    private List<Domain> domains;

    static private class CellWithAdjacents {
        Point seed; // Not used for computing domains
        boolean isWhite;
        boolean isProcessed;
        Point[] polytopePoints; // Not used for computing domains
        List<Integer> adjacentCellsIndexes = new ArrayList<>();
    }

    static class Point {
        double x;
        double y;
    }

    static class Domain {
        Set<Integer> cellIndexes = new HashSet<>();
        Status isSimplyConnected = Status.ANY;
    }

    public VoronoiDiagram() {
    }

    public VoronoiDiagram(int[][] dataArray) {
        numberOfCells = dataArray.length;
        cellsWithAdjacents = new CellWithAdjacents[numberOfCells];
        for (int[] d : dataArray) {
            int i = d[0];
            cellsWithAdjacents[i] = new CellWithAdjacents();
            cellsWithAdjacents[i].isWhite = d[OFFSET_OF_COLOR_IN_DATA_ARRAY] == 1;
            for (int j = OFFSET_OF_ADJACENT_INDEXES_IN_DATA_ARRAY; j < d.length; j++) {
                cellsWithAdjacents[i].adjacentCellsIndexes.add(d[j]);
            }
        }
    }

    @Override
    public VoronoiDiagram clone() {
        VoronoiDiagram newVd = new VoronoiDiagram();
        newVd.cellsWithAdjacents = new CellWithAdjacents[numberOfCells];
        for (int i = 0; i < cellsWithAdjacents.length; i++) {
            newVd.cellsWithAdjacents[i] = new CellWithAdjacents();
            newVd.cellsWithAdjacents[i].isWhite = cellsWithAdjacents[i].isWhite;
            newVd.cellsWithAdjacents[i].adjacentCellsIndexes.addAll(cellsWithAdjacents[i].adjacentCellsIndexes);
        }
        return newVd;
    }

    private List<Integer> getAdjacentCellsOfSameColorIndexes(int cellIndex) {
        List<Integer> result = new ArrayList<>();
        cellsWithAdjacents[cellIndex].adjacentCellsIndexes.forEach(i -> {
            if (cellsWithAdjacents[i].isWhite == cellsWithAdjacents[cellIndex].isWhite) result.add(i);
        });
        return result;
    }

    private void computeDomainsWithoutSimplyConnectedStatus() {
        domains = new ArrayList<>();
        for (int ci = 0; ci < cellsWithAdjacents.length; ci++) {
            if (cellsWithAdjacents[ci].isProcessed) continue;
            Domain domain = new Domain();
            domain.cellIndexes.add(ci);
            Queue<Integer> queue = new LinkedList<>(getAdjacentCellsOfSameColorIndexes(ci));
            while (!queue.isEmpty()) {
                int i = queue.poll();
                if (!cellsWithAdjacents[i].isProcessed) {
                    queue.addAll(getAdjacentCellsOfSameColorIndexes(i));
                }
                cellsWithAdjacents[i].isProcessed = true;
                domain.cellIndexes.add(i);
            }
            domains.add(domain);
        }
    }

    public int getDomainsCount() {
        if (domains == null) {
            computeDomains();
        }
        return domains.size();
    }

    public int getDomainsCount(boolean isWhite, Status status) {
        if (domains == null) {
            computeDomains();
        }
        int count = 0;
        for (Domain d : domains) {
            count += cellsWithAdjacents[d.cellIndexes.iterator().next()].isWhite == isWhite &&
                    (d.isSimplyConnected == status || status == Status.ANY) ? 1 : 0;
        }
        return count;
    }

    public void computeDomains() {
        computeDomainsWithoutSimplyConnectedStatus();
        for (Domain d : domains) {
            VoronoiDiagram cloned = clone();
            cloned.colourCellsWhite(d);
            cloned.computeDomainsWithoutSimplyConnectedStatus();
            d.isSimplyConnected = cloned.getDomainsCount(true, Status.ANY) ==
                    getDomainsCount(true, Status.ANY) ?
                    Status.IS_SIMPLY_CONNECTED : Status.IS_NON_SIMPLY_CONNECTED;
        }
    }

    private void colourCellsWhite(Domain domain) {
        domain.cellIndexes.forEach(i -> cellsWithAdjacents[i].isWhite = true);
    }
}
