package PoolGame.memento;

import PoolGame.GameManager;
import PoolGame.objects.Ball;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Memento {

    private List<Double> xPosList;
    private List<Double> yPosList;
    private int score;
    private String minutes, seconds;
    private List<Boolean> visibleList;

    public Memento(List<Double> xPosList, List<Double> yPosList, int score,String seconds,String minutes,List<Boolean> visibleList){
        this.xPosList = xPosList;
        this.yPosList = yPosList;
        this.score = score;
        this.minutes = minutes;
        this.seconds = seconds;
        this.visibleList = visibleList;
    }


    //Total score for the game

    /**
     * return the saved score of the ball
     * @return score of the game
     */
    public int getScore(){
        return this.score;
    }

    /**
     * list of balls x position
     * @return a list that contains the balls x position
     */
    public List<Double> getxPos(){
        return xPosList;
    }

    /**
     * list of balls y position
     * @return a list that contains the balls y position
     */
    public List<Double> getyPos(){
        return yPosList;
    }

    /**
     * the duration of the game
     * @return the duration of the game
     */
    public String getSeconds(){
        return this.seconds;
    }

    public String getMinutes(){
        return this.minutes;
    }

    public List<Boolean> getVisibleList() {
        return visibleList;
    }
}
