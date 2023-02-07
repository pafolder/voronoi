package com.pafolder.voronoi;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.System.out;

public class Task extends Data {

    static class Cell {
        Point seed;
        boolean isWhite;
        boolean isProcessed;
        Point[] polytopePoints;
    }

    static class CellWithAdjacents extends Cell {
        List<Integer> adjacentCellsIndexes = new ArrayList<>();
    }

    static class VoronoiDiagram {
        CellWithAdjacents[] cellWithAdjacents = new CellWithAdjacents[NUMBER_OF_CELLS];

        @Override
        public VoronoiDiagram clone() {
            VoronoiDiagram newVd = new VoronoiDiagram();
            for (int i = 0; i < cellWithAdjacents.length; i++) {
                newVd.cellWithAdjacents[i] = new CellWithAdjacents();
                newVd.cellWithAdjacents[i].isWhite = cellWithAdjacents[i].isWhite;
                newVd.cellWithAdjacents[i].adjacentCellsIndexes.addAll(cellWithAdjacents[i].adjacentCellsIndexes);
            }
            return newVd;
        }

        List<Integer> getAdjacentCellsOfSameColorIndexes(int cellIndex) {
            List<Integer> result = new ArrayList<>();
            cellWithAdjacents[cellIndex].adjacentCellsIndexes.forEach(i -> {
                if (cellWithAdjacents[i].isWhite == cellWithAdjacents[cellIndex].isWhite) result.add(i);
            });
            return result;
        }
    }

    static class Domain {
        Set<Integer> cellIndexes = new HashSet<>();
        Status isSimplyConnected = Status.IS_IGNORED;

        void colourWhite(VoronoiDiagram clonedVd) {
            cellIndexes.forEach(i -> clonedVd.cellWithAdjacents[i].isWhite = true);
        }
    }

    List<Domain> getDomainsWithoutStatus(VoronoiDiagram vd) {
        List<Domain> domains = new ArrayList<>();
        for (int ci = 0; ci < vd.cellWithAdjacents.length; ci++) {
            if (vd.cellWithAdjacents[ci].isProcessed) continue;
            Domain domain = new Domain();
            domain.cellIndexes.add(ci);
            Queue<Integer> queue = new LinkedList<>(vd.getAdjacentCellsOfSameColorIndexes(ci));
            while (!queue.isEmpty()) {
                int i = queue.poll();
                if (!vd.cellWithAdjacents[i].isProcessed) {
                    queue.addAll(vd.getAdjacentCellsOfSameColorIndexes(i));
                }
                vd.cellWithAdjacents[i].isProcessed = true;
                domain.cellIndexes.add(i);
            }
            domains.add(domain);
        }
        return domains;
    }

    int countDomainsInList(boolean isWhite, Status status, List<Domain> domains) {
        int count = 0;
        for (Domain d : domains) {
            count += vd.cellWithAdjacents[d.cellIndexes.iterator().next()].isWhite == isWhite &&
                    (d.isSimplyConnected == status || status == Status.IS_IGNORED) ? 1 : 0;
        }
        return count;
    }

    List<Domain> getDomainsWithSimplyConnectedStatus(VoronoiDiagram vd) {
        List<Domain> domains = getDomainsWithoutStatus(vd);
        int whiteDomainsCount = countDomainsInList(true, Status.IS_IGNORED, domains);
        for (Domain d : domains) {
            VoronoiDiagram clonedVd = vd.clone();
            d.colourWhite(clonedVd);
            d.isSimplyConnected = countDomainsInList(true, Status.IS_IGNORED,
                    getDomainsWithoutStatus(clonedVd)) == whiteDomainsCount ?
                    Status.IS_SIMPLY_CONNECTED : Status.IS_NON_SIMPLY_CONNECTED;
        }
        return domains;
    }

    public static void main(String[] args) {
        Task task = new Task();

        List<Domain> domainsWithSimplyConnectedStatus = task.getDomainsWithSimplyConnectedStatus(vd);

        out.println("Total number of Domains: " + domainsWithSimplyConnectedStatus.size());
        out.println("Number of Non-white Domains: " +
                task.countDomainsInList(false, Status.IS_IGNORED, domainsWithSimplyConnectedStatus));
        out.println("Number of Non-white Simply Connected Domains: " +
                task.countDomainsInList(false, Status.IS_SIMPLY_CONNECTED, domainsWithSimplyConnectedStatus));
    }

    enum Status {
        IS_IGNORED,
        IS_SIMPLY_CONNECTED,
        IS_NON_SIMPLY_CONNECTED;
    }
}
