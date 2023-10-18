package PoolGame;

import PoolGame.memento.Caretaker;
import PoolGame.memento.Memento;
import PoolGame.memento.MementoB;
import PoolGame.memento.Originator;
import PoolGame.objects.*;

import java.awt.event.ActionListener;
import java.util.*;

import PoolGame.observer.ObservableSubject;
import PoolGame.observer.Observer;
import PoolGame.observer.PointsDisplayer;
import com.sun.media.jfxmediaimpl.HostUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.paint.Paint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

import javafx.util.Duration;
import javafx.util.Pair;

import javax.swing.Timer;

/**
 * Controls the game interface; drawing objects, handling logic and collisions.
 */
public class GameManager implements ObservableSubject {
    private Table table;
    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private Line cue;
    private boolean cueSet = false;
    private boolean cueActive = false;
    private boolean winFlag = false;
    private int score,expected = 0;
    long elapsedTime = 0;

    private final double TABLEBUFFER = Config.getTableBuffer();
    private final double TABLEEDGE = Config.getTableEdge();
    private final double FORCEFACTOR = 0.1;

    private Scene scene;
    private GraphicsContext gc;

    private Button undoBtn,saveBtn,undoBtnB,saveBtnB;
    private Button cheatBtn;

    private List<Observer> observers;
    private String cheatColour;

    private long startTime;
    private String secondsDisplay;
    private String elapsedMinutes;
    private boolean isUndo =false;
    private Memento memento;

    private MementoB mementoB;

    private Originator originator = new Originator(this);
    private Caretaker caretaker = new Caretaker();

    public GameManager(){
        this.observers = new ArrayList<>();
    }

    /**
     * Initialises timeline and cycle count.
     */
    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> this.draw()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        startTime = System.currentTimeMillis();
    }

    /**
     * Builds GameManager properties such as initialising pane, canvas,
     * graphicscontext, and setting events related to clicks.
     */
    public void buildManager() {
        Pane pane = new Pane();
        setClickEvents(pane);
        this.scene = new Scene(pane, table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);
        Canvas canvas = new Canvas(table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

        //Add a Undo button, should apply Memento pattern
        undoBtn = new Button("Undo");
        undoBtn.setTranslateX(540);
        undoBtn.setTranslateY(10);
        undoBtn.setOnAction(new EventHandler<ActionEvent>() {
            //Now it worked with click
            @Override
            public void handle(ActionEvent event) {
                undo(memento);
            }
        });
        pane.getChildren().add(undoBtn);

        //Add a save button, should apply Memento pattern
        saveBtn = new Button("Save");
        saveBtn.setTranslateX(300);
        saveBtn.setTranslateY(10);
        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            //Now it worked with click
            @Override
            public void handle(ActionEvent event) {
                memento = save();
            }
        });
        pane.getChildren().add(saveBtn);

        //TODO: Add a save button, should apply Memento pattern
        saveBtnB = new Button("SaveB");
        saveBtnB.setTranslateX(340);
        saveBtnB.setTranslateY(10);
        saveBtnB.setOnAction(new EventHandler<ActionEvent>() {
            //Now it worked with click
            @Override
            public void handle(ActionEvent event) {
                saveB();
            }
        });
        pane.getChildren().add(saveBtnB);

        //TODO: Add a Undo button, should apply Memento pattern
        undoBtnB = new Button("UndoB");
        undoBtnB.setTranslateX(580);
        undoBtnB.setTranslateY(10);
        undoBtnB.setOnAction(new EventHandler<ActionEvent>() {
            //Now it worked with click
            @Override
            public void handle(ActionEvent event) {
                undoB();
            }
        });
        pane.getChildren().add(undoBtnB);


        //Add a cheat button, which offer a range of coloured balls choices for cheating
        cheatBtn = new Button("Cheat");
        cheatBtn.setTranslateX(5);
        cheatBtn.setTranslateY(60);
        cheatBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //generate a dropdown selection of choices for ball cheating
                ObservableList<String> options =
                        FXCollections.observableArrayList(
                                "black",
                                "red",
                                "yellow",
                                "green","brown","blue","purple","orange"
                        );
                final ComboBox comboBox = new ComboBox(options);

                //Below are EventHandler for the combo box, selecting the colour of the ball for cheating
                comboBox.setPromptText("Select cheat colour");
                comboBox.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        cheatColour = (String) comboBox.getValue();
                        cheat();
                    }
                });
                pane.getChildren().add(comboBox);
            }
        });
        pane.getChildren().add(cheatBtn);
    }

    /**
     * Draws all relevant items - table, cue, balls, pockets - onto Canvas.
     * Used Exercise 6 as reference.
     */
    private void draw() {
        tick();


        // Fill in background
        gc.setFill(Paint.valueOf("white"));
        gc.fillRect(0, 0, table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);

        // Fill in edges
        gc.setFill(Paint.valueOf("brown"));
        gc.fillRect(TABLEBUFFER - TABLEEDGE, TABLEBUFFER - TABLEEDGE, table.getxLength() + TABLEEDGE * 2,
                table.getyLength() + TABLEEDGE * 2);

        // Fill in Table
        gc.setFill(table.getColour());
        gc.fillRect(TABLEBUFFER, TABLEBUFFER, table.getxLength(), table.getyLength());

        // Fill in Pockets
        for (Pocket pocket : table.getPockets()) {
            gc.setFill(Paint.valueOf("black"));
            gc.fillOval(pocket.getxPos() - pocket.getRadius(), pocket.getyPos() - pocket.getRadius(),
                    pocket.getRadius() * 2, pocket.getRadius() * 2);
        }

        // Cue
        if (this.cue != null && cueActive) {
            gc.strokeLine(cue.getStartX(), cue.getStartY(), cue.getEndX(), cue.getEndY());
            cue.setStrokeWidth(5);
            cue.setStroke(Color.BURLYWOOD);
        }

        for (Ball ball : balls) {
            if (ball.isActive()) {
                gc.setFill(ball.getColour());
                gc.fillOval(ball.getxPos() - ball.getRadius(),
                        ball.getyPos() - ball.getRadius(),
                        ball.getRadius() * 2,
                        ball.getRadius() * 2);
            }
        }

        // Win
        if (winFlag) {
            gc.setStroke(Paint.valueOf("white"));
            gc.setFont(new Font("Impact", 80));
            gc.strokeText("Win and bye", table.getxLength() / 2 + TABLEBUFFER - 180,
                    table.getyLength() / 2 + TABLEBUFFER);
        }



        //Draw time

        secondsDisplay = String.format("%02d", elapsedTime % 60);
        elapsedMinutes = String.format("%02d",elapsedTime / 60);


        if (!winFlag) {
            elapsedTime = (System.currentTimeMillis()-startTime)/1000;
            gc.setFill(Color.LIGHTPINK);
            gc.setFont(new Font("Comic sans MS", 24));
            gc.fillText("Time: "+ getElapsedMinutes()+"："+getSecondsDisplay(),400,24);
//FIXME            System.out.println("draw: "+secondsDisplay);
        } else {
            //Draw a duplicate one
            gc.setFill(Color.LIGHTPINK);
            gc.setFont(new Font("Comic sans MS",24));
            gc.fillText("Time: "+ elapsedMinutes+": "+secondsDisplay,400,24);
        }


        //Draw score using observer pattern
        PointsDisplayer pointsDisplayer = new PointsDisplayer(this);
        attachObserver(pointsDisplayer);
        gc.fillText(notifyObservers(),100,24);
    }

    /**
     * Updates positions of all balls, handles logic related to collisions.
     * Used Exercise 6 as reference.
     */
    public void tick() {

        PointsDisplayer pointsDisplayer = new PointsDisplayer(this);
        attachObserver(pointsDisplayer);

        //Score match expected
        if (score==expected){
            winFlag = true;
        }

        for (Ball ball : balls) {
            ball.tick();

            if (ball.isCue() && cueSet) {
                hitBall(ball);
            }

            double width = table.getxLength();
            double height = table.getyLength();

            // Check if ball landed in pocket
            for (Pocket pocket : table.getPockets()) {
                if (pocket.isInPocket(ball)) {
                    if (ball.isCue()) {
                        this.reset();
                    } else {
                        if (ball.remove()) {
                            score+=ball.getScore();
                        } else {
                            // Check if when ball is removed, any other balls are present in its space. (If
                            // another ball is present, blue ball is removed)
                            for (Ball otherBall : balls) {
                                double deltaX = ball.getxPos() - otherBall.getxPos();
                                double deltaY = ball.getyPos() - otherBall.getyPos();
                                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                                if (otherBall != ball && otherBall.isActive() && distance < 10) {
                                    ball.remove();
                                }
                            }
                        }
                    }
                    break;
                }
            }

            // Handle the edges (balls don't get a choice here)
            if (ball.getxPos() + ball.getRadius() > width + TABLEBUFFER) {
                ball.setxPos(width - ball.getRadius());
                ball.setxVel(ball.getxVel() * -1);
            }
            if (ball.getxPos() - ball.getRadius() < TABLEBUFFER) {
                ball.setxPos(ball.getRadius());
                ball.setxVel(ball.getxVel() * -1);
            }
            if (ball.getyPos() + ball.getRadius() > height + TABLEBUFFER) {
                ball.setyPos(height - ball.getRadius());
                ball.setyVel(ball.getyVel() * -1);
            }
            if (ball.getyPos() - ball.getRadius() < TABLEBUFFER) {
                ball.setyPos(ball.getRadius());
                ball.setyVel(ball.getyVel() * -1);
            }

            // Apply table friction
            double friction = table.getFriction();
            ball.setxVel(ball.getxVel() * friction);
            ball.setyVel(ball.getyVel() * friction);

            // Check ball collisions
            for (Ball ballB : balls) {
                if (checkCollision(ball, ballB)) {
                    Point2D ballPos = new Point2D(ball.getxPos(), ball.getyPos());
                    Point2D ballBPos = new Point2D(ballB.getxPos(), ballB.getyPos());
                    Point2D ballVel = new Point2D(ball.getxVel(), ball.getyVel());
                    Point2D ballBVel = new Point2D(ballB.getxVel(), ballB.getyVel());
                    Pair<Point2D, Point2D> changes = calculateCollision(ballPos, ballVel, ball.getMass(), ballBPos,
                            ballBVel, ballB.getMass(), false);
                    calculateChanges(changes, ball, ballB);
                }
            }
        }

    }

    /**
     * Save the detail into Memento class
     * @return the saving state
     */
    public Memento save(){
        //make deep copy
        List<Double> xPos = new ArrayList<>();
        List<Double> yPos = new ArrayList<>();
        List<Boolean> visibleList = new ArrayList<>();
        int curScore = this.getScore();
        String seconds = secondsDisplay;
        String minutes = elapsedMinutes;
        for (Ball ball:this.balls){
            xPos.add(ball.getxPos());
            yPos.add(ball.getyPos());
            visibleList.add(ball.isActive());
        }
        return new Memento(xPos,yPos,curScore,seconds,minutes,visibleList);
    }


    /**
     * Undo the game
     * @param memento call methods in Memento class to get saved state
     */
    public void undo(Memento memento){
        List<Double> savedxPos = memento.getxPos();
        List<Double> savedyPos = memento.getyPos();
        for (int i= 0;i < balls.size();i++){
            //respawn the ball when it is in the pocket
            if (!balls.get(i).isActive()){
                balls.get(i).setActive(true);
                balls.get(i).addLives();
            }
            balls.get(i).setxPos(savedxPos.get(i)-TABLEBUFFER);
            balls.get(i).setyPos(savedyPos.get(i)-TABLEBUFFER);
        }
        this.score = memento.getScore();
        this.setSecondsDisplay(memento.getSeconds());
        this.setElapsedMinutes(memento.getMinutes());
//FIXME        System.out.println("undo"+this.secondsDisplay);
    }

    public void saveB(){
        caretaker.setMementoB(originator.saveB());
    }

    public void undoB(){
        originator.undoB(caretaker.getMementoB());
    }


    /**
     * Compare the colour of the balls and Perform cheat action.
     */
    public void cheat(){
        for (Ball ball: balls){
            if (Paint.valueOf(cheatColour).equals(ball.getColour())){
                score+=ball.getScore();
            }
        }
        balls.removeIf(ball -> Paint.valueOf(cheatColour).equals(ball.getColour()));

    }

    /**
     * Resets the game.
     */
    public void reset() {
        for (Ball ball : balls) {
            ball.reset();
        }

        this.score = 0;
    }

    /**
     * @return scene.
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Sets the table of the game.
     * 
     * @param table
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * @return table
     */
    public Table getTable() {
        return this.table;
    }

    /**
     * Sets the balls of the game.
     * 
     * @param balls
     */
    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    /**
     * Sets the expected winning score of the pool table
     *
     * @param expected
     */
    public void setExpected(int expected){
        this.expected = expected;
    }

    /**
     * Hits the ball with the cue, distance of the cue indicates the strength of the
     * strike.
     * 
     * @param ball
     */
    private void hitBall(Ball ball) {
        double deltaX = ball.getxPos() - cue.getStartX();
        double deltaY = ball.getyPos() - cue.getStartY();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Check that start of cue is within cue ball
        if (distance < ball.getRadius()) {
            // Collide ball with cue
            double hitxVel = (cue.getStartX() - cue.getEndX()) * FORCEFACTOR;
            double hityVel = (cue.getStartY() - cue.getEndY()) * FORCEFACTOR;

            ball.setxVel(hitxVel);
            ball.setyVel(hityVel);
        }

        cueSet = false;

    }

    /**
     * Changes values of balls based on collision (if ball is null ignore it)
     * 
     * @param changes
     * @param ballA
     * @param ballB
     */
    private void calculateChanges(Pair<Point2D, Point2D> changes, Ball ballA, Ball ballB) {
        ballA.setxVel(changes.getKey().getX());
        ballA.setyVel(changes.getKey().getY());
        if (ballB != null) {
            ballB.setxVel(changes.getValue().getX());
            ballB.setyVel(changes.getValue().getY());
        }
    }

    /**
     * Sets the cue to be drawn on click, and manages cue actions
     * 
     * @param pane
     */
    private void setClickEvents(Pane pane) {
        pane.setOnMousePressed(event -> {
            cue = new Line(event.getX(), event.getY(), event.getX(), event.getY());
            cue.setStrokeWidth(5);
            cue.setStroke(Color.BURLYWOOD);
            pane.getChildren().add(cue);
            cueSet = false;
            cueActive = true;
        });

        pane.setOnMouseDragged(event -> {
            cue.setEndX(event.getX());
            cue.setEndY(event.getY());
        });

        pane.setOnMouseReleased(event -> {
            pane.getChildren().remove(cue);
            cueSet = true;
            cueActive = false;
        });
    }

    /**
     * Checks if two balls are colliding.
     * Used Exercise 6 as reference.
     * 
     * @param ballA
     * @param ballB
     * @return true if colliding, false otherwise
     */
    private boolean checkCollision(Ball ballA, Ball ballB) {
        if (ballA == ballB) {
            return false;
        }

        return Math.abs(ballA.getxPos() - ballB.getxPos()) < ballA.getRadius() + ballB.getRadius() &&
                Math.abs(ballA.getyPos() - ballB.getyPos()) < ballA.getRadius() + ballB.getRadius();
    }

    /**
     * Collision function adapted from assignment, using physics algorithm:
     * http://www.gamasutra.com/view/feature/3015/pool_hall_lessons_fast_accurate_.php?page=3
     *
     * @param positionA The coordinates of the centre of ball A
     * @param velocityA The delta x,y vector of ball A (how much it moves per tick)
     * @param massA     The mass of ball A (for the moment this should always be the
     *                  same as ball B)
     * @param positionB The coordinates of the centre of ball B
     * @param velocityB The delta x,y vector of ball B (how much it moves per tick)
     * @param massB     The mass of ball B (for the moment this should always be the
     *                  same as ball A)
     *
     * @return A Pair in which the first (key) Point2D is the new
     *         delta x,y vector for ball A, and the second (value) Point2D is the
     *         new delta x,y vector for ball B.
     */
    public static Pair<Point2D, Point2D> calculateCollision(Point2D positionA, Point2D velocityA, double massA,
            Point2D positionB, Point2D velocityB, double massB, boolean isCue) {

        // Find the angle of the collision - basically where is ball B relative to ball
        // A. We aren't concerned with
        // distance here, so we reduce it to unit (1) size with normalize() - this
        // allows for arbitrary radii
        Point2D collisionVector = positionA.subtract(positionB);
        collisionVector = collisionVector.normalize();

        // Here we determine how 'direct' or 'glancing' the collision was for each ball
        double vA = collisionVector.dotProduct(velocityA);
        double vB = collisionVector.dotProduct(velocityB);

        // If you don't detect the collision at just the right time, balls might collide
        // again before they leave
        // each others' collision detection area, and bounce twice.
        // This stops these secondary collisions by detecting
        // whether a ball has already begun moving away from its pair, and returns the
        // original velocities
        if (vB <= 0 && vA >= 0 && !isCue) {
            return new Pair<>(velocityA, velocityB);
        }

        // This is the optimisation function described in the gamasutra link. Rather
        // than handling the full quadratic
        // (which as we have discovered allowed for sneaky typos)
        // this is a much simpler - and faster - way of obtaining the same results.
        double optimizedP = (2.0 * (vA - vB)) / (massA + massB);

        // Now we apply that calculated function to the pair of balls to obtain their
        // final velocities
        Point2D velAPrime = velocityA.subtract(collisionVector.multiply(optimizedP).multiply(massB));
        Point2D velBPrime = velocityB.add(collisionVector.multiply(optimizedP).multiply(massA));

        return new Pair<>(velAPrime, velBPrime);
    }

    /**
     *
     * @return The score of the ball
     */
    public int getScore() {
        return score;
    }

    // Observer Methods
    @Override
    public void attachObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void detachObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public String notifyObservers() {
        String messege = null;
        for (Observer o : observers) {
            messege = o.observe();
        }
        return messege;
    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public void setSecondsDisplay(String secondsDisplay) {
        this.secondsDisplay = secondsDisplay;
    }

    public void setElapsedMinutes(String elapsedMinutes) {
        this.elapsedMinutes = elapsedMinutes;
    }

    public String getSecondsDisplay(){
        return secondsDisplay;
    }

    public String getElapsedMinutes() {
        return elapsedMinutes;
    }
}
