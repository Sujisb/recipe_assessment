package com.example.recipe_api.service;

import com.example.recipe_api.model.Model;
import com.example.recipe_api.repository.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.*;

@Service
public class MenuService {

    @Autowired
    private Repo repo;

    // ✅ API 1 - Get all recipes paginated
    public Map<String, Object> getAllRecipes(int page, int limit) {
        int offset = (page - 1) * limit;
        List<Model> recipes = repo.findAllRecipes(limit, offset);
        long total = repo.count();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", page);
        response.put("limit", limit);
        response.put("total", total);
        response.put("data", recipes);
        return response;
    }

    // ✅ Helper: Parse operator-based query like <=400, >=4.5 etc.
    private Map<String, String> parseExpression(String input) {
        Map<String, String> map = new HashMap<>();
        if (input == null || input.isEmpty()) return map;

        Pattern pattern = Pattern.compile("^(<=|>=|=|<|>)?\\s*(\\d+(\\.\\d+)?)$");
        Matcher matcher = pattern.matcher(input.trim());

        if (matcher.matches()) {
            map.put("op", matcher.group(1) != null ? matcher.group(1) : "=");
            map.put("value", matcher.group(2));
        }
        return map;
    }

    // ✅ API 2 - Search recipes dynamically
    public Map<String, Object> searchRecipes(String calories, String title, String cuisine, String totalTime, String rating) {
        Map<String, String> calMap = parseExpression(calories);
        Map<String, String> rateMap = parseExpression(rating);
        Map<String, String> timeMap = parseExpression(totalTime);

        String calOp = calMap.get("op");
        String rateOp = rateMap.get("op");
        String timeOp = timeMap.get("op");

        Integer calValue = calMap.containsKey("value") ? Integer.parseInt(calMap.get("value")) : null;
        Double rateValue = rateMap.containsKey("value") ? Double.parseDouble(rateMap.get("value")) : null;
        Integer timeValue = timeMap.containsKey("value") ? Integer.parseInt(timeMap.get("value")) : null;

        List<Model> results = repo.searchRecipes(
                title != null && !title.isBlank() ? title : null,
                cuisine != null && !cuisine.isBlank() ? cuisine : null,
                rateOp, rateValue,
                timeOp, timeValue,
                calOp, calValue
        );

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", results);
        return response;
    }
}
