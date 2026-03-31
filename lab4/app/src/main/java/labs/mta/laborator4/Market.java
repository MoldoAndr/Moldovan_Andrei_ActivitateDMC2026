package labs.mta.laborator4;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalTime;
import java.util.Locale;

public class Market implements Parcelable {
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

    protected Market(Parcel in) {
        nume = in.readString();
        nonStop = in.readByte() != 0;
        nrAngajati = in.readInt();
        String tipName = in.readString();
        tip = tipName == null ? null : TipMarket.valueOf(tipName);
        rating = in.readFloat();
        areParcare = in.readByte() != 0;
        areLivrare = in.readByte() != 0;
        zona = in.readString();
        int hour = in.readInt();
        int minute = in.readInt();
        localtime = LocalTime.of(hour, minute);
    }

    public static final Creator<Market> CREATOR = new Creator<>() {
        @Override
        public Market createFromParcel(Parcel in) {
            return new Market(in);
        }

        @Override
        public Market[] newArray(int size) {
            return new Market[size];
        }
    };

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

    public LocalTime getLocaltime() {
        return localtime;
    }

    public void setLocaltime(LocalTime localtime) {
        this.localtime = localtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nume);
        dest.writeByte((byte) (nonStop ? 1 : 0));
        dest.writeInt(nrAngajati);
        dest.writeString(tip == null ? null : tip.name());
        dest.writeFloat(rating);
        dest.writeByte((byte) (areParcare ? 1 : 0));
        dest.writeByte((byte) (areLivrare ? 1 : 0));
        dest.writeString(zona);
        dest.writeInt(localtime == null ? 0 : localtime.getHour());
        dest.writeInt(localtime == null ? 0 : localtime.getMinute());
    }

    @NonNull
    @Override
    public String toString() {
        String oraInchidere = localtime == null
                ? "--:--"
                : String.format(Locale.getDefault(), "%02d:%02d", localtime.getHour(), localtime.getMinute());

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
                "oraInchidere=", oraInchidere
        );
    }
}
