package com.example.ecommerce.specification;

import com.example.ecommerce.domain.ItemCondition;
import com.example.ecommerce.dto.request.ProductFilterRequestDTO;
import com.example.ecommerce.model.Brand;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.ProductVariant;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Production-grade JPA Criteria Specification for dynamic product filtering.
 * Every predicate is only applied when the corresponding filter value is present.
 */
public class ProductSpecification {

    private ProductSpecification() {}

    public static Specification<Product> withFilters(ProductFilterRequestDTO filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // ----------------------------------------------------------------
            // JOIN — ProductVariant (for price, stock, discount filters)
            // Use LEFT JOIN so products without variants are still returned
            // unless inStockOnly / price filter explicitly needs variants
            // ----------------------------------------------------------------
            Join<Product, ProductVariant> variantJoin = root.join("productVariants", JoinType.LEFT);

            // ----------------------------------------------------------------
            // JOIN — Brand & Category (for slug filters)
            // ----------------------------------------------------------------
            Join<Product, Brand> brandJoin = root.join("brand", JoinType.LEFT);
            Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);

            // ----------------------------------------------------------------
            // DISTINCT — avoid duplicate products from multi-variant joins
            // ----------------------------------------------------------------
            if (query != null) {
                query.distinct(true);
            }

            // ----------------------------------------------------------------
            // 1. Full-text search — title, description, category name
            // ----------------------------------------------------------------
            if (filter.getSearch() != null && !filter.getSearch().isBlank()) {
                String pattern = "%" + filter.getSearch().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern),
                        cb.like(cb.lower(categoryJoin.get("name")), pattern)
                ));
            }

            // ----------------------------------------------------------------
            // 2. Category slug
            // ----------------------------------------------------------------
            if (filter.getCategorySlug() != null && !filter.getCategorySlug().isBlank()) {
                predicates.add(cb.equal(categoryJoin.get("slug"), filter.getCategorySlug()));
            }

            // ----------------------------------------------------------------
            // 3. Brand slugs (multi-select OR)
            // ----------------------------------------------------------------
            if (filter.getBrandSlugs() != null && !filter.getBrandSlugs().isEmpty()) {
                predicates.add(brandJoin.get("seoSlug").in(filter.getBrandSlugs()));
            }

            // ----------------------------------------------------------------
            // 4. Price range (from variant sellingPrice)
            // ----------------------------------------------------------------
            if (filter.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(variantJoin.get("sellingPrice"), filter.getMinPrice()));
            }
            if (filter.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(variantJoin.get("sellingPrice"), filter.getMaxPrice()));
            }

            // ----------------------------------------------------------------
            // 5. Item conditions (multi-select OR)
            // ----------------------------------------------------------------
            if (filter.getItemConditions() != null && !filter.getItemConditions().isEmpty()) {
                List<ItemCondition> conditions = filter.getItemConditions().stream()
                        .map(c -> ItemCondition.valueOf(c.toUpperCase()))
                        .toList();
                predicates.add(root.get("itemCondition").in(conditions));
            }

            // ----------------------------------------------------------------
            // 6. Minimum average rating
            // ----------------------------------------------------------------
            if (filter.getMinRating() != null && filter.getMinRating() > 0) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("averageRating"), filter.getMinRating()));
            }

            // ----------------------------------------------------------------
            // 7. In-stock only (variant stockQuantity > 0)
            // ----------------------------------------------------------------
            if (filter.isInStockOnly()) {
                predicates.add(cb.greaterThan(variantJoin.get("stockQuantity"), 0));
            }

            // ----------------------------------------------------------------
            // 8. Minimum discount percentage (from variant)
            // ----------------------------------------------------------------
            if (filter.getMinDiscount() != null && filter.getMinDiscount() > 0) {
                predicates.add(cb.greaterThanOrEqualTo(variantJoin.get("discountPercentage"), filter.getMinDiscount()));
            }

            // ----------------------------------------------------------------
            // 9. Dynamic specifications — product-level specs (JSONB)
            //    Also check variant-level specs
            //    Strategy: check if spec key/value appears in product or variant specs
            // ----------------------------------------------------------------
            if (filter.getSpecifications() != null && !filter.getSpecifications().isEmpty()) {
                for (Map.Entry<String, String> entry : filter.getSpecifications().entrySet()) {
                    String specValue = entry.getValue().toLowerCase();

                    // Match in product-level specifications (JSONB stored as text)
                    // We use native JSONB operator via function — cast to text and LIKE
                    Predicate productSpecMatch = cb.like(
                            cb.lower(cb.function("jsonb_extract_path_text",
                                    String.class,
                                    root.get("specifications"),
                                    cb.literal(entry.getKey()))),
                            "%" + specValue + "%"
                    );

                    // Match in variant-level specifications (JSONB)
                    Predicate variantSpecMatch = cb.like(
                            cb.lower(cb.function("jsonb_extract_path_text",
                                    String.class,
                                    variantJoin.get("specifications"),
                                    cb.literal(entry.getKey()))),
                            "%" + specValue + "%"
                    );

                    predicates.add(cb.or(productSpecMatch, variantSpecMatch));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

