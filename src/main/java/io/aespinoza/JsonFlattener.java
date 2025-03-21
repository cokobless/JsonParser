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
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                String newKey = prefix + "[" + i + "]";
                flattenJsonRecursive(newKey, node.get(i), flatList, mapper);
            }
        } else if (node.isValueNode()) {
            ObjectNode objNode = mapper.createObjectNode();
            objNode.put("key", prefix);
            objNode.put("value", node.asText());
            flatList.add(objNode);
        }
    }

    public static void main(String[] args) {
        String jsonInput = "{\"id\":\"ee6b8018-66d2-46d0-9803-cbce2ecc39e7\",\"object\":\"chat.completion\",\"created\":1741290146,\"model\":\"deepseek-chat\",\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"Python es un lenguaje de programación...\"},\"logprobs\":null,\"finish_reason\":\"stop\"}],\"usage\":{\"prompt_tokens\":14,\"completion_tokens\":386,\"total_tokens\":400,\"prompt_tokens_details\":{\"cached_tokens\":0},\"prompt_cache_hit_tokens\":0,\"prompt_cache_miss_tokens\":14},\"system_fingerprint\":\"fp_3a5770e1b4_prod0225\"}";
        String resultJson = flattenJson(jsonInput);
        System.out.println(resultJson);
    }
}
