package PoolGame.memento;

import PoolGame.GameManager;
import PoolGame.objects.Ball;

import java.util.ArrayList;
import java.util.List;

public class Originator {

    private GameManager gm;
    private List<Ball> balls = new ArrayList<>();


    public Originator(GameManager gm){
        this.gm = gm;
    }

    public List<Ball> getBalls(){
        balls = this.gm.getBalls();
        return this.balls;
    }

    public MementoB saveB(){
        //make deep copy
        List<Double> xPos = new ArrayList<>();
        List<Double> yPos = new ArrayList<>();
        List<Boolean> visibleList = new ArrayList<>();
        this.balls = getBalls();
        for (Ball ball:this.balls){
            xPos.add(ball.getxPos());
            yPos.add(ball.getyPos());
            visibleList.add(ball.isActive());
        }
        return new MementoB(xPos,yPos,visibleList);
    }

    public void undoB(MementoB memento){
        List<Double> savedxPos = memento.getxPos();
        List<Double> savedyPos = memento.getyPos();
        for (int i= 0;i < balls.size();i++){
            //respawn the ball when it is in the pocket
            if (!balls.get(i).isActive()){
                balls.get(i).setActive(true);
                balls.get(i).addLives();
            }
            balls.get(i).setxPos(savedxPos.get(i)-20);
            balls.get(i).setyPos(savedyPos.get(i)-20);
        }
    }

}
