SELECT ps1.name, ps1.product_subcategory_id, ps2.product_subcategory_id
FROM production.product_subcategory as ps1 JOIN production.product_subcategory as ps2
                                                ON ps1.name = ps2.name