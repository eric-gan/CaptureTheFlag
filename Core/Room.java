package byog.Core;


import java.io.Serializable;

public class Room implements Serializable {
    private int length;
    private int width;
    private Position p;
    private int centerX;
    private int centerY;

    public Room(int l, int w, Position p) {
        length = l;
        width = w;
        this.p = p;
        this.centerX = p.getPx() + w / 2;
        this.centerY = p.getPy() + l / 2;
    }

    public Position getPosition() {
        return p;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public boolean overlap(Room r) {
        int rx = r.getPosition().getPx();
        int ry = r.getPosition().getPy();
        if (rx - p.getPx() - width > 1 || p.getPx() - rx - r.getWidth() > 1) {
            return false;
        }
        if (ry - p.getPy() - length > 1 || p.getPy() - ry - r.getLength() > 1) {
            return false;
        }
        return true;
    }

}
