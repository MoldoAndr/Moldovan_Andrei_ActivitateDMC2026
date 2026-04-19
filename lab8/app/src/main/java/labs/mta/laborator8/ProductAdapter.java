package labs.mta.laborator8;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(@NonNull Context context) {
        super(context, android.R.layout.simple_list_item_2, new ArrayList<>());
    }

    public void updateProducts(List<Product> products) {
        clear();
        addAll(products);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Product product = getItem(position);
        TextView titleTextView = view.findViewById(android.R.id.text1);
        TextView detailsTextView = view.findViewById(android.R.id.text2);

        if (product != null) {
            titleTextView.setText(product.getName() + " - stoc: " + product.getQuantity());
            detailsTextView.setText("ID: " + product.getId() + " | Categorie: " + product.getCategory());
        }

        return view;
    }
}
