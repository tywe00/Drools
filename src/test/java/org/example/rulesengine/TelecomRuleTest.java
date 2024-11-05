package org.example.rulesengine;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.drools.core.time.SessionPseudoClock;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TelecomRuleTest {

    static final Logger LOG = LoggerFactory.getLogger(TelecomRuleTest.class);
    private KieSession kSession;
    private SessionPseudoClock pseudoClock;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setup() {
        LOG.info("Setting up KieSession");
        try {
            KieServices ks = KieServices.Factory.get();
            KieContainer kContainer = ks.getKieClasspathContainer();
            kSession = kContainer.newKieSession("ksession-rules");
            pseudoClock = kSession.getSessionClock();
            

            if (kSession == null) {
                LOG.error("Failed to create KieSession");
                throw new IllegalStateException("KieSession could not be initialized.");
            } else {
                LOG.info("KieSession created successfully");
            }
        } catch (Exception e) {
            LOG.error("Error setting up KieSession", e);
            throw e; // Ensure exception fails setup if KieSession is not available
        }
        System.setOut(new PrintStream(outContent));
    }


    @After
    public void tearDown() {
        LOG.info("Cleaning up KieSession");
        if (kSession != null) {
            kSession.dispose();
        }
        System.setOut(originalOut);
    }

    @Test
    public void testCriticalFaultDetection() {
        LOG.info("Testing Critical Fault Detection");
        Event criticalEvent = new Event("1", "Fault", System.currentTimeMillis(), "Router1", 4, "System Crash");
        kSession.insert(criticalEvent);
        kSession.fireAllRules();

        assertTrue(outContent.toString().contains("Critical Fault Detected: System Crash"));
    }

    @Test
    public void testMultipleFaultsFromSameSource() {
        LOG.info("Testing Multiple Faults from Same Source");
        long currentTime = System.currentTimeMillis();
        Event event1 = new Event("1", "Fault", currentTime, "Router1", 2, "Connection lost");
        Event event2 = new Event("2", "Fault", currentTime + 60000, "Router1", 3, "Hardware failure");

        kSession.insert(event1);
        kSession.insert(event2);
        kSession.fireAllRules();

        assertTrue(outContent.toString().contains("Multiple faults detected from source: Router1"));
    }

    @Test
    public void testEscalatingSeverity() {
        LOG.info("Testing Escalating Severity");
        long currentTime = System.currentTimeMillis();
        Event event1 = new Event("1", "Fault", currentTime, "Switch1", 1, "Minor issue");
        Event event2 = new Event("2", "Fault", currentTime + 30000, "Switch1", 2, "Major issue");

        kSession.insert(event1);
        kSession.insert(event2);
        kSession.fireAllRules();

        assertTrue(outContent.toString().contains("Escalating severity detected for source: Switch1"));
    }

    @Test
    public void testRepeatedHighLatency() {
        LOG.info("Testing Repeated High Latency");

        long currentTime = System.currentTimeMillis();
        Event event1 = new Event("1", "Warning", currentTime, "Switch1", 1, "High Latency");
        Event event2 = new Event("2", "Fault", currentTime + 120000, "Switch1", 3, "High Latency");  // 2 minutes later
        Event event3 = new Event("3", "Fault", currentTime + 125000, "Switch1", 4, "High Latency");  // 2 minutes later

        kSession.insert(event1);
        kSession.fireAllRules();

        //pseudoClock.advanceTime(9, TimeUnit.MINUTES);
        kSession.insert(event2);
        kSession.insert(event3);
        kSession.fireAllRules();
        assertTrue(outContent.toString().contains("High latency detected repeatedly from source: Switch1"));
    }

  
}