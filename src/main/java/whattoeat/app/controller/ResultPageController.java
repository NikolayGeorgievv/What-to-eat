package whattoeat.app.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import whattoeat.app.dto.RecipeDTO;
import whattoeat.app.service.service.RecipeService;
import whattoeat.app.service.service.UserService;

@Controller
public class ResultPageController {

    private final RecipeService recipeService;
    private final UserService userService;

    public ResultPageController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }


    @GetMapping("/resultPage")
    private String getSearchPage() {
        return "resultPage";
    }


    @GetMapping("/results")
    public Page<RecipeDTO> getResults(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
        return recipeService.getMatches(PageRequest.of(page, size));
    }

    @GetMapping("/search")
    public String search(@RequestParam String searchType, @RequestParam(required = false) String productName, @RequestParam(required = false) String recipeName, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, Model model) {
        setModelAttributes(model, searchType, productName, recipeName, page, size);

        Page<RecipeDTO> resultPage;
        if ("product".equals(searchType)) {
            resultPage = recipeService.searchByProductName(productName, PageRequest.of(page, size));
        } else {
            resultPage = recipeService.searchByRecipeName(recipeName, PageRequest.of(page, size));
        }
        model.addAttribute("page", resultPage);

        return "resultPage";
    }

    @GetMapping("/recipe")
    public String getRecipe(@RequestParam("title") String title, Model model) {
        RecipeDTO recipe = recipeService.findByTitle(title);
        model.addAttribute("recipe", recipe);
        return "popularRecipeResult";
    }


    @ModelAttribute("isFavorite")
    public boolean isFavorite(@RequestParam(required = false) Long recipeId) {
        if (recipeId == null) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userService.isFavorite(recipeId, userEmail);
    }


    @PostMapping("/addToFavorite")
    public String addToFavorites(
            @RequestParam Long recipeId,
            @RequestParam String searchType,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String recipeName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        userService.addRecipeToFavorites(recipeId, userEmail);

        return redirectResultPage(searchType, productName, recipeName, page, size, model);
    }

    @PostMapping("/removeFromFavorite")
    public String removeFromFavorites(
            @RequestParam Long recipeId,
            @RequestParam String searchType,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String recipeName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        userService.removeRecipeFromFavorites(recipeId, userEmail);

        return redirectResultPage(searchType, productName, recipeName, page, size, model);
    }

    private String redirectResultPage(
            @RequestParam String searchType,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String recipeName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            Model model) {
        setModelAttributes(model, searchType, productName, recipeName, page, size);

        Page<RecipeDTO> resultPage;
        if ("product".equals(searchType)) {
            resultPage = recipeService.searchByProductName(productName, PageRequest.of(page, size));
        } else if ("recipe".equals(searchType)) {
            resultPage = recipeService.searchByRecipeName(recipeName, PageRequest.of(page, size));
        } else {
            throw new IllegalArgumentException("Invalid search type");
        }
        model.addAttribute("page", resultPage);

        return "resultPage";
    }

    private void setModelAttributes(Model model, String searchType, String productName, String recipeName, int page, int size) {
        model.addAttribute("searchType", searchType);
        model.addAttribute("productName", productName);
        model.addAttribute("recipeName", recipeName);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
    }
}
