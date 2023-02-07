package com.pafolder.voronoi;

import static java.lang.System.out;

public class Main {

    static void printNumbers(VoronoiDiagram vd) {
        out.println("\nNumber of Domains: " + vd.getDomainsCount());
        out.println("Number of White Domains: " + vd.getDomainsCount(true, Status.ANY));
        out.println("Number of Non-White Domains: " + vd.getDomainsCount(false, Status.ANY) + ", including " +
                vd.getDomainsCount(false, Status.IS_SIMPLY_CONNECTED) + " Simply-Connected");
    }

    public static void main(String[] args) {
        VoronoiDiagram vd1 = new VoronoiDiagram(Data.example1);
        printNumbers(vd1);

        VoronoiDiagram vd2 = new VoronoiDiagram(Data.example2);
        printNumbers(vd2);

        VoronoiDiagram vd3 = new VoronoiDiagram(Data.example3);
        printNumbers(vd3);

        VoronoiDiagram vd4 = new VoronoiDiagram(Data.example4);
        printNumbers(vd4);
    }

}
