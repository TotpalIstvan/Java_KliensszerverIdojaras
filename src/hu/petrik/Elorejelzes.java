package hu.petrik;

public class Elorejelzes {
    private String szovegesElorejelzes;
    private int min, max;

    public Elorejelzes(String elo, String minmax) {
        szovegesElorejelzes = elo.trim();
        String[] adatok = minmax.split("/");
        min = Integer.parseInt(adatok[0].trim());
        max = Integer.parseInt(adatok[1].trim());
    }

    public String getSzovegesElorejelzes() {
        return szovegesElorejelzes;
    }

    public void setSzovegesElorejelzes(String szovegesElorejelzes) {
        this.szovegesElorejelzes = szovegesElorejelzes;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return String.format("%s, Minimum: %d, Maximum: %d", szovegesElorejelzes, min, max);
    }
}
