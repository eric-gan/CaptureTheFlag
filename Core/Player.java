package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Player implements Serializable {
    protected int px;
    protected int py;
    protected int number;
    protected int startpx;
    protected int startpy;
    protected int lives;
    protected boolean flag;

    public Player(int xposition, int yposition, int number) {
        this.px = xposition;
        this.py = yposition;
        this.number = number;
        this.startpx = xposition;
        this.startpy = yposition;
        this.lives = 3;
        this.flag = false;
    }

    private static void movePlayer(Player p, TETile[][] world) {
        TETile temp = world[p.px][p.py];
        world[p.px][p.py] = Tileset.FLOOR;
        p.px = p.startpx;
        p.py = p.startpy;
        world[p.px][p.py] = temp;
    }

    private static void gameOver(Player p, Game g) {
        if (p.lives <= 0) {
            g.gameOver = true;
        }
    }

    public TETile[][] movePlayer1(String typed, TETile[][] world, Game g, Flag f) {
        if (typed.equals("W") || typed.equals("w")) {
            gameOver((world[px][py + 1]), g);
            hitEnemy((world[px][py + 1]), g, f);
            hitPlayer((world[px][py + 1]), g, f);
            hitFlag(g.currWorld, px, py + 1);
            if (py + 1 < world[0].length && world[px][py + 1].equals(Tileset.FLOOR)) {
                world[px][py] = Tileset.FLOOR;
                world[px][py + 1] = Tileset.PLAYER1;
                py = py + 1;
            }
        }
        if (typed.equals("A") || typed.equals("a")) {
            gameOver((world[px - 1][py]), g);
            hitEnemy((world[px - 1][py]), g, f);
            hitPlayer((world[px - 1][py]), g, f);
            hitFlag(g.currWorld, px - 1, py);
            if (px - 1 > 0 && world[px - 1][py].equals(Tileset.FLOOR)) {
                world[px][py] = Tileset.FLOOR;
                world[px - 1][py] = Tileset.PLAYER1;
                px = px - 1;
            }
        }
        if (typed.equals("S") || typed.equals("s")) {
            gameOver((world[px][py - 1]), g);
            hitEnemy((world[px][py - 1]), g, f);
            hitPlayer((world[px][py - 1]), g, f);
            hitFlag(g.currWorld, px, py - 1);
            if (py - 1 > 0 && world[px][py - 1].equals(Tileset.FLOOR)) {
                world[px][py] = Tileset.FLOOR;
                world[px][py - 1] = Tileset.PLAYER1;
                py = py - 1;
            }
        }
        if (typed.equals("D") || typed.equals("d")) {
            gameOver((world[px + 1][py]), g);
            hitEnemy((world[px + 1][py]), g, f);
            hitPlayer((world[px + 1][py]), g, f);
            hitFlag(g.currWorld, px + 1, py);
            if (px + 1 < world.length && world[px + 1][py].equals(Tileset.FLOOR)) {
                world[px][py] = Tileset.FLOOR;
                world[px + 1][py] = Tileset.PLAYER1;
                px = px + 1;
            }
        }
        return world;
    }

    public TETile[][] movePlayer2(String typed, TETile[][] world, Game g, Flag f) {
        if (typed.equals("I") || typed.equals("i")) {
            gameOver((world[px][py + 1]), g);
            hitEnemy((world[px][py + 1]), g, f);
            hitPlayer((world[px][py + 1]), g, f);
            hitFlag(g.currWorld, px, py + 1);
            if (py + 1 < world[0].length && world[px][py + 1].equals(Tileset.FLOOR)) {
                world[px][py] = Tileset.FLOOR;
                world[px][py + 1] = Tileset.PLAYER2;
                py = py + 1;
            }
        }
        if (typed.equals("J") || typed.equals("j")) {
            gameOver((world[px - 1][py]), g);
            hitEnemy((world[px - 1][py]), g, f);
            hitPlayer((world[px - 1][py]), g, f);
            hitFlag(g.currWorld, px - 1, py);
            if (px - 1 > 0 && world[px - 1][py].equals(Tileset.FLOOR)) {
                world[px][py] = Tileset.FLOOR;
                world[px - 1][py] = Tileset.PLAYER2;
                px = px - 1;
            }
        }
        if (typed.equals("K") || typed.equals("k")) {
            gameOver((world[px][py - 1]), g);
            hitEnemy((world[px][py - 1]), g, f);
            hitPlayer((world[px][py - 1]), g, f);
            hitFlag(g.currWorld, px, py - 1);
            if (py - 1 > 0 && world[px][py - 1].equals(Tileset.FLOOR)) {
                world[px][py] = Tileset.FLOOR;
                world[px][py - 1] = Tileset.PLAYER2;
                py = py - 1;
            }
        }
        if (typed.equals("L") || typed.equals("l")) {
            gameOver((world[px + 1][py]), g);
            hitEnemy((world[px + 1][py]), g, f);
            hitPlayer((world[px + 1][py + 1]), g, f);
            hitFlag(g.currWorld, px + 1, py);
            if (px + 1 < world.length && world[px + 1][py].equals(Tileset.FLOOR)) {
                world[px][py] = Tileset.FLOOR;
                world[px + 1][py] = Tileset.PLAYER2;
                px = px + 1;
            }
        }
        return world;
    }

    public TETile[][] moveCPU(int direction, TETile[][] world, Game g, Flag f) {
        if (direction == 0) {
            hitPlayer(world[px][py + 1], g, f);
            if (py + 1 < world[0].length && world[px][py + 1].equals(Tileset.FLOOR)) {
                world[px][py] = Tileset.FLOOR;
                world[px][py + 1] = Tileset.ENEMY;
                py = py + 1;
            }
        }
        if (direction == 1) {
            hitPlayer(world[px - 1][py], g, f);
            if (px - 1 > 0 && world[px - 1][py].equals(Tileset.FLOOR)) {
                world[px][py] = Tileset.FLOOR;
                world[px - 1][py] = Tileset.ENEMY;
                px = px - 1;
            }
        }
        if (direction == 2) {
            hitPlayer(world[px][py - 1], g, f);
            if (py - 1 > 0 && world[px][py - 1].equals(Tileset.FLOOR)) {
                world[px][py] = Tileset.FLOOR;
                world[px][py - 1] = Tileset.ENEMY;
                py = py - 1;
            }
        }
        if (direction == 3) {
            hitPlayer(world[px + 1][py], g, f);
            if (px + 1 < world.length && world[px + 1][py].equals(Tileset.FLOOR)) {
                world[px][py] = Tileset.FLOOR;
                world[px + 1][py] = Tileset.ENEMY;
                px = px + 1;
            }
        }
        return world;
    }

    private void hitPlayer(TETile tile, Game g, Flag f) {
        if (tile.equals(Tileset.PLAYER1)) {
            Player p1 = g.players.get(1);
            if (this.number < 0) {
                p1.lives -= 1;
                gameOver(p1, g);
                movePlayer(p1, g.currWorld);
            }
            if (p1.flag) {
                p1.flag = false;
                movePlayer(p1, g.currWorld);
                f.reset(g);
            }
        }
        if (tile.equals(Tileset.PLAYER2)) {
            Player p2 = g.players.get(0);
            if (this.number < 0) {
                p2.lives -= 1;
                gameOver(p2, g);
                movePlayer(p2, g.currWorld);
            }
            if (p2.flag) {
                p2.flag = false;
                movePlayer(p2, g.currWorld);
                f.reset(g);
            }
        }

    }

    private void hitEnemy(TETile tile, Game g, Flag f) {
        if (tile.equals(Tileset.ENEMY)) {
            this.lives -= 1;
            gameOver(this, g);
            movePlayer(this, g.currWorld);
            if (this.flag) {
                this.flag = false;
                f.reset(g);
            }
        }
    }

    private void hitFlag(TETile[][] world, int x, int y) {
        if (world[x][y].equals(Tileset.FLAG)) {
            this.flag = true;
            world[x][y] = Tileset.FLAGPOLE;
        }
    }

    private void gameOver(TETile tile, Game g) {
        if (tile.equals(Tileset.P1BASE) && this.equals(g.players.get(1))) {
            if (this.flag) {
                g.players.get(0).lives = 0;
                gameOver(g.players.get(0), g);
            }
        }
        if (tile.equals(Tileset.P2BASE) && this.equals(g.players.get(0))) {
            if (this.flag) {
                g.players.get(1).lives = 0;
                gameOver(g.players.get(1), g);
            }
        }
    }

}
