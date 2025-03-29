package whattoeat.app.service.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static whattoeat.app.constants.Prompts.*;

@Service
public class RecipeGenerator {

    private final String API_URL = "https://api.openai.com/v1/chat/completions";
    @Value("${recipeGenerator}")
    private String API_KEY;
    private String requestBody;

    public String getRecipe(String searchType, String ingredients, String recipeName) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        if (searchType.equals("product")){
            requestBody = getSearchByProductsRequestBody(ingredients);
        } else if (searchType.equals("recipe")) {
            requestBody = getSearchByRecipeNameRequestBody(recipeName);
        }

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

    public String generateAnotherRecipeWithGivenIngredientsOrRecipeName(List<String> ingredients, List<String> previousRecipes) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        requestBody = getGenerateAnotherRecipeRequestBody(ingredients, previousRecipes);

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

    private String getSearchByProductsRequestBody(String ingredients) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "system", "content", PROMPT_FOR_INGREDIENTS_INPUT
                        ),
                        Map.of("role", "user", "content", ingredients)),
                "max_tokens", 800
        ));
    }

    private String getSearchByRecipeNameRequestBody(String recipeName) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "system", "content", PROMPT_FOR_RECIPE_NAME_INPUT),
                        Map.of("role", "user", "content", recipeName)),
                "max_tokens", 800
        ));
    }

    private String getGenerateAnotherRecipeRequestBody(List<String> ingredients, List<String> previousRecipes) throws JsonProcessingException {
        String previousRecipesText = "Previously generated recipes: " + (previousRecipes.isEmpty() ? "None" : String.join(", ", previousRecipes));
        String ingredientsText = "Ingredients: " + String.join(", ", ingredients);

        return new ObjectMapper().writeValueAsString(Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "system", "content", PROMPT_FOR_GENERATE_ANOTHER_RECIPE),
                        Map.of("role", "user", "content", ingredientsText),
                        Map.of("role", "user", "content", previousRecipesText)),
                "max_tokens", 800
        ));

    }

}
