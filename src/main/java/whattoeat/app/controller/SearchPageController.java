package whattoeat.app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchPageController {

    @GetMapping("/searchPage")
    private String getSearchPage() {
        return "searchPage";
    }


}
