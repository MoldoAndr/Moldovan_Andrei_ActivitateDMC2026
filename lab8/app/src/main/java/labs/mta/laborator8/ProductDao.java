package labs.mta.laborator8;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    long insert(Product product);

    @Query("SELECT * FROM products ORDER BY id ASC")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE name = :name LIMIT 1")
    Product getProductByName(String name);

    @Query("SELECT * FROM products WHERE quantity BETWEEN :minQuantity AND :maxQuantity ORDER BY quantity ASC, name ASC")
    List<Product> getProductsByQuantityBetween(int minQuantity, int maxQuantity);

    @Query("DELETE FROM products WHERE quantity > :value")
    int deleteProductsWithQuantityGreaterThan(int value);

    @Query("DELETE FROM products WHERE quantity < :value")
    int deleteProductsWithQuantityLessThan(int value);

    @Query("UPDATE products SET quantity = quantity + 1 WHERE LOWER(name) LIKE LOWER(:prefix) || '%'")
    int incrementQuantityForNamesStartingWith(String prefix);
}
