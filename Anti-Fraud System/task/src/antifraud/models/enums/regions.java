package antifraud.models.enums;

public enum regions {
    EAP("East Asia and Pacific"),
    ECA("Europe and Central Asia"),
    LAC("Latin America and the Caribbean"),
    HIC("High-Income Countries"),
    MENA("The Middle East and North Africa"),
    SA("South Asia"),
    SSA("Sub-Saharan Africa");

    public final String description;

    regions(String description) {
        this.description = description;
    }

}
