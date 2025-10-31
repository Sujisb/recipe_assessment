package com.example.recipe_api.controller;

import com.example.recipe_api.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin
public class Controller {

    @Autowired
    private MenuService menuService;

    // ✅ API 1: Get all recipes (Paginated & Sorted)   http://localhost:8080/api/recipes?page=1&limit=10
    @GetMapping
    public Map<String, Object> getAllRecipes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return menuService.getAllRecipes(page, limit);
    }

    // ✅ API 2: Search Recipes   http://localhost:8080/api/recipes/search?calories=<=400&title=pie&rating=>=4.5

    @GetMapping("/search")
    public Map<String, Object> searchRecipes(
            @RequestParam(required = false) String calories,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false, name = "total_time") String totalTime,
            @RequestParam(required = false) String rating
    ) {
        return menuService.searchRecipes(calories, title, cuisine, totalTime, rating);
    }
}
