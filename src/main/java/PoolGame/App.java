package PoolGame;

//import PoolGame.config.*;

import java.util.List;

import config.BallReaderFactory;
import config.Reader;
import config.ReaderFactory;
import config.TableReaderFactory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


/** Main application entry point. */
public class App extends Application {

    private Pane pane;
    private Scene scene;
    private GraphicsContext gc;
    /**
     * @param args First argument is the path to the config file
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    /**
     * Starts the application.
     *
     * @param primaryStage The primary stage for the application.
     */
    public void start(Stage primaryStage) {

        pane = new Pane();
        this.scene = new Scene(pane, 600, 400);
        Canvas canvas = new Canvas(600, 400);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);


        Button button1 = new Button("easy");
        button1.setTranslateX(280);
        button1.setTranslateY(40);
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameManager gameManager = new GameManager();

                List<String> args = List.of("src/main/resources/config_easy.json");
                String configPath = checkConfig(args);

                ReaderFactory tableFactory = new TableReaderFactory();
                Reader tableReader = tableFactory.buildReader();
                tableReader.parse(configPath, gameManager);

                ReaderFactory ballFactory = new BallReaderFactory();
                Reader ballReader = ballFactory.buildReader();
                ballReader.parse(configPath, gameManager);
                gameManager.buildManager();
                Stage stage = new Stage();

                gameManager.run();
                stage.setTitle("Pool");
                stage.setScene(gameManager.getScene());
                stage.show();
                gameManager.run();
            }
        });
        pane.getChildren().add(button1);

        Button button2 = new Button("normal");
        button2.setTranslateX(280);
        button2.setTranslateY(80);
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameManager gameManager = new GameManager();

                List<String> args = List.of("src/main/resources/config_normal.json");
                String configPath = checkConfig(args);

                ReaderFactory tableFactory = new TableReaderFactory();
                Reader tableReader = tableFactory.buildReader();
                tableReader.parse(configPath, gameManager);

                ReaderFactory ballFactory = new BallReaderFactory();
                Reader ballReader = ballFactory.buildReader();
                ballReader.parse(configPath, gameManager);
                gameManager.buildManager();
                Stage stage = new Stage();

                gameManager.run();
                stage.setTitle("Pool");
                stage.setScene(gameManager.getScene());
                stage.show();
                gameManager.run();
            }
        });
        pane.getChildren().add(button2);

        Button button3 = new Button("hard");
        button3.setTranslateX(280);
        button3.setTranslateY(120);
        button3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameManager gameManager = new GameManager();

                List<String> args = List.of("src/main/resources/config_hard.json");
                String configPath = checkConfig(args);

                ReaderFactory tableFactory = new TableReaderFactory();
                Reader tableReader = tableFactory.buildReader();
                tableReader.parse(configPath, gameManager);

                ReaderFactory ballFactory = new BallReaderFactory();
                Reader ballReader = ballFactory.buildReader();
                ballReader.parse(configPath, gameManager);
                gameManager.buildManager();
                Stage stage = new Stage();

                gameManager.run();
                stage.setTitle("Pool");
                stage.setScene(gameManager.getScene());
                stage.show();
                gameManager.run();
            }
        });
        pane.getChildren().add(button3);


        primaryStage.setScene(this.scene);
        primaryStage.show();
    }


    /**
     * Checks if the config file path is given as an argument.
     *
     * @param args
     * @return config path.
     */
    public static String checkConfig(List<String> args) {
        String configPath;
        if (args.size() > 0) {
            configPath = args.get(0);
        } else {
            configPath = "src/main/resources/config_easy.json";
        }
        return configPath;
    }

}
