package hr.fer.projekt.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.util.List;
import java.util.Random;

public class Obstacle extends Entity implements Comparable<Obstacle> {

    private final Color COLOR;
    private final String ID;

    public Obstacle(double x, double y, double width, double height, Color color, String ID) {
        super(x, y, width, height);
        this.COLOR = color;
        this.ID = ID;
    }

    public Color getCOLOR() {
        return COLOR;
    }

    public String getID() {
        return ID;
    }

    public static void moveObstacles(double length, List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles){
            obstacle.setX(obstacle.getX() - length);
        }
    }

    public static Obstacle randomObstacle(String ID, Random rand){

        int randomNum = rand.nextInt(4);

        switch (randomNum){
            case 0: return new Obstacle(700, 125, 100, 175, Color.ALICEBLUE, ID); // iceberg
            case 1: return new Obstacle(700, 135, 200, 100, Color.WHITE, ID); // boat
            case 2: return new Obstacle(700, 175, 125, 300, Color.SANDYBROWN, ID); // island
            case 3: return new Obstacle(700, 0, 100, 250, Color.SLATEGRAY, ID); // stalactite
            default: throw new IllegalArgumentException("Exception in class Obstacle line 44");
        }
    }

    public Rectangle toRectangle() {
        Rectangle rectangle = new Rectangle(getWidth(), getHeight());
        rectangle.setArcHeight(5.0);
        rectangle.setArcWidth(5.0);
        rectangle.setLayoutX(getX());
        rectangle.setLayoutY(getY());
        rectangle.setId(this.getID());
        rectangle.setFill(getCOLOR());
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStroke(Color.BLACK);
        return rectangle;
    }

    @Override
    public int compareTo(Obstacle o) {
        return this.getID().compareTo(o.getID());
    }
}
