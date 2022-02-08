package org.json.junit;

import org.json.JSONObject;
import org.json.XML;
import org.junit.Test;

import java.io.StringReader;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class M3Test {
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

    @Test
    public void testFunction() {
        String prefix = "swe262_";
        JSONObject jo = XML.toJSONObject(new StringReader(xmlString1),
                (key) -> prefix + key);
        for (String key : jo.keySet()) {
            assertTrue(key.startsWith(prefix));
        }
    }

    @Test
    public void testArray() {
        Function<String, String> transformer = String::toUpperCase;
        JSONObject jo = XML.toJSONObject(new StringReader(xmlStrArray), transformer);
        for (String key : jo.keySet()) {
            assertTrue(isUpperCase(key));
        }
    }

    @Test
    public void testEmpty() {
        JSONObject jo = XML.toJSONObject(new StringReader(xmlString1),
                (key) -> "");
        for (String key : jo.keySet()) {
            assertEquals("", key);
        }
    }

    public static boolean isUpperCase(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isUpperCase(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
