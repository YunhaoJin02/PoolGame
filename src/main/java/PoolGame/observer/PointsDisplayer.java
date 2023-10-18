package PoolGame.observer;

import PoolGame.GameManager;
import javafx.scene.control.Label;
import PoolGame.objects.Ball;
import PoolGame.observer.Observer;

/**
 * A concrete observer.
 *
 * It would probably be neater to do all of this in a lambda, but for the sake
 * of demonstration this shows a way of doing it. Another way can be seen in
 * my code for Week 7, Q4.
 */
public class PointsDisplayer implements Observer {

    private GameManager gameManager;

    public PointsDisplayer(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    @Override
    public String observe() {
        // We get information from the subject, then use it
        // as we see fit

        return "Current Score: "+gameManager.getScore();
    }
}