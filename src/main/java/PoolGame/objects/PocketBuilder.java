package PoolGame.objects;

public interface PocketBuilder {
    void setxPos(double xPositoin);

    void setyPos(double yPosition);

    void setRadius(double radius);

    Pocket build();
}
