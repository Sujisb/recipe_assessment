package com.example.recipe_api.repository;

import com.example.recipe_api.model.Model;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Repo extends JpaRepository<Model, Long> {

    // ✅ API 1 - Get all recipes sorted by rating desc
    @Query(value = "SELECT * FROM menu ORDER BY rating DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Model> findAllRecipes(@Param("limit") int limit, @Param("offset") int offset);

    // ✅ API 2 - Dynamic search
    @Query(value = """
        SELECT * FROM menu 
        WHERE (:title IS NULL OR title LIKE %:title%)
          AND (:cuisine IS NULL OR cuisine = :cuisine)
          AND (:ratingOp IS NULL OR 
               CASE 
                    WHEN :ratingOp = '>' THEN rating > :ratingValue
                    WHEN :ratingOp = '<' THEN rating < :ratingValue
                    WHEN :ratingOp = '>=' THEN rating >= :ratingValue
                    WHEN :ratingOp = '<=' THEN rating <= :ratingValue
                    WHEN :ratingOp = '=' THEN rating = :ratingValue
                    ELSE TRUE
               END)
          AND (:totalTimeOp IS NULL OR 
               CASE 
                    WHEN :totalTimeOp = '>' THEN total_time > :totalTimeValue
                    WHEN :totalTimeOp = '<' THEN total_time < :totalTimeValue
                    WHEN :totalTimeOp = '>=' THEN total_time >= :totalTimeValue
                    WHEN :totalTimeOp = '<=' THEN total_time <= :totalTimeValue
                    WHEN :totalTimeOp = '=' THEN total_time = :totalTimeValue
                    ELSE TRUE
               END)
          AND (:caloriesOp IS NULL OR 
               CASE 
                    WHEN :caloriesOp = '>' THEN CAST(JSON_UNQUOTE(JSON_EXTRACT(nutrients, '$.calories')) AS UNSIGNED) > :caloriesValue
                    WHEN :caloriesOp = '<' THEN CAST(JSON_UNQUOTE(JSON_EXTRACT(nutrients, '$.calories')) AS UNSIGNED) < :caloriesValue
                    WHEN :caloriesOp = '>=' THEN CAST(JSON_UNQUOTE(JSON_EXTRACT(nutrients, '$.calories')) AS UNSIGNED) >= :caloriesValue
                    WHEN :caloriesOp = '<=' THEN CAST(JSON_UNQUOTE(JSON_EXTRACT(nutrients, '$.calories')) AS UNSIGNED) <= :caloriesValue
                    WHEN :caloriesOp = '=' THEN CAST(JSON_UNQUOTE(JSON_EXTRACT(nutrients, '$.calories')) AS UNSIGNED) = :caloriesValue
                    ELSE TRUE
               END)
        """, nativeQuery = true)
    List<Model> searchRecipes(
            @Param("title") String title,
            @Param("cuisine") String cuisine,
            @Param("ratingOp") String ratingOp,
            @Param("ratingValue") Double ratingValue,
            @Param("totalTimeOp") String totalTimeOp,
            @Param("totalTimeValue") Integer totalTimeValue,
            @Param("caloriesOp") String caloriesOp,
            @Param("caloriesValue") Integer caloriesValue
    );
}
