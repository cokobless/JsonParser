package io.aespinoza;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonFlattener {
    public static String flattenJson(String jsonInput) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonInput);
            List<ObjectNode> flatList = new ArrayList<>();
            flattenJsonRecursive("", rootNode, flatList, mapper);
            return mapper.writeValueAsString(flatList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void flattenJsonRecursive(String prefix, JsonNode node, List<ObjectNode> flatList, ObjectMapper mapper) {
        if (node.isObject()) {
            Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                String newKey = prefix.isEmpty() ? fieldName : prefix + "." + fieldName;
                flattenJsonRecursive(newKey, node.get(fieldName), flatList, mapper);
            }
        } else if (node.isValueNode()) {
            ObjectNode objNode = mapper.createObjectNode();
            objNode.put("key", prefix);
            objNode.put("value", node.asText());
            flatList.add(objNode);
        }
    }

    public static void main(String[] args) {
        String jsonInput = "{\"level1\":{\"level2\":{\"key1\":\"value1\",\"key2\":\"value2\"},\"key3\":\"value3\"}}";
        String resultJson = flattenJson(jsonInput);
        System.out.println(resultJson);
    }
}
