package PoolGame.objects;

public class PoolPocketBuilder implements PocketBuilder{

    private double xPosition;
    private double yPosition;
    private double radius;

    @Override
    public void setxPos(double xPositoin) {
        this.xPosition = xPositoin;
    }

    @Override
    public void setyPos(double yPosition) {
        this.yPosition = yPosition;
    }

    @Override
    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public Pocket build(){
        return new Pocket(this.xPosition,this.yPosition,this.radius);
    }
}
