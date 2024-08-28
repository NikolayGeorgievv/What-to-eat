package whattoeat.app.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import whattoeat.app.model.Ingredient;
import whattoeat.app.model.Recipe;
import whattoeat.app.model.RecipeIngredient;
import whattoeat.app.repository.IngredientRepository;
import whattoeat.app.repository.RecipeIngredientsRepository;
import whattoeat.app.repository.RecipeRepository;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CSVService {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientsRepository recipeIngredientsRepository;

    public CSVService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository, RecipeIngredientsRepository recipeIngredientsRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.recipeIngredientsRepository = recipeIngredientsRepository;
    }

    public void readCsvAndInsertData() {
        String filePath = "C:\\Users\\skull\\coding\\app\\src\\main\\java\\whattoeat\\app\\recipesInfo.csv";
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();

            System.out.println("test");

            for (int i = 1; i <= records.size(); i++) {
                List<RecipeIngredient> ingredientsList = new ArrayList<>();
                String[] currentRecord = records.get(i);
                Recipe recipe = new Recipe();
                recipe.setName(currentRecord[1]);
                recipe.setPreparationDescription(currentRecord[2]);
                recipeRepository.saveAndFlush(recipe);

                List<String> ingredientsArr = Arrays.stream(currentRecord[0].split("\\n")).toList();

                ingredientsArr.forEach(ingredient ->{
                    //check if the ingredient already exists in the database
                    Ingredient ingredientEntity = new Ingredient();
                    String ingredientName = ingredient.split(" - ")[0];
                    ingredientEntity.setName(ingredientName);
                    ingredientRepository.saveAndFlush(ingredientEntity);



                    RecipeIngredient recipeIngredientEntity = new RecipeIngredient();
                    String quantity = ingredient.split(" - ")[1];
                    recipeIngredientEntity.setIngredient(ingredientEntity);
                    recipeIngredientEntity.setQuantity(quantity);
                    recipeIngredientEntity.setRecipe(recipe);
                    recipeIngredientsRepository.saveAndFlush(recipeIngredientEntity);
                    ingredientsList.add(recipeIngredientEntity);
                });

                recipeRepository.findByName(recipe.getName()).ifPresent(recipeOpt -> {
                    recipeOpt.setIngredients(ingredientsList);
                    recipeRepository.saveAndFlush(recipeOpt);
                });

            }


        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
}
