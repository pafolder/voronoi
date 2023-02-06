package com.pafolder.voronoi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

import static java.lang.System.out;

class MainTest {
    private Main main = new Main();

    @org.junit.jupiter.api.Test
    void mainTest() {
        List<Main.Domain> domainsWithSimplyConnectedStatus = main.getDomainsWithSimplyConnectedStatus(Main.vd);
        Assertions.assertEquals(4,
                domainsWithSimplyConnectedStatus.size());
        Assertions.assertEquals(2,
                main.countDomainsInList(false, false, domainsWithSimplyConnectedStatus));
        Assertions.assertEquals(1,
                main.countDomainsInList(false, true, domainsWithSimplyConnectedStatus));
    }
}
