package hr.fer.projekt.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

import java.util.List;
import java.util.Random;

public class Coins {

    private double points;
    private double r;
    private double x;
    private double y;
    private Circle circle;

    private final String ID;

    public Coins(double points, double r, double x, double y, String ID) {
        this.points = points;
        this.r = r;
        this.x = x;
        this.y = y;
        this.ID = ID;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public double getPoints() {
        return points;
    }
    public void setPoints(double points) {
        this.points = points;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getID() {
        return ID;
    }

    public Circle toCircle() {
        Circle circle = new Circle();
        circle.setLayoutX(getX());
        circle.setLayoutY(getY());
        circle.setRadius(getR());
        circle.setId(this.getID());
        circle.setFill(Color.rgb(255, 215, 0));
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setStroke(Color.BLACK);
        return circle;
    }

    public static void moveCoins(double length, List<Coins> coins) {
        for (Coins coin : coins){
            coin.setX(coin.getX() - length);
        }
    }
    public static Coins makeCoin(String ID, Random random, double x) {
        int randomNum = random.nextInt(1, 3);
        return new Coins(500, 12, x + 225, randomNum * 100, ID);
    }
}
