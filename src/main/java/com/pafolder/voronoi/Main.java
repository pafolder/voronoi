package com.pafolder.voronoi;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        computeDomainsAndShowResults(new VoronoiDiagram(Data.example1), "Example 1");
        computeDomainsAndShowResults(new VoronoiDiagram(Data.example2), "Example 2");
        computeDomainsAndShowResults(new VoronoiDiagram(Data.example3), "Example 3");
        computeDomainsAndShowResults(new VoronoiDiagram(Data.example4), "Example 4");
    }

    static void computeDomainsAndShowResults(VoronoiDiagram vd, String title) {
        out.println("\n" + title);
        out.println("Number of Domains: " + vd.getDomainsCount());
        out.println("Number of White Domains: " + vd.getDomainsCount(true, Status.ANY));
        out.println("Number of Non-White Domains: " + vd.getDomainsCount(false, Status.ANY) + ", including " +
                vd.getDomainsCount(false, Status.IS_SIMPLY_CONNECTED) + " Simply-Connected");
    }
}
