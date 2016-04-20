package biz.snowdragon.easylog;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class EasyLogTest {
    private EasyLog el;

    @Before
    public void setUp() throws Exception {
        el = new EasyLog("test.log", 10);
    }

    @Test
    public void logMessage() throws Exception {
        for (int i = 0; i < 10; i++) el.logMessageWithTime("test " + i);
        Thread.sleep(100);
        assertEquals(0, el.getQueueSize());

        for (int i = 0; i < 1000; i++) el.logMessageWithTime("test " + i);
        Thread.sleep(100);
        assertEquals(0, el.getQueueSize());

    }

    @Test
    public void flush() throws Exception {
        for (int i = 0; i < 9; i++) el.logMessageWithTime("test " + i);
        assertEquals(9, el.getQueueSize());

        el.flush();
        Thread.sleep(100);
        assertEquals(0, el.getQueueSize());
    }

}