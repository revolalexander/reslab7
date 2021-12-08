package domainObjectModel;

import commandHandler.utils.Logger;

/**
 * Coordinates of the ticket
 * **/
public class Coordinates {
    private double x;
    private Long y;
    private final Long MAX_Y = 3L;
    private int id;

    public Coordinates(double x, Long y) {
        if (y == null || y > MAX_Y){
            Logger.error("y is null or more than 3 in Coordinates constructor");
            System.exit(0);
        }
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setY(Long y) {
        if (y == null || y > MAX_Y){
            Logger.error("y is null or more than 3 in Coordinates setter");
            System.exit(0);
        }
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "\nx=" + x +
                ", \ny=" + y +
                '}';
    }
}
