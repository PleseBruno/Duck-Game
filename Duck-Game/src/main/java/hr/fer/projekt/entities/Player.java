package hr.fer.projekt.entities;

import java.util.List;

public class Player extends Entity {

    public Player(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    private final double SCALER = 10;

    private double moveX = 0;
    private double moveY = 0;
    private boolean jumped = false;
    private boolean dived = false;
    private final double GRAVITY = 1.2 / (SCALER * SCALER);
    private final double FRICTION = 1 / (SCALER * SCALER);
    private final double BOUYANCY = 0.9 / (SCALER * SCALER);

    public boolean isJumped() {
        return jumped;
    }

    public void setJumped(boolean jumped) {
        this.jumped = jumped;
    }

    public boolean isDived() {
        return dived;
    }

    public void setDived(boolean dived) {
        this.dived = dived;
    }

    public double getMoveX() {
        return moveX;
    }

    public void setMoveX(double moveX) {
        this.moveX = moveX;
    }

    public double getMoveY() {
        return moveY;
    }

    public void setMoveY(double moveY) {
        this.moveY = moveY;
    }

    public void jump() {
        setJumped(true);
        setMoveY(-17 / SCALER);
    }

    public void dive() {
        setDived(true);
        setMoveY(17 / SCALER);
    }

    public void moveRight() {
        moveX = 6 / SCALER;
        setMoveX(moveX);
    }

    public void moveLeft() {
        moveX = -6 / SCALER;
        setMoveX(moveX);
    }

    public void moveVertical(List<Obstacle> obstacles) {
        if (getMoveX() > 0) {
            if (getMoveX() - FRICTION < 0) {
                setMoveX(0);
            }
            else {
                setX(getX() + getMoveX());
                setMoveX(getMoveX() - FRICTION);
            }
        }
        if (getMoveX() < 0) {
            if (getMoveX() + FRICTION > 0) {
                setMoveX(0);
            }
            else {

                setX(getX() + getMoveX());
                setMoveX(getMoveX() + FRICTION);

            }
        }
        if (isJumped()) {
            if (getY() + getMoveY() > 178) {
                    setY(178);
                    setMoveY(0);
            }
            else {
                setY(getY() + getMoveY());
                setMoveY(getMoveY() + GRAVITY);
            }
        }
        if (isDived()) {
            if  (getY() + getMoveY() < 178) {
                setY(178);
                setMoveY(0);
            }
            else {
                setY(getY() + getMoveY());
                setMoveY(getMoveY() - BOUYANCY);
            }
        }

        //resetira isJumped i isDived

        if (getY() == 178 && (isJumped() || isDived())) {
            setMoveY(0);
            setJumped(false);
            setDived(false);
        }

        for (Obstacle obstacle : obstacles) {
            int touch = touching(obstacle);
            switch (touch) {

                case 1: //dolazi odozgo
                {

                    setMoveY(0);
                    setY(obstacle.getY() - getHeight());
                    break;
                }
                case 2: //dolazi odozdo
                {

                    setMoveY(0);
                    setY(obstacle.getY() + obstacle.getHeight());
                    break;

                }
                case 3: //dolazi slijeva
                {
                    setMoveX(0);
                    setX(obstacle.getX() - getWidth());
                    break;
                }
                case 4: //dolazi sdesna
                {
                    setMoveX(0);
                    setX(obstacle.getX() + obstacle.getWidth());
                    break;
                }
                case 0:{
                    break;
                }
                default:
                {
                    throw new IllegalArgumentException("Exception in class Player line 152");
                }
            }
        }
    }

    public int touching(Obstacle obstacle){
        if (getY()  < (obstacle.getY() + obstacle.getHeight())
        && (getY() + getHeight()) > obstacle.getY()
        && (getX() + getWidth()) >  obstacle.getX()
        && getX() < (obstacle.getX() + obstacle.getWidth())){
            if (getY() < obstacle.getY() && getMoveY() >= 0 && getY() + (5* getHeight())/6 < obstacle.getY()) return 1; //dolazi odozgo
            if ((getY() + getHeight()) > obstacle.getY() + obstacle.getHeight() && getMoveY() <= 0 && getY() +  getHeight()/6 > obstacle.getY())  return 2; //dolazi odozdo
            if (getX() <  obstacle.getX()) return 3; //slijeva
            if (getX() + getWidth() > obstacle.getX() + obstacle.getWidth()) return 4; //sdesna
        }
        return 0;
    }

    public int collectCoin(Coins coin) {
        double closestX = Math.max(getX(), Math.min(getX() + getWidth(), coin.getX()));
        double closestY = Math.max(getY(), Math.min(getY() + getHeight(), coin.getY()));

        double distanceX = Math.abs(coin.getX() - closestX);
        double distanceY = Math.abs(coin.getY() - closestY);

        if (distanceX*distanceX + distanceY*distanceY <= coin.getR()*coin.getR()) {
            return 1;
        }
        return 0;
    }

    public boolean isDead() {
        return getX() + getWidth() < 0;
    }
}