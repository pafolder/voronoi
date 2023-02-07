package com.pafolder.voronoi;

import java.util.*;
import java.util.List;

public class VoronoiDiagram {
    private static final int OFFSET_OF_COLOR_IN_DATA_ARRAY = 1;
    private static final int OFFSET_OF_ADJACENT_CELLS_INDICES_IN_DATA_ARRAY = 2;

    private List<CellWithAdjacents> cellsWithAdjacents;
    private List<Domain> domains;

    static class CellWithAdjacents {
        Point seed; // Not used for computing domains
        boolean isWhite;
        boolean isProcessed;
        List<Point> polytopePoints; // Not used for computing domains
        List<Integer> adjacentCellsIndices = new ArrayList<>();
    }

    static class Point { // Not used for computing domains
        double x;
        double y;
    }

    static class Domain {
        Set<Integer> cellIndices = new HashSet<>();
        Status isSimplyConnected = Status.ANY;
        boolean isWhite;
    }

    public VoronoiDiagram() {
    }

    public VoronoiDiagram(int[][] dataArray) {
        cellsWithAdjacents = new LinkedList<>();
        for (int[] d : dataArray) {
            CellWithAdjacents cell = new CellWithAdjacents();
            cell.isWhite = d[OFFSET_OF_COLOR_IN_DATA_ARRAY] == 1;
            for (int j = OFFSET_OF_ADJACENT_CELLS_INDICES_IN_DATA_ARRAY; j < d.length; j++) {
                cell.adjacentCellsIndices.add(d[j]);
            }
            cellsWithAdjacents.add(cell);
        }
    }

    @Override
    public VoronoiDiagram clone() {
        VoronoiDiagram newVd = new VoronoiDiagram();
        newVd.cellsWithAdjacents = new LinkedList<>();
        cellsWithAdjacents.forEach(cell -> {
            CellWithAdjacents clonedCell = new CellWithAdjacents();
            clonedCell.isWhite = cell.isWhite;
            clonedCell.isProcessed = false;
            clonedCell.adjacentCellsIndices.addAll(cell.adjacentCellsIndices);
            newVd.cellsWithAdjacents.add(clonedCell);
        });
        return newVd;
    }

    private List<Integer> getAdjacentCellsOfSameColorIndices(int cellIndex) {
        List<Integer> result = new ArrayList<>();
        cellsWithAdjacents.get(cellIndex).adjacentCellsIndices.forEach(i -> {
            if (cellsWithAdjacents.get(i).isWhite == cellsWithAdjacents.get(cellIndex).isWhite) {
                result.add(i);
            }
        });
        return result;
    }

    private void computeDomainsWithoutSimplyConnectedStatus() {
        domains = new ArrayList<>();
        for (int ajacentCellIndex = 0; ajacentCellIndex < cellsWithAdjacents.size(); ajacentCellIndex++) {
            if (cellsWithAdjacents.get(ajacentCellIndex).isProcessed) continue;
            Domain domain = new Domain();
            domain.isWhite = cellsWithAdjacents.get(ajacentCellIndex).isWhite;
            domain.cellIndices.add(ajacentCellIndex);
            Queue<Integer> queue = new LinkedList<>(getAdjacentCellsOfSameColorIndices(ajacentCellIndex));
            while (!queue.isEmpty()) {
                int i = queue.poll();
                if (!cellsWithAdjacents.get(i).isProcessed) {
                    queue.addAll(getAdjacentCellsOfSameColorIndices(i));
                }
                cellsWithAdjacents.get(i).isProcessed = true;
                domain.cellIndices.add(i);
            }
            domains.add(domain);
        }
    }

    public int getDomainsCount() {
        computeDomainsIfNeeded();
        return domains.size();
    }

    public int getDomainsCount(boolean isWhite, Status status) {
        computeDomainsIfNeeded();
        int count = 0;
        for (Domain d : domains) {
            count += cellsWithAdjacents.get(d.cellIndices.iterator().next()).isWhite == isWhite &&
                    (d.isSimplyConnected == status || status == Status.ANY) ? 1 : 0;
        }
        return count;
    }

    public void computeDomains() {
        computeDomainsWithoutSimplyConnectedStatus();
        domains.forEach(d -> {
            if (!d.isWhite) {
                VoronoiDiagram cloned = clone();
                cloned.colourCellsWhite(d);
                cloned.computeDomainsWithoutSimplyConnectedStatus();
                d.isSimplyConnected = cloned.getDomainsCount(true, Status.ANY) ==
                        getDomainsCount(true, Status.ANY) ?
                        Status.IS_SIMPLY_CONNECTED : Status.IS_NON_SIMPLY_CONNECTED;
            }
        });
    }

    private void colourCellsWhite(Domain domain) {
        domain.cellIndices.forEach(i -> cellsWithAdjacents.get(i).isWhite = true);
    }

    void computeDomainsIfNeeded() {
        if (domains == null) {
            computeDomains();
        }
    }
}
