package labs.mta.laborator4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

public class MarketAdapter extends ArrayAdapter<Market> {

    public MarketAdapter(@NonNull Context context, @NonNull List<Market> markets) {
        super(context, 0, markets);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_market, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Market market = getItem(position);
        if (market != null) {
            viewHolder.tvNume.setText(market.getNume());
            viewHolder.tvDetaliiPrincipale.setText(String.format(
                    Locale.getDefault(),
                    "Tip: %s | Zona: %s | Rating: %.0f",
                    market.getTip(),
                    market.getZona(),
                    market.getRating()
            ));
            viewHolder.tvDetaliiSecundare.setText(String.format(
                    Locale.getDefault(),
                    "Angajati: %d | Non-stop: %s | Parcare: %s | Livrare: %s | Inchidere: %s",
                    market.getNrAngajati(),
                    market.isNonStop() ? "Da" : "Nu",
                    market.isAreParcare() ? "Da" : "Nu",
                    market.isAreLivrare() ? "Da" : "Nu",
                    formatTime(market.getLocaltime())
            ));
        }

        return convertView;
    }

    private String formatTime(@Nullable LocalTime time) {
        if (time == null) {
            return "--:--";
        }

        return String.format(Locale.getDefault(), "%02d:%02d", time.getHour(), time.getMinute());
    }

    private static class ViewHolder {
        private final TextView tvNume;
        private final TextView tvDetaliiPrincipale;
        private final TextView tvDetaliiSecundare;

        private ViewHolder(View view) {
            tvNume = view.findViewById(R.id.tvItemNume);
            tvDetaliiPrincipale = view.findViewById(R.id.tvItemDetaliiPrincipale);
            tvDetaliiSecundare = view.findViewById(R.id.tvItemDetaliiSecundare);
        }
    }
}
