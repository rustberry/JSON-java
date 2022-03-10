package org.json.junit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.junit.Test;

import java.io.StringReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

public class M5Test {
    private String xmlString1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                "<contact>\n" +
                                "  <nick>Crista </nick>\n" +
                                "  <name>Crista Lopes</name>\n" +
                                "  <address>\n" +
                                "    <street>Ave of Nowhere</street>\n" +
                                "    <zipcode>92614</zipcode>\n" +
                                "  </address>\n" +
                                "</contact>\n" +
                                "<second>Top second level</second>";

    private String xmlStrArray = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                 "  <food>\n" +
                                 "    <name>Belgian Waffles</name>\n" +
                                 "    <price>$5.95</price>\n" +
                                 "    <description>Two of our famous Belgian Waffles with plenty of real maple syrup</description>\n" +
                                 "    <calories>650</calories>\n" +
                                 "  </food>\n" +
                                 "  <food>\n" +
                                 "    <name>Strawberry Belgian Waffles</name>\n" +
                                 "    <price>$7.95</price>\n" +
                                 "    <description>Light Belgian waffles covered with strawberries and whipped cream</description>\n" +
                                 "    <calories>900</calories>\n" +
                                 "  </food>\n" +
                                 "  <food>\n" +
                                 "    <name>Berry-Berry Belgian Waffles</name>\n" +
                                 "    <price>$8.95</price>\n" +
                                 "    <description>Light Belgian waffles covered with an assortment of fresh berries and whipped cream</description>\n" +
                                 "    <calories>900</calories>\n" +
                                 "  </food>";

    private String illegal = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                             "<contact>\n";

    /**
     * Test the functionality of the callback style parsing.
     */
    @Test
    public void testFunctionalityCallback1() throws InterruptedException {
        JSONObject jo = XML.toJSONObject(new StringReader(xmlString1));
        CountDownLatch latch = new CountDownLatch(1);
        XML.toJSONObject(new StringReader(xmlString1),
             (parsed) -> {
                 assertEquals("Top second level", parsed.get("second"));
                 assertEquals("Crista", parsed.query("/contact/nick"));
                 assertEquals(jo.toString(), parsed.toString());
                 latch.countDown();
             },
             System.out::println
        );
        latch.await();  // wait until all the asserts are done
    }

    @Test
    public void testFunctionalityCallback2() throws InterruptedException {
        JSONObject jo = XML.toJSONObject(new StringReader(xmlStrArray));
        CountDownLatch latch = new CountDownLatch(1);
        XML.toJSONObject(new StringReader(xmlStrArray),
             (parsed) -> {
                 assertEquals("$5.95",
                              ((JSONObject) ((JSONArray) parsed.get("food")).get(0))
                                      .get("price"));
                 assertEquals(jo.toString(), parsed.toString());
                 latch.countDown();
             },
             System.out::println
        );
        latch.await();  // wait until all the asserts are done
    }

    /**
     * Test the functionality of the Future style parsing
     */
    @Test
    public void testFunctionalityFuture1() throws ExecutionException, InterruptedException {
        JSONObject jo = XML.toJSONObject(new StringReader(xmlString1));
        Future<JSONObject> future = XML.toJSONObjectFuture(new StringReader(xmlString1));
        JSONObject parsed = future.get();
        assertEquals("Top second level", parsed.get("second"));
        assertEquals("Crista", parsed.query("/contact/nick"));
        assertEquals(jo.toString(), parsed.toString());
    }

    @Test
    public void testFunctionalityFuture2() throws InterruptedException, ExecutionException {
        JSONObject jo = XML.toJSONObject(new StringReader(xmlStrArray));
        Future<JSONObject> future = XML.toJSONObjectFuture(new StringReader(xmlStrArray));
        JSONObject parsed = future.get();
        assertEquals("$5.95",
            ((JSONObject) ((JSONArray) parsed.get("food")).get(0))
                 .get("price"));
        assertEquals(jo.toString(), parsed.toString());
    }

    /**
     * Test exception handling.
     * @throws InterruptedException
     */
    @Test
    public void testExceptionCallback() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        XML.toJSONObject(new StringReader(illegal),
            (parsed) -> {
                System.out.println("cannot reach here");
                latch.countDown();
            },
            (exp) -> {
                assertNotNull(exp);
                assertTrue(exp.getMessage().startsWith("Unclosed tag contact"));
                latch.countDown();
            }
        );
        latch.await();
    }

    /**
     * Test exception handling.
     * @throws InterruptedException
     */
    @Test
    public void testExceptionFuture() throws InterruptedException {
        try {
            XML.toJSONObjectFuture(new StringReader(illegal)).get();
        } catch (ExecutionException e) {
            assertNotNull(e);
            assertEquals("org.json.JSONException: Unclosed tag contact at 49 [character 0 line 3]",
            e.getMessage());
        }
    }
}
