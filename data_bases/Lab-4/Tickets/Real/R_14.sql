SELECT p_c."name"
FROM production.product_category AS p_c
WHERE p_c.product_category_id IN (
    SELECT p_sc.product_category_id
    FROM production.product_subcategory AS p_sc
    WHERE p_sc.product_subcategory_id IN (
        SELECT p_p.product_subcategory_id
        FROM production.product AS p_p
        WHERE p_p.color = 'Red'
        GROUP BY p_p.product_subcategory_id
        HAVING COUNT(*) > (
            SELECT COUNT(*)
            FROM production.product AS p_p2
            WHERE p_p2.color = 'Black'
              AND p_p2.product_subcategory_id = p_p.product_subcategory_id
        )
    )
)