package io.aespinoza;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonValidator {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int SUCCESS_CODE = 0;
    private static final int ERROR_CODE = 1;
    private static final String SUCCESS_MESSAGE = "Ok";

    public static String validateJson(String jsonString) {
        try {
            // Intenta parsear el JSON de entrada
            objectMapper.readTree(jsonString);

            // Si no hay excepción, crear respuesta de éxito
            ObjectNode response = objectMapper.createObjectNode();
            response.put("errCod", SUCCESS_CODE);
            response.put("errDes", SUCCESS_MESSAGE);

            return response.toString();

        } catch (JsonParseException e) {
            // Para errores de parsing con ubicación específica
            String errorMessage = String.format("Error en la línea %d, columna %d: %s",
                    e.getLocation().getLineNr(),
                    e.getLocation().getColumnNr(),
                    e.getOriginalMessage());

            ObjectNode response = objectMapper.createObjectNode();
            response.put("errCod", ERROR_CODE);
            response.put("errDes", errorMessage);

            return response.toString();

        } catch (JsonProcessingException e) {
            // Para otros errores de procesamiento
            ObjectNode response = objectMapper.createObjectNode();
            response.put("errCod", ERROR_CODE);
            response.put("errDes", "Error al procesar el JSON: " + e.getMessage());

            return response.toString();
        }
    }

    // Método de ejemplo para usar la clase
    public static void main(String[] args) {
        // Ejemplo con JSON válido
        String validJson = """
            {
                "name": "John",
                "age": 30,
                "city": "New York"
            }""";

        // Ejemplo con JSON inválido (falta una coma)
        String invalidJson = """
            {
                "name": "John"
                "age": 30,
                "city": "New York"
            }""";

        // Probar con JSON válido
        System.out.println("Validando JSON válido:");
        System.out.println(validateJson(validJson));

        // Probar con JSON inválido
        System.out.println("\nValidando JSON inválido:");
        System.out.println(validateJson(invalidJson));
    }
}