package org.json.junit;

import org.json.JSONObject;
import org.json.XML;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class M4Test {
    private String jsonStr = "{\"menu\": {\n" +
            "  \"id\": \"file\",\n" +
            "  \"value\": \"File\",\n" +
            "  \"popup\": {\n" +
            "    \"menuitem\": [\n" +
            "      {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},\n" +
            "      {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},\n" +
            "      {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}\n" +
            "    ]\n" +
            "  }\n" +
            "}}";

    @Test
    public void testFunctionality() {
        JSONObject jo = new JSONObject(jsonStr);
        List<JSONObject> li = jo.toStream().collect(Collectors.toList());
        List<String> correctKeys = List.of(
                "menu",
                    "popup",
                        "menuitem",
                        "onclick", "onclick", "value",
                        "onclick", "onclick", "value",
                        "onclick", "onclick", "value",
                    "id",
                    "value"
        );
        for (int i = 0; i < li.size(); i++) {
            JSONObject li_obj = li.get(i);
            assertEquals(correctKeys.get(i), li_obj.keys().next());
        }
    }

    @Test
    public void testEmpty() {
        JSONObject jo = XML.toJSONObject("");
        jo.toStream().forEach(jobj -> assertEquals("{}", jobj.toString()));
    }
}
