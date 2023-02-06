package com.pafolder.voronoi;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.System.out;

public class Main extends Initialization {

    static class Cell {
        Point seed;
        boolean isWhite;
        boolean isProcessed;
        Point[] polytopePoints;
        List<Integer> adjacentCellsIndexes = new ArrayList<>();
    }

    static class VoronoiDiagram {
        Cell[] cells = new Cell[NUMBER_OF_CELLS];

        @Override
        public VoronoiDiagram clone() {
            VoronoiDiagram newVd = new VoronoiDiagram();
            for (int i = 0; i < cells.length; i++) {
                newVd.cells[i] = new Cell();
                newVd.cells[i].isWhite = cells[i].isWhite;
                newVd.cells[i].adjacentCellsIndexes.addAll(cells[i].adjacentCellsIndexes);
            }
            return newVd;
        }

        List<Integer> getAdjacentCellsOfSameColorIndexes(int cellIndex) {
            List<Integer> result = new ArrayList<>();
            for (int i : cells[cellIndex].adjacentCellsIndexes) {
                if (cells[i].isWhite == cells[cellIndex].isWhite) result.add(i);
            }
            return result;
        }
    }

    static class Domain {
        Set<Integer> cellIndexes = new HashSet<>();
        boolean isSimplyConnected = false;

        void colourWhite(VoronoiDiagram clonedVd) {
            for (int cellIndex : cellIndexes)
                clonedVd.cells[cellIndex].isWhite = true;
        }
    }

    List<Domain> getDomainsWithoutStatus(VoronoiDiagram vd) {
        List<Domain> domains = new ArrayList<>();
        for (int ci = 0; ci < vd.cells.length; ci++) {
            if (vd.cells[ci].isProcessed) continue;
            Domain domain = new Domain();
            domain.cellIndexes.add(ci);
            Queue<Integer> queue = new LinkedList<>(vd.getAdjacentCellsOfSameColorIndexes(ci));
            while (!queue.isEmpty()) {
                int i = queue.poll();
                if (!vd.cells[i].isProcessed) {
                    queue.addAll(vd.getAdjacentCellsOfSameColorIndexes(i));
                }
                vd.cells[i].isProcessed = true;
                domain.cellIndexes.add(i);
            }
            domains.add(domain);
        }
        return domains;
    }

    int countDomainsInList(boolean isWhite, boolean isSimplyConnected, List<Domain> domains) {
        int count = 0;
        for (Domain d : domains) {
            count += vd.cells[d.cellIndexes.iterator().next()].isWhite == isWhite &&
                    ((d.isSimplyConnected == isSimplyConnected) || !isSimplyConnected) ? 1 : 0;
        }
        return count;
    }

    List<Domain> getDomainsWithSimplyConnectedStatus(VoronoiDiagram vd) {
        List<Domain> domains = getDomainsWithoutStatus(vd);
        int whiteDomainsCount = countDomainsInList(true, false, domains);
        for (Domain d : domains) {
            d.isSimplyConnected = true;
            VoronoiDiagram clonedVd = vd.clone();
            d.colourWhite(clonedVd);
            List<Domain> domainsClone = getDomainsWithoutStatus(clonedVd);
            int whiteDomainsCountAfterColouredWhite = countDomainsInList(true, false, domainsClone);
            if (whiteDomainsCountAfterColouredWhite != whiteDomainsCount) {
                d.isSimplyConnected = false;
            }
        }
        return domains;
    }

    public static void main(String[] args) {
        Main main = new Main();
        List<Domain> domainsWithSimplyConnectedStatus = main.getDomainsWithSimplyConnectedStatus(vd);
        out.println("Total number of Domains: " + domainsWithSimplyConnectedStatus.size());
        out.println("Number of Non-white Domains: " +
                main.countDomainsInList(false, false, domainsWithSimplyConnectedStatus));
        out.println("Number of Non-white Simply Connected Domains: " +
                main.countDomainsInList(false, true, domainsWithSimplyConnectedStatus));
    }
}
