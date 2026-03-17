package labs.mta.laborator4;

import android.annotation.SuppressLint;
import android.health.connect.LocalTimeRangeFilter;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalTime;

public class Market implements Serializable {
    private String nume;
    private boolean nonStop;
    private int nrAngajati;
    private TipMarket tip;
    private float rating;
    private boolean areParcare;
    private boolean areLivrare;
    private String zona;
    private LocalTime localtime;

    public Market() {
    }

    public Market(String nume, boolean nonStop, int nrAngajati, TipMarket tip,
                  float rating, boolean areParcare, boolean areLivrare, String zona, LocalTime local_time) {
        this.nume = nume;
        this.nonStop = nonStop;
        this.nrAngajati = nrAngajati;
        this.tip = tip;
        this.rating = rating;
        this.areParcare = areParcare;
        this.areLivrare = areLivrare;
        this.zona = zona;
        this.localtime = local_time;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public boolean isNonStop() {
        return nonStop;
    }

    public void setNonStop(boolean nonStop) {
        this.nonStop = nonStop;
    }

    public int getNrAngajati() {
        return nrAngajati;
    }

    public void setNrAngajati(int nrAngajati) {
        this.nrAngajati = nrAngajati;
    }

    public TipMarket getTip() {
        return tip;
    }

    public void setTip(TipMarket tip) {
        this.tip = tip;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isAreParcare() {
        return areParcare;
    }

    public void setAreParcare(boolean areParcare) {
        this.areParcare = areParcare;
    }

    public boolean isAreLivrare() {
        return areLivrare;
    }

    public void setAreLivrare(boolean areLivrare) {
        this.areLivrare = areLivrare;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(
                "\n%-12s %s" +
                        "\n%-12s %s" +
                        "\n%-12s %s" +
                        "\n%-12s %s" +
                        "\n%-12s %s" +
                        "\n%-12s %s" +
                        "\n%-12s %s" +
                        "\n%-12s %s" +
                        "\n%-12s %s\n",
                "nume=", nume,
                "nonStop=", nonStop,
                "nrAngajati=", nrAngajati,
                "tip=", tip,
                "rating=", rating,
                "areParcare=", areParcare,
                "areLivrare=", areLivrare,
                "zona=", zona,
                "oraInchidere=", String.format("%02d:%02d", localtime.getHour(), localtime.getMinute())
        );
    }
}
