package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game implements Serializable {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final long serialVersionUID = 71199185422L;
    private static final char[] PLAYER1MOVE = "wasdWASD".toCharArray();
    private static final char[] PLAYER2MOVE = "ijklIJKL".toCharArray();
    private static final char[] NUMBERS = "0123456789".toCharArray();
    protected TETile[][] currWorld = null;
    protected List<Player> players;
    protected Flag flag = null;
    protected boolean gameOver = false;
    TERenderer ter = new TERenderer();
    private long seed;
    private Random RANDOM;
    private Character prevInput = ' ';
    private Character currInput = ' ';
    private String prevMouseInput = "";
    private boolean flip = true;

    private static int calcDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        char input = ' ';
        drawMainMenu();
        while (input == ' ') {
            if (StdDraw.hasNextKeyTyped()) {
                input = StdDraw.nextKeyTyped();
            }
        }
        if (input == 'N' || input == 'n') {
            String userSeed = drawSeed();
            seed = Long.parseLong(userSeed.replaceAll("[^0-9]", ""));
            RANDOM = new Random(seed);
            startGame();

        } else if (input == 'L' || input == 'l') {
            Game g = Load.loadWorld();
            reload(g);
            startGame();
        } else if (input == 'Q' || input == 'q') {
            System.exit(0);

        } else {
            throw new RuntimeException("Incorrect input: Input should start with N, L, or Q");
        }

    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {

        //extracts user input to determine which option to run
        if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
            //extracts integer from input and assigns it to seed.
            seed = Long.parseLong(input.replaceAll("[^0-9]", ""));
            RANDOM = new Random(seed);
        } else if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
            Game g = Load.loadWorld();
            reload(g);

        } else if (input.charAt(0) == 'Q' || input.charAt(0) == 'q') {
            save(this);

        } else {
            throw new RuntimeException("Incorrect input: Input should start with N, L, or Q");
        }

        startStringGame(input);
        //finalizes the world and returns
        return currWorld;
    }

    private void startStringGame(String input) {
        if (currWorld == null) {
            currWorld = createWorld();
        }
        for (int i = 1; i < input.length(); i += 1) {
            if (inReference(input.charAt(i), NUMBERS)) {
                continue;
            }
            Character typed = input.charAt(i);
            if (inReference(typed, PLAYER1MOVE)) {
                renderPlayerMove(players.get(1), Character.toString(typed));
            }
            if (inReference(typed, PLAYER2MOVE)) {
                renderPlayerMove(players.get(0), Character.toString(typed));
            }
            if (typed.equals(':')) {
                save(this);
                break;
            }

            //alternates CPU movement for increased player movement and control.
            if (flip) {
                for (int j = 2; j < players.size(); j += 2) {
                    int direction = RANDOM.nextInt(4);
                    currWorld = players.get(j).moveCPU(direction, currWorld, this, flag);
                }
                flip = false;
            } else {
                for (int k = 3; k < players.size(); k += 2) {
                    int direction = RANDOM.nextInt(4);
                    currWorld = players.get(k).moveCPU(direction, currWorld, this, flag);
                }
                flip = true;
            }
        }
    }

    private void startGame() {
        if (currWorld == null) {
            ter.initialize(WIDTH, HEIGHT + 5);
            currWorld = createWorld();
        }
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        //draws the world to the screen
        ter.initialize(WIDTH, HEIGHT + 5);
        ter.renderFrame(currWorld);

        boolean type = false;
        while (!gameOver) {
            String currMouseInput = mousePointer(currWorld);
            if (!currMouseInput.equals(prevMouseInput)) {
                drawHud(currMouseInput);
                prevMouseInput = currMouseInput;
            }

            if (StdDraw.hasNextKeyTyped()) {
                prevInput = currInput;
                currInput = StdDraw.nextKeyTyped();
                if (inReference(currInput, PLAYER1MOVE)) {
                    renderPlayerMove(players.get(1), Character.toString(currInput));
                    ter.renderFrame(currWorld);
                }
                if (inReference(currInput, PLAYER2MOVE)) {
                    renderPlayerMove(players.get(0), Character.toString(currInput));
                    ter.renderFrame(currWorld);
                }
                if (prevInput.equals(':') && (currInput.equals('Q') || currInput.equals('q'))) {
                    save(this);
                    System.exit(0);
                    break;
                }
                type = true;
            }

            //alternates CPU movement for increased player movement and control.
            if (type) {
                if (flip) {
                    for (int i = 2; i < players.size(); i += 2) {
                        int direction = RANDOM.nextInt(4);
                        currWorld = players.get(i).moveCPU(direction, currWorld, this, flag);
                        drawHud(currMouseInput);
                        ter.renderFrame(currWorld);
                    }
                    flip = false;
                } else {
                    for (int i = 3; i < players.size(); i += 2) {
                        int direction = RANDOM.nextInt(4);
                        currWorld = players.get(i).moveCPU(direction, currWorld, this, flag);
                        drawHud(currMouseInput);
                        ter.renderFrame(currWorld);
                    }
                    flip = true;
                }
            }
            type = false;
            drawHud(currMouseInput);
            ter.renderFrame(currWorld);
        }
        renderGameOver();
        StdDraw.pause(5000);
        System.exit(0);
    }

    private void renderGameOver() {
        int length = 48;
        int width = 40;

        ter.initialize(length, width);
        StdDraw.clear(Color.BLACK);

        Font title = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(title);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(length / 2, 6 * (width / 8), "Game Over");

        Font subtitle = new Font("Monaco", Font.PLAIN, 26);
        StdDraw.setFont(subtitle);
        if (players.get(0).lives == 0) {
            StdDraw.text(length / 2, 5 * (width / 8), "Player 1 Wins!");
        } else {
            StdDraw.text(length / 2, 5 * (width / 8), "Player 2 Wins!");
        }
        StdDraw.show();

    }

    private void renderPlayerMove(Player p, String typed) {
        if (p.number == 1) {
            currWorld = p.movePlayer1(typed, currWorld, this, flag);
        }
        if (p.number == 2) {
            currWorld = p.movePlayer2(typed, currWorld, this, flag);
        }
    }

    private void drawHud(String newMouseInput) {
        Font info = new Font("Arial", Font.BOLD, 15);
        StdDraw.setFont(info);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(3, HEIGHT + 3, newMouseInput);
        String p1Life = Integer.toString(players.get(1).lives);
        String p2Life = Integer.toString(players.get(0).lives);
        StdDraw.text(10, HEIGHT + 3, "P1 lives: " + p1Life);
        StdDraw.text(20, HEIGHT + 3, "P2 lives: " + p2Life);
        for (int i = 0; i < 2; i += 1) {
            Player p = players.get(i);
            if (p.flag && i == 0) {
                StdDraw.text(30, HEIGHT + 3, "Player 2 has the flag!");
            }
            if (p.flag && i == 1) {
                StdDraw.text(30, HEIGHT + 3, "Player 1 has the flag!");
            }
        }
        StdDraw.show();

    }

    private TETile[][] createWorld() {
        players = new ArrayList<>();
        //initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        Build b = new Build(seed);
        b.drawRandomRooms(world);
        b.drawHall(world);
        b.surroundFloorWithWall(world);
        b.checkEdges(world);

        int playerset = 2;

        //Randomly places players in the center of a random room
        List<Room> allRooms = b.getAllRooms();
        while (playerset > 0) {
            Room room = allRooms.get(RANDOM.nextInt(allRooms.size()));
            Player user = new Player(room.getCenterX(), room.getCenterY(), playerset);
            if (world[user.px][user.py] == Tileset.FLOOR) {
                players.add(user);
                if (playerset == 2) {
                    world[user.px][user.py] = Tileset.PLAYER2;
                    world[user.px - 1][user.py] = Tileset.P2BASE;
                } else {
                    world[user.px][user.py] = Tileset.PLAYER1;
                    world[user.px - 1][user.py] = Tileset.P1BASE;
                }
                playerset -= 1;
            }
        }
        //adds CPU
        int numberOfNPC = 16 + RANDOM.nextInt(5);
        while (numberOfNPC > 0) {
            int width = RANDOM.nextInt(WIDTH);
            int height = RANDOM.nextInt(HEIGHT);
            if (world[width][height] == Tileset.FLOOR) {
                Player p = new Player(width, height, -1);
                players.add(p);
                world[p.px][p.py] = Tileset.ENEMY;
                numberOfNPC -= 1;
            }
        }

        //add Flag
        Position p = placeFlag(b);
        flag = new Flag(p.getPx(), p.getPy());
        world[flag.px][flag.py] = Tileset.FLAG;

        //finalizes the world and returns
        currWorld = world;
        return currWorld;
    }

    private void drawMainMenu() {
        int length = 48;
        int width = 40;

        ter.initialize(length, width);
        StdDraw.clear(Color.BLACK);

        Font title = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(title);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(length / 2, 7 * (width / 8), "CAPTURE THE FLAG");

        Font subtitle = new Font("Monaco", Font.PLAIN, 26);
        StdDraw.setFont(subtitle);
        StdDraw.text(length / 2, 5 * (width / 8), "New Game (N)");
        StdDraw.text(length / 2, 4 * (width / 8), "Load Game (L)");
        StdDraw.text(length / 2, 3 * (width / 8), "Quit (Q)");

        StdDraw.show();
    }

    private String drawSeed() {
        int length = 48;
        int width = 40;
        String seeds = "Seed: ";
        drawSeedHelper(seeds);

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char temp = StdDraw.nextKeyTyped();
                if (inReference(temp, NUMBERS)) {
                    seeds += Character.toString(temp);
                    drawSeedHelper(seeds);
                } else if (temp == 'S' || temp == 's') {
                    if (seeds.length() <= 6) {
                        StdDraw.text(length / 2, 3 * (width / 8), "Please enter a seed");
                        StdDraw.show();
                        StdDraw.pause(750);
                        drawSeedHelper(seeds);
                    } else {
                        return seeds;
                    }
                } else {
                    StdDraw.text(length / 2, 3 * (width / 8), "Seed can only contain integers");
                    StdDraw.show();
                    StdDraw.pause(750);
                    drawSeedHelper(seeds);
                }
            }
        }
    }

    private String mousePointer(TETile[][] world) {
        try {
            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();
            String tile = world[x][y].description();
            return tile;
        } catch (ArrayIndexOutOfBoundsException e) {
            return "HUD";
        }
    }

    private void drawSeedHelper(String seeds) {
        int length = 48;
        int width = 40;
        StdDraw.clear(Color.BLACK);
        Font title = new Font("Monaco", Font.BOLD, 28);
        StdDraw.setFont(title);
        StdDraw.text(length / 2, 6 * (width / 8), "Please enter a a random seed followed by S:");
        Font subtitle = new Font("Monaco", Font.PLAIN, 26);
        StdDraw.setFont(subtitle);
        StdDraw.text(length / 2, 4 * (width / 8), seeds);
        String move = "Player 1 Controls: WASD, Player 2 Controls: IJKL";
        StdDraw.text(length / 2, 1 * (width / 4), move);
        StdDraw.show();
    }

    private boolean inReference(char c, char[] ref) {
        for (char x : ref) {
            if (x == c) {
                return true;
            }
        }
        return false;
    }

    private Position placeFlag(Build b) {
        Player p1 = players.get(0);
        Player p2 = players.get(1);
        int maxX = 0;
        int maxY = 0;
        int maxDistance = 0;
        for (int i = 0; i < b.getAllRooms().size(); i += 1) {
            Room r = b.getAllRooms().get(i);
            int dp1 = calcDistance(p1.px, p1.py, r.getCenterX(), r.getCenterY());
            int dp2 = calcDistance(p2.px, p2.py, r.getCenterX(), r.getCenterY());
            int d = dp1 * dp2;
            if (d > maxDistance) {
                maxDistance = d;
                maxX = r.getCenterX();
                maxY = r.getCenterY();
            }

        }
        return new Position(maxX, maxY);
    }

    private void reload(Game g) {
        currWorld = g.currWorld;
        flag = g.flag;
        players = g.players;
        seed = g.seed;
        RANDOM = g.RANDOM;
        gameOver = g.gameOver;
        prevInput = g.prevInput;
        currInput = g.currInput;
        prevMouseInput = g.prevMouseInput;
        flip = g.flip;
        ter = g.ter;
    }

    private void save(Game g) {
        Load.saveWorld(this);
    }

}
