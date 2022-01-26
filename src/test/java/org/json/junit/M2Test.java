package org.json.junit;

import java.io.StringReader;

import org.json.JSONException;
import org.json.JSONPointer;
import org.json.JSONObject;
import org.json.XML;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class M2Test {
    String xmlString1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
            "<contact>\n"+
            "  <nick>Crista </nick>\n"+
            "  <name>Crista Lopes</name>\n" +
            "  <address>\n" +
            "    <street>Ave of Nowhere</street>\n" +
            "    <zipcode>92614</zipcode>\n" +
            "  </address>\n" +
            "</contact>\n" +
            "<second>Top second level</second>";

    @Test
    public void test1() {
        try {
            JSONObject jobj = XML.toJSONObject(new StringReader(xmlString1), new JSONPointer(
                    "/contact/address/street"));
            JSONObject ans = new JSONObject().put("street", "Ave of Nowhere");
            assertTrue("get a single inner tag", ans.similar(jobj));
            // /second  /contact/address
            System.out.println(jobj);
            jobj = XML.toJSONObject(new StringReader(xmlString1), new JSONPointer(
                    "/contact/address/"));
            ans.put("zipcode", 92614);
            System.out.println(jobj);
            assertTrue("get multiple inner tags", ans.similar(jobj));

            jobj = XML.toJSONObject(new StringReader(xmlString1), new JSONPointer(
                    "/second/"));
            ans = new JSONObject().put("second", "Top second level");
            System.out.println(jobj);
            assertTrue("get the second tag at top level", ans.similar(jobj));

            jobj = XML.toJSONObject(new StringReader(xmlString1), new JSONPointer(
                    "/illegal/"));
            System.out.println(jobj);
            assertTrue("test illegal path", jobj.similar(new JSONObject()));
        } catch (JSONException e) {
            System.out.println(e);
        }
    }

    @Test
    public void test2() {
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                "<contact>\n"+
                "  <nick>Crista </nick>\n"+
                "  <name>Crista Lopes</name>\n" +
                "  <address>\n" +
                "    <street>Ave of Nowhere</street>\n" +
                "    <zipcode>92614</zipcode>\n" +
                "  </address>\n" +
                "</contact>";

        try {
            JSONObject replacement = XML.toJSONObject("<street>Ave of the Arts</street>\n");
            System.out.println("Given replacement: " + replacement);
            String qryPath = "/contact/address/street";
            JSONObject jobj = XML.toJSONObject(new StringReader(xmlString),
                    new JSONPointer(qryPath), replacement);
            System.out.println(jobj);
            System.out.println(jobj.optQuery(qryPath));
            assertTrue(jobj.optQuery(qryPath).toString().endsWith("Arts"));


            replacement = XML.toJSONObject("<street>Ave of the Arts</street>\n" +
                    "<zipcode>99999</zipcode>\n");
            jobj = XML.toJSONObject(new StringReader(xmlString1), new JSONPointer(
                    "/contact/address/"), replacement);
            System.out.println(jobj);
            assertTrue("replace multiple inner tags",
                    replacement.similar(jobj.optQuery("/contact/address")));

            replacement = XML.toJSONObject("<second>Replaced second</second>");
            jobj = XML.toJSONObject(new StringReader(xmlString1), new JSONPointer(
                    "/second/"), replacement);
            System.out.println(jobj);
            assertEquals("replace the second tag at top level", "Replaced second", jobj.optQuery("/second"));
        } catch (JSONException e) {
            System.out.println(e);
        }
    }

    @Test
    public void testIllegal() {
        JSONObject replacement = XML.toJSONObject("<street>Ave of the Arts</street>\n");
        JSONObject jobj = XML.toJSONObject(new StringReader(xmlString1), new JSONPointer(
                "/illegal/"), replacement);
        JSONObject original = XML.toJSONObject(xmlString1);
        System.out.println("Illegal path: " + jobj.toString());
        assertTrue("test illegal path", jobj.similar(original));
    }
}

