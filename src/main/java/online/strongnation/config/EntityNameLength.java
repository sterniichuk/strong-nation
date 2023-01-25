package online.strongnation.config;

public enum EntityNameLength {
    COUNTRY(255),
    REGION(255);

    public final int length;

    EntityNameLength(int length) {
        this.length = length;
    }
}
