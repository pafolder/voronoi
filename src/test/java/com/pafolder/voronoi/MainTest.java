package com.pafolder.voronoi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void example1Test() {
        VoronoiDiagram vd = new VoronoiDiagram(Data.example1);
        Assertions.assertEquals(4, vd.getDomainsCount());
        Assertions.assertEquals(2, vd.getDomainsCount(false, Status.ANY));
        Assertions.assertEquals(1, vd.getDomainsCount(false, Status.IS_SIMPLY_CONNECTED));
    }

    @Test
    void example2Test() {
        VoronoiDiagram vd = new VoronoiDiagram(Data.example2);
        Assertions.assertEquals(3, vd.getDomainsCount());
        Assertions.assertEquals(1, vd.getDomainsCount(false, Status.ANY));
        Assertions.assertEquals(0, vd.getDomainsCount(false, Status.IS_SIMPLY_CONNECTED));
    }

    @Test
    void example3Test() {
        VoronoiDiagram vd = new VoronoiDiagram(Data.example3);
        Assertions.assertEquals(4, vd.getDomainsCount());
        Assertions.assertEquals(1, vd.getDomainsCount(false, Status.ANY));
        Assertions.assertEquals(0, vd.getDomainsCount(false, Status.IS_SIMPLY_CONNECTED));
    }

    @Test
    void example4Test() {
        VoronoiDiagram vd = new VoronoiDiagram(Data.example4);
        Assertions.assertEquals(3, vd.getDomainsCount());
        Assertions.assertEquals(2, vd.getDomainsCount(false, Status.ANY));
        Assertions.assertEquals(2, vd.getDomainsCount(false, Status.IS_SIMPLY_CONNECTED));
    }
}
