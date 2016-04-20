package biz.snowdragon.easylog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class LogPrinterTest {

    private LogPrinter lp;
    private File log;

    @Before
    public void setUp() throws Exception {
        lp = new LogPrinter("test.log");
        log = new File("test.log");
    }

    @Test
    public void println() throws Exception {
        long filesizeBefore = log.length();
        lp.println("Test");
        lp.flush();
        long filesizeAfter = log.length();

        assertTrue(filesizeAfter > filesizeBefore);
    }

    @After
    public void tearDown() throws Exception {
        lp.close();
    }
}