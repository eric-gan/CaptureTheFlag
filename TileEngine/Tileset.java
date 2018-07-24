package byog.TileEngine;

import java.awt.Color;
import java.io.Serializable;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 * <p>
 * Ex:
 * world[x][y] = Tileset.FLOOR;
 * <p>
 */

public class Tileset implements Serializable {
    public static final TETile PLAYER1 = new TETile('㋛', Color.black, Color.cyan, "player1");
    public static final TETile PLAYER2 = new TETile('㋡', Color.black, Color.yellow, "player2");
    public static final TETile P1BASE = new TETile('A', Color.cyan, Color.cyan, "player1base");
    public static final TETile P2BASE = new TETile('B', Color.yellow, Color.yellow, "player2base");
    public static final TETile ENEMY = new TETile('@', Color.black, Color.red, "enemy");
    public static final TETile FLAG = new TETile('⍟', Color.black, Color.magenta, "flag");
    public static final TETile FLAGPOLE = new TETile('p', Color.BLUE, Color.blue, "flagpole");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
}


