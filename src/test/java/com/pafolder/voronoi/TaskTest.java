package com.pafolder.voronoi;

import org.junit.jupiter.api.Assertions;

import java.util.List;

class TaskTest {
    private Task main = new Task();

    @org.junit.jupiter.api.Test
    void mainTest() {
        List<Task.Domain> domainsWithSimplyConnectedStatus = main.getDomainsWithSimplyConnectedStatus(Task.vd);
        Assertions.assertEquals(4,
                domainsWithSimplyConnectedStatus.size());
        Assertions.assertEquals(2,
                main.countDomainsInList(false, Task.Status.IS_IGNORED, domainsWithSimplyConnectedStatus));
        Assertions.assertEquals(1,
                main.countDomainsInList(false, Task.Status.IS_SIMPLY_CONNECTED, domainsWithSimplyConnectedStatus));
    }
}
