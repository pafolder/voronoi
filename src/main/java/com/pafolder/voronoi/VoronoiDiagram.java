package com.pafolder.voronoi;

import java.util.*;
import java.util.List;

public class VoronoiDiagram {
    private static final int OFFSET_OF_COLOR_IN_DATA_ARRAY = 1;
    private static final int OFFSET_OF_ADJACENT_INDEXES_IN_DATA_ARRAY = 2;

    private CellWithAdjacents[] cellWithAdjacents;
    private int cellsNumber;
    List<Domain> domains;

    static class CellWithAdjacents {
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
        cellsNumber = dataArray.length;
        cellWithAdjacents = new CellWithAdjacents[cellsNumber];
        for (int[] d : dataArray) {
            int i = d[0];
            cellWithAdjacents[i] = new CellWithAdjacents();
            cellWithAdjacents[i].isWhite = d[OFFSET_OF_COLOR_IN_DATA_ARRAY] == 1;
            for (int j = OFFSET_OF_ADJACENT_INDEXES_IN_DATA_ARRAY; j < d.length; j++) {
                cellWithAdjacents[i].adjacentCellsIndexes.add(d[j]);
            }
        }
    }

    @Override
    public VoronoiDiagram clone() {
        VoronoiDiagram newVd = new VoronoiDiagram();
        newVd.cellWithAdjacents = new CellWithAdjacents[cellsNumber];
        for (int i = 0; i < cellWithAdjacents.length; i++) {
            newVd.cellWithAdjacents[i] = new CellWithAdjacents();
            newVd.cellWithAdjacents[i].isWhite = cellWithAdjacents[i].isWhite;
            newVd.cellWithAdjacents[i].adjacentCellsIndexes.addAll(cellWithAdjacents[i].adjacentCellsIndexes);
        }
        return newVd;
    }

    private List<Integer> getAdjacentCellsOfSameColorIndexes(int cellIndex) {
        List<Integer> result = new ArrayList<>();
        cellWithAdjacents[cellIndex].adjacentCellsIndexes.forEach(i -> {
            if (cellWithAdjacents[i].isWhite == cellWithAdjacents[cellIndex].isWhite) result.add(i);
        });
        return result;
    }

    private void computeDomainsWithoutSimplyConnectedStatus() {
        domains = new ArrayList<>();
        for (int ci = 0; ci < cellWithAdjacents.length; ci++) {
            if (cellWithAdjacents[ci].isProcessed) continue;
            Domain domain = new Domain();
            domain.cellIndexes.add(ci);
            Queue<Integer> queue = new LinkedList<>(getAdjacentCellsOfSameColorIndexes(ci));
            while (!queue.isEmpty()) {
                int i = queue.poll();
                if (!cellWithAdjacents[i].isProcessed) {
                    queue.addAll(getAdjacentCellsOfSameColorIndexes(i));
                }
                cellWithAdjacents[i].isProcessed = true;
                domain.cellIndexes.add(i);
            }
            domains.add(domain);
        }
    }

    int getDomainsCount() {
        if (domains == null) {
            computeDomains();
        }
        return domains.size();
    }

    int getDomainsCount(boolean isWhite, Status status) {
        if (domains == null) {
            computeDomains();
        }
        int count = 0;
        for (Domain d : domains) {
            count += cellWithAdjacents[d.cellIndexes.iterator().next()].isWhite == isWhite &&
                    (d.isSimplyConnected == status || status == Status.ANY) ? 1 : 0;
        }
        return count;
    }

    public void computeDomains() {
        computeDomainsWithoutSimplyConnectedStatus();
        int whiteDomainsCount = getDomainsCount(true, Status.ANY);
        for (Domain d : domains) {
            VoronoiDiagram clonedVd = clone();
            clonedVd.colourCellsWhite(d);
            clonedVd.computeDomainsWithoutSimplyConnectedStatus();
            int whiteDomainsCountInClone = clonedVd.getDomainsCount(true, Status.ANY);
            d.isSimplyConnected = whiteDomainsCountInClone == whiteDomainsCount ?
                    Status.IS_SIMPLY_CONNECTED : Status.IS_NON_SIMPLY_CONNECTED;
        }
    }

    void colourCellsWhite(Domain domain) {
        domain.cellIndexes.forEach(i -> cellWithAdjacents[i].isWhite = true);
    }
}
