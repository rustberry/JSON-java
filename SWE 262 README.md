# SWE 262 README

## Milestone 3

### Performance difference between M1 task 4 and M3

Milestone 1's task 4 requires changing all the keys of the JSONObject read from an XML file. I did it in a two-pass manner: first read it into a JSON object, then iterate through this object and recursively change all the keys. The performance  cost is the sum of the cost of parsing an XML file plus the cost of literally parsing the generated JSONObject.

With the new method in Milestone 3, the modification of keys is done while parsing XML files, right before adding the parsed tag pair into the JSONObject result. What it takes is only the cost of the transformation function.

### Notes about Milestone 3

To make it easier to read my code:

- `myParse3` in `XML.java` is the newly implemented method that does the work of Milestone 3;
- the test file is `M3Test.java`, which tests both multilevel JSON object as well as JSON array. 