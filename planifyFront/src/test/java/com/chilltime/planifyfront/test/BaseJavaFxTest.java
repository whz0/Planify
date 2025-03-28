package com.chilltime.planifyfront.test;

import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

public abstract class BaseJavaFxTest extends ApplicationTest {

    @Override
    public void init() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
    }

    public FxRobot sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}