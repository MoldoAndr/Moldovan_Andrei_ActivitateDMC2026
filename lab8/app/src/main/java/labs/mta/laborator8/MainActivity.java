package labs.mta.laborator8;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProductDao productDao;
    private ProductAdapter productAdapter;

    private EditText nameEditText;
    private EditText categoryEditText;
    private EditText quantityEditText;
    private EditText searchNameEditText;
    private EditText minQuantityEditText;
    private EditText maxQuantityEditText;
    private EditText deleteThresholdEditText;
    private EditText initialLetterEditText;
    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        productDao = AppDatabase.getInstance(this).productDao();
        productAdapter = new ProductAdapter(this);

        bindViews();
        configureList();
        configureButtons();
        refreshAllProducts();

        View rootView = findViewById(R.id.main);
        int initialLeftPadding = rootView.getPaddingLeft();
        int initialTopPadding = rootView.getPaddingTop();
        int initialRightPadding = rootView.getPaddingRight();
        int initialBottomPadding = rootView.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    initialLeftPadding + systemBars.left,
                    initialTopPadding + systemBars.top,
                    initialRightPadding + systemBars.right,
                    initialBottomPadding + systemBars.bottom
            );
            return insets;
        });
    }

    private void bindViews() {
        nameEditText = findViewById(R.id.edit_text_name);
        categoryEditText = findViewById(R.id.edit_text_category);
        quantityEditText = findViewById(R.id.edit_text_quantity);
        searchNameEditText = findViewById(R.id.edit_text_search_name);
        minQuantityEditText = findViewById(R.id.edit_text_min_quantity);
        maxQuantityEditText = findViewById(R.id.edit_text_max_quantity);
        deleteThresholdEditText = findViewById(R.id.edit_text_delete_threshold);
        initialLetterEditText = findViewById(R.id.edit_text_initial_letter);
        statusTextView = findViewById(R.id.text_view_status);
    }

    private void configureList() {
        ListView productsListView = findViewById(R.id.list_view_products);
        productsListView.setAdapter(productAdapter);
    }

    private void configureButtons() {
        Button addButton = findViewById(R.id.button_add_product);
        Button showAllButton = findViewById(R.id.button_show_all);
        Button searchByNameButton = findViewById(R.id.button_search_by_name);
        Button filterByRangeButton = findViewById(R.id.button_filter_by_range);
        Button deleteGreaterButton = findViewById(R.id.button_delete_greater);
        Button deleteLessButton = findViewById(R.id.button_delete_less);
        Button incrementByLetterButton = findViewById(R.id.button_increment_by_letter);

        addButton.setOnClickListener(v -> insertProduct());
        showAllButton.setOnClickListener(v -> refreshAllProducts());
        searchByNameButton.setOnClickListener(v -> searchProductByName());
        filterByRangeButton.setOnClickListener(v -> filterProductsByQuantityRange());
        deleteGreaterButton.setOnClickListener(v -> deleteProductsWithQuantityGreaterThan());
        deleteLessButton.setOnClickListener(v -> deleteProductsWithQuantityLessThan());
        incrementByLetterButton.setOnClickListener(v -> incrementQuantityForNamesStartingWith());
    }

    private void insertProduct() {
        String name = readRequiredText(nameEditText);
        String category = readRequiredText(categoryEditText);
        Integer quantity = readRequiredInt(quantityEditText);

        if (name == null || category == null || quantity == null) {
            return;
        }

        productDao.insert(new Product(name, category, quantity));
        clearInsertFields();
        showToast(getString(R.string.product_added));
        refreshAllProducts();
    }

    private void refreshAllProducts() {
        showProducts(productDao.getAllProducts(), getString(R.string.status_all_products));
    }

    private void searchProductByName() {
        String searchName = readRequiredText(searchNameEditText);
        if (searchName == null) {
            return;
        }

        Product product = productDao.getProductByName(searchName);
        if (product == null) {
            showProducts(Collections.emptyList(), getString(R.string.status_no_product_found, searchName));
            showToast(getString(R.string.product_not_found));
            return;
        }

        showProducts(
                Collections.singletonList(product),
                getString(R.string.status_exact_search, searchName)
        );
    }

    private void filterProductsByQuantityRange() {
        Integer minQuantity = readRequiredInt(minQuantityEditText);
        Integer maxQuantity = readRequiredInt(maxQuantityEditText);

        if (minQuantity == null || maxQuantity == null) {
            return;
        }

        if (minQuantity > maxQuantity) {
            showToast(getString(R.string.invalid_range));
            return;
        }

        showProducts(
                productDao.getProductsByQuantityBetween(minQuantity, maxQuantity),
                getString(R.string.status_range_search, minQuantity, maxQuantity)
        );
    }

    private void deleteProductsWithQuantityGreaterThan() {
        Integer threshold = readRequiredInt(deleteThresholdEditText);
        if (threshold == null) {
            return;
        }

        int deletedRows = productDao.deleteProductsWithQuantityGreaterThan(threshold);
        showToast(getString(R.string.deleted_greater_result, deletedRows));
        refreshAllProducts();
    }

    private void deleteProductsWithQuantityLessThan() {
        Integer threshold = readRequiredInt(deleteThresholdEditText);
        if (threshold == null) {
            return;
        }

        int deletedRows = productDao.deleteProductsWithQuantityLessThan(threshold);
        showToast(getString(R.string.deleted_less_result, deletedRows));
        refreshAllProducts();
    }

    private void incrementQuantityForNamesStartingWith() {
        String prefix = readRequiredText(initialLetterEditText);
        if (prefix == null) {
            return;
        }

        int updatedRows = productDao.incrementQuantityForNamesStartingWith(prefix.substring(0, 1));
        showToast(getString(R.string.updated_result, updatedRows));
        refreshAllProducts();
    }

    private void showProducts(List<Product> products, String statusMessage) {
        productAdapter.updateProducts(products);
        statusTextView.setText(getString(R.string.status_format, statusMessage, products.size()));
    }

    private String readRequiredText(EditText editText) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty()) {
            editText.setError(getString(R.string.required_field));
            return null;
        }
        return value;
    }

    private Integer readRequiredInt(EditText editText) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty()) {
            editText.setError(getString(R.string.required_number));
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            editText.setError(getString(R.string.invalid_number));
            return null;
        }
    }

    private void clearInsertFields() {
        nameEditText.setText("");
        categoryEditText.setText("");
        quantityEditText.setText("");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
