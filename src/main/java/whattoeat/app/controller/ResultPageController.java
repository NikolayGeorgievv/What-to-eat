package whattoeat.app.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import whattoeat.app.dto.RecipeDTO;
import whattoeat.app.service.service.RecipeService;
import whattoeat.app.service.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        model.addAttribute("favoritesMap", getFavorites());

        return "resultPage";
    }

    @GetMapping("/recipe")
    public String getRecipe(@RequestParam("title") String title, Model model) {
        RecipeDTO recipe = recipeService.findByTitle(title);
        model.addAttribute("recipe", recipe);
        return "singleRecipeResult";
    }


    @ModelAttribute("favoritesMap")
    public Map<Long, Boolean> getFavorites() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        if (userEmail.equals("anonymousUser")) {
            return new HashMap<>();
        }
        return userService.getFavorites(userEmail);
    }

    @PostMapping("/addToFavorite")
    @ResponseBody
    public Map<String, Object> addToFavorite(@RequestParam Long recipeId, Authentication authentication) {
        String userEmail = authentication.getName();
        try {
            userService.addRecipeToFavorites(recipeId, userEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("favoritesMap", getFavorites());
        return response;
    }

    @PostMapping("/removeFromFavorite")
    @ResponseBody
    public Map<String, Object> removeFromFavorite(@RequestParam Long recipeId, Authentication authentication) {
        String userEmail = authentication.getName();
        userService.removeRecipeFromFavorites(recipeId, userEmail);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("favoritesMap", getFavorites());
        return response;
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
    @ModelAttribute("userNotifications")
    public List<String> getUserNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return userService.getUserNotifications(userEmail);
    }
}
