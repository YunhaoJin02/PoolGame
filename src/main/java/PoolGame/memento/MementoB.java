package PoolGame.memento;

import java.util.List;

public class MementoB {

    private List<Double> xPosList;
    private List<Double> yPosList;
    private List<Boolean> visibleList;

    public MementoB(List<Double> xPosList, List<Double> yPosList,List<Boolean> visibleList){
        this.xPosList = xPosList;
        this.yPosList = yPosList;
        this.visibleList = visibleList;
    }
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

}
