explain SELECT
            p.id,
            p.brand_id,
            b.name,
            p.name,
            p.price,
            p.description,
            ps.like_count,
            p.created_at,
            p.updated_at
        FROM product p
                 inner join brand b
                            on p.brand_id = b.id
                 inner join stock s
                            on p.id = s.product_id
                 inner join product_status ps
                            on p.id = ps.product_id
        order by ps.like_count desc
        limit 0,10;
