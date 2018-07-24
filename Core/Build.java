package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Build implements Serializable {
    private Random RANDOM;
    private List<Room> allRooms;

    public Build() {

    }

    public Build(long seed) {
        RANDOM = new Random(seed);
        allRooms = new ArrayList<>();
    }

    private static void checkSur(TETile[][] tiles, TETile newTile, TETile ref, int px, int py) {
        checkHoriz(tiles, newTile, ref, px, py);
        checkVert(tiles, newTile, ref, px, py);
        checkDiag(tiles, newTile, ref, px, py);
    }

    private static void checkHoriz(TETile[][] tiles, TETile newTile, TETile ref, int px, int py) {
        if (tiles[px - 1][py].equals(ref)) {
            tiles[px - 1][py] = newTile;
        }
        if (tiles[px + 1][py].equals(ref)) {
            tiles[px + 1][py] = newTile;
        }
    }

    private static void checkVert(TETile[][] tiles, TETile newTile, TETile ref, int px, int py) {
        if (tiles[px][py - 1].equals(ref)) {
            tiles[px][py - 1] = newTile;
        }
        if (tiles[px][py + 1].equals(ref)) {
            tiles[px][py + 1] = newTile;
        }
    }

    private static void checkDiag(TETile[][] tiles, TETile newTile, TETile ref, int px, int py) {
        if (tiles[px - 1][py - 1].equals(ref)) {
            tiles[px - 1][py - 1] = newTile;
        }
        if (tiles[px - 1][py + 1].equals(ref)) {
            tiles[px - 1][py + 1] = newTile;
        }
        if (tiles[px + 1][py - 1].equals(ref)) {
            tiles[px + 1][py - 1] = newTile;
        }
        if (tiles[px + 1][py + 1].equals(ref)) {
            tiles[px + 1][py + 1] = newTile;
        }
    }

    public void surroundFloorWithWall(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 1; x < width - 1; x += 1) {
            for (int y = 1; y < height - 1; y += 1) {
                if (tiles[x][y].equals(Tileset.FLOOR)) {
                    checkSur(tiles, Tileset.WALL, Tileset.NOTHING, x, y);
                }
            }
        }

    }

    public void checkEdges(TETile[][] tiles) {
        for (int i = 0; i < tiles.length; i += 1) {
            if (tiles[i][0] == Tileset.FLOOR) {
                tiles[i][0] = Tileset.WALL;
            }
            if (tiles[i][tiles[0].length - 1] == Tileset.FLOOR) {
                tiles[i][tiles[0].length - 1] = Tileset.WALL;
            }
        }

        for (int i = 0; i < tiles[0].length; i += 1) {
            if (tiles[0][i] == Tileset.FLOOR) {
                tiles[0][i] = Tileset.WALL;
            }
            if (tiles[tiles.length - 1][i] == Tileset.FLOOR) {
                tiles[tiles.length - 1][i] = Tileset.WALL;
            }
        }
    }

    public List<Room> getAllRooms() {
        return allRooms;
    }

    private boolean generateRoom() {
        int tileNum = RANDOM.nextInt(69);
        switch (tileNum) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    private int generateLength() {
        int tileNum = RANDOM.nextInt(5);
        return 3 + tileNum;
    }

    private void drawRoom(TETile[][] tiles, TETile tile, Position p) {
        int height = generateLength();
        int width = generateLength();
        Room r = new Room(height, width, p);
        for (int i = 0; i < allRooms.size(); i += 1) {
            if (r.overlap(allRooms.get(i))) {
                return;
            }
        }
        allRooms.add(r);
        for (int x = p.getPx(); x < p.getPx() + width && x != tiles.length - 2; x += 1) {
            for (int y = p.getPy(); y < p.getPy() + height && y != tiles[0].length - 2; y += 1) {
                tiles[x][y] = tile;
            }
        }
    }

    public void drawRandomRooms(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 2; x < width - 3; x += 1) {
            for (int y = 2; y < height - 3; y += 1) {
                if (generateRoom() && tiles[x][y] == Tileset.NOTHING) {
                    drawRoom(tiles, Tileset.FLOOR, new Position(x, y));
                }
            }
        }
    }

    private int generateConnect() {
        int tileNum = RANDOM.nextInt(allRooms.size() / 2);
        return tileNum;
    }

    private int choosePath() {
        int tileNum = RANDOM.nextInt(2);
        return tileNum;
    }

    public void drawHall(TETile[][] tiles) {
        for (int i = 0; i < allRooms.size() - 1; i++) {
            Room currRoom = allRooms.get(i);
            Room connect = allRooms.get((i + generateConnect()) % (allRooms.size()));
            drawHallHelper(tiles, currRoom, allRooms.get(i + 1));
            drawHallHelper(tiles, currRoom, connect);

        }
    }

    private void drawHallHelper(TETile[][] tiles, Room currRoom, Room connect) {
        int currCenterX = currRoom.getCenterX();
        int currCenterY = currRoom.getCenterY();
        int connectCenterX = connect.getCenterX();
        int connectCenterY = connect.getCenterY();

        if (currCenterX >= connectCenterX && currCenterY >= connectCenterY) {
            while (currCenterX != connectCenterX && currCenterY != connectCenterY) {
                if (choosePath() == 1) {
                    currCenterX -= 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                } else {
                    currCenterY -= 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                }
            }
            if (currCenterX == connectCenterX) {
                while (currCenterY != connectCenterY) {
                    currCenterY -= 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                }
            } else {
                while (currCenterX != connectCenterX) {
                    currCenterX -= 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                }
            }
        }
        if (currCenterX <= connectCenterX && currCenterY <= connectCenterY) {
            while (currCenterX != connectCenterX && currCenterY != connectCenterY) {
                if (choosePath() == 1) {
                    currCenterX += 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                } else {
                    currCenterY += 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                }
            }
            if (currCenterX == connectCenterX) {
                while (currCenterY != connectCenterY) {
                    currCenterY += 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                }
            } else {
                while (currCenterX != connectCenterX) {
                    currCenterX += 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                }
            }
        } else {
            drawHallHelper2(tiles, currRoom, connect);
        }

    }

    private void drawHallHelper2(TETile[][] tiles, Room currRoom, Room connect) {
        int currCenterX = currRoom.getCenterX();
        int currCenterY = currRoom.getCenterY();
        int connectCenterX = connect.getCenterX();
        int connectCenterY = connect.getCenterY();
        if (currCenterX <= connectCenterX && currCenterY >= connectCenterY) {
            while (currCenterX != connectCenterX && currCenterY != connectCenterY) {
                if (choosePath() == 1) {
                    currCenterX += 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                } else {
                    currCenterY -= 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                }
            }
            if (currCenterX == connectCenterX) {
                while (currCenterY != connectCenterY) {
                    currCenterY -= 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                }
            } else {
                while (currCenterX != connectCenterX) {
                    currCenterX += 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                }
            }
        }

        if (currCenterX >= connectCenterX && currCenterY <= connectCenterY) {
            while (currCenterX != connectCenterX && currCenterY != connectCenterY) {
                if (choosePath() == 1) {
                    currCenterX -= 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                } else {
                    currCenterY += 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                }
            }
            if (currCenterX == connectCenterX) {
                while (currCenterY != connectCenterY) {
                    currCenterY += 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                }
            } else {
                while (currCenterX != connectCenterX) {
                    currCenterX -= 1;
                    tiles[currCenterX][currCenterY] = Tileset.FLOOR;
                }
            }
        }
    }
}
