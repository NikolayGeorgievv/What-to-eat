package whattoeat.app.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import whattoeat.app.model.Ingredient;
import whattoeat.app.model.Recipe;
import whattoeat.app.model.RecipeIngredient;
import whattoeat.app.repository.IngredientRepository;
import whattoeat.app.repository.RecipeIngredientsRepository;
import whattoeat.app.repository.RecipeRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CSVService {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientsRepository recipeIngredientsRepository;

    @Value("${file1Path}")
    private String file1Path;

    public CSVService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository, RecipeIngredientsRepository recipeIngredientsRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.recipeIngredientsRepository = recipeIngredientsRepository;
    }

    public void readCsvAndInsertData() {
        String filePath = file1Path;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();


            for (int i = 1; i <= records.size() - 1; i++) {
                List<RecipeIngredient> ingredientsList = new ArrayList<>();
                String[] currentRecord = records.get(i);

                if (recipeRepository.findByName(currentRecord[1]).isPresent()) {
                    continue;
                }

                Recipe recipe = new Recipe();
                recipe.setName(currentRecord[1]);
                recipe.setPreparationDescription(currentRecord[2]);
                recipeRepository.saveAndFlush(recipe);

                List<String> ingredientsArr = Arrays.stream(currentRecord[0].split("\\n")).toList();

                ingredientsArr.forEach(ingredient ->{
                    Ingredient ingredientEntity = new Ingredient();
                    RecipeIngredient recipeIngredientEntity = new RecipeIngredient();

                    //check if the ingredient is already in the database
                    Optional<Ingredient> ingredientOpt = ingredientRepository.findByName(ingredient.split(" - ")[0]);
                    if (ingredientOpt.isPresent()) {
                        ingredientEntity = ingredientOpt.get();
                    }else {
                        String ingredientName = ingredient.split(" - ")[0];
                        ingredientEntity.setName(ingredientName);
                    }
                    ingredientRepository.saveAndFlush(ingredientEntity);


                    //check if the ingredient has quantity
                    if (ingredient.split(" - ").length != 2) {
                        //no quantity
                        recipeIngredientEntity.setIngredient(ingredientEntity);
                        recipeIngredientEntity.setRecipe(recipe);
                        recipeIngredientsRepository.saveAndFlush(recipeIngredientEntity);

                    }else {
                        //has quantity
                        String quantity = ingredient.split(" - ")[1];
                        recipeIngredientEntity.setIngredient(ingredientEntity);
                        recipeIngredientEntity.setQuantity(quantity);
                        recipeIngredientEntity.setRecipe(recipe);
                        recipeIngredientsRepository.saveAndFlush(recipeIngredientEntity);

                    }

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
