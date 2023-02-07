package com.pafolder.voronoi;

import static java.lang.System.out;

public class Task {

    static void printNumbers(VoronoiDiagram vd) {
        out.println("\nNumber of Domains: " + vd.getDomainsCount());
        out.println("Number of Non-white Domains: " + vd.getDomainsCount(false, Status.ANY));
        out.println("Number of Non-white Simply Connected Domains: "
                + vd.getDomainsCount(false, Status.IS_SIMPLY_CONNECTED));
    }

    public static void main(String[] args) {
        VoronoiDiagram vd = new VoronoiDiagram(Data.example1);
        printNumbers(vd);

        vd = new VoronoiDiagram(Data.example2);
        printNumbers(vd);

        vd = new VoronoiDiagram(Data.example3);
        printNumbers(vd);
    }

}
