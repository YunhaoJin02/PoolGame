package PoolGame.objects;

import PoolGame.strategy.BlackStrategy;
import PoolGame.strategy.PocketStrategy;
import PoolGame.strategy.BallStrategy;
import PoolGame.strategy.BlueStrategy;

/** Builds pool balls. */
public class PoolBallBuilder implements BallBuilder {
    // Required Parameters
    private String colour;
    private double xPosition;
    private double yPosition;
    private double xVelocity;
    private double yVelocity;
    private double mass;

    // Variable Parameters
    private boolean isCue = false;
    public PocketStrategy strategy;

    private int score = 0;

    @Override
    public void setColour(String colour) {
        this.colour = colour;
    };

    @Override
    public void setxPos(double xPosition) {
        this.xPosition = xPosition;
    };

    @Override
    public void setyPos(double yPosition) {
        this.yPosition = yPosition;
    };

    @Override
    public void setxVel(double xVelocity) {
        this.xVelocity = xVelocity;
    };

    @Override
    public void setyVel(double yVelocity) {
        this.yVelocity = yVelocity;
    };

    @Override
    public void setMass(double mass) {
        this.mass = mass;
    };

    /**
     * Builds the ball.
     * 
     * @return ball
     */
    public Ball build() {
        //score section
        switch (colour) {
            case "red" -> score = 1;
            case "yellow" -> score = 2;
            case "green" -> score = 3;
            case "brown" -> score = 4;
            case "blue" -> score = 5;
            case "purple" -> score = 6;
            case "black" -> score = 7;
            case "orange" -> score = 8;
        }

        //strategy section
        if (colour.equals("white")) {
            isCue = true;
            strategy = new BallStrategy();
        } else if (colour.equals("blue") || (colour.equals("green") || colour.equals("purple"))) {
            strategy = new BlueStrategy();
        } else if (colour.equals("brown")||colour.equals("black")) {
            strategy = new BlackStrategy();
        } else {
            strategy = new BallStrategy();
        }

        return new Ball(colour, xPosition, yPosition, xVelocity, yVelocity, mass, isCue, strategy,score);
    }
}
