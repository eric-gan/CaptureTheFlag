package byog.Core;

import byog.TileEngine.Tileset;

import java.io.Serializable;


public class Flag implements Serializable {
    protected int px;
    protected int py;

    public Flag(int px, int py) {
        this.px = px;
        this.py = py;
    }

    public void reset(Game g) {
        g.currWorld[px][py] = Tileset.FLAG;
    }


}
