package hr.fer.projekt.application;

import hr.fer.projekt.entities.Coins;
import hr.fer.projekt.entities.Obstacle;
import hr.fer.projekt.entities.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private List<Obstacle> obstacles;
    private List<Coins> coins;
    private int borderLeft = 0;
    private int borderRight = 600;
    public Random rand;

    private Player player;

    private int objectCounter = 1;
    private int coinCounter = 1;

    public Player getPlayer(){
        return player;
    }

    public List<Obstacle> getObstacles(){
        return obstacles;
    }

    public List<Coins> getCoins() {
        return coins;
    }

    public World(Random rand) {
        this.rand = rand;

        player = new Player(73, 178, 57, 44);

        obstacles = new ArrayList<>();
        obstacles.add(Obstacle.randomObstacle(String.valueOf(objectCounter), rand));

        coins = new ArrayList<>();
        coins.add((Coins.makeCoin(String.valueOf(objectCounter), rand, obstacles.getLast().getX() + obstacles.getLast().getWidth())));
    }

    public void generateObstacle() {
        objectCounter++;
        obstacles.add(Obstacle.randomObstacle(String.valueOf(objectCounter), rand));
    }

    public void generateCoins() {
        coinCounter++;
        coins.add(Coins.makeCoin(String.valueOf(coinCounter), rand, obstacles.getLast().getX() + obstacles.getLast().getWidth()));
    }

    public int getBorderLeft() {
        return borderLeft;
    }

    public int getBorderRight() {
        return borderRight;
    }
}
