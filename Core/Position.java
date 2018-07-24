package byog.Core;

import java.io.Serializable;

public class Position implements Serializable {
    private int px;
    private int py;

    public Position(int x, int y) {
        px = x;
        py = y;
    }

    public int getPx() {
        return px;
    }

    public int getPy() {
        return py;
    }
}
