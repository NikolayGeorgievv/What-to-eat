package whattoeat.app.service.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class RecipeGenerator {

    private final String API_URL = "https://api.openai.com/v1/chat/completions";
    @Value("${recipeGenerator}")
    private String API_KEY;

    public String getRecipe(String userInput) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        String requestBody = new ObjectMapper().writeValueAsString(Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(Map.of("role", "user", "content", userInput)),
                "max_tokens", 800
        ));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse response
        JsonNode jsonResponse = new ObjectMapper().readTree(response.body());
        return jsonResponse.get("choices").get(0).get("message").get("content").asText();
    }
}
