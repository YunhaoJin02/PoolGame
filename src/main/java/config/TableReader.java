package config;

import PoolGame.GameManager;
import PoolGame.objects.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/** Readers table section of JSON. */
public class TableReader implements Reader {
    /**
     * Parses the JSON file and builds the table.
     * 
     * @param path        The path to the JSON file.
     * @param gameManager The game manager.
     */
    public void parse(String path, GameManager gameManager) {
        JSONParser parser = new JSONParser();
        List<Pocket> pockets = new ArrayList<>();
        try {
            Object object = parser.parse(new FileReader(path));

            // convert Object to JSONObject
            JSONObject jsonObject = (JSONObject) object;

            // reading the Table section:
            JSONObject jsonTable = (JSONObject) jsonObject.get("Table");

            // reading a value from the table section
            String tableColour = (String) jsonTable.get("colour");

            // reading a coordinate from the nested section within the table
            // note that the table x and y are of type Long (i.e. they are integers)
            Long tableX = (Long) ((JSONObject) jsonTable.get("size")).get("x");
            Long tableY = (Long) ((JSONObject) jsonTable.get("size")).get("y");

            // getting the friction level.
            // This is a double which should affect the rate at which the balls slow down
            Double tableFriction = (Double) jsonTable.get("friction");

            // Check friction level is between 0 and 1
            if (tableFriction >= 1 || tableFriction <= 0) {
                System.out.println("Friction must be between 0 and 1");
                System.exit(0);
            }


            // reading the "pockets" array:
            JSONArray jsonPockets = (JSONArray) jsonTable.get("pockets");

            for (Object obj: jsonPockets){
                JSONObject jsonPocket = (JSONObject) obj;

                //start reading parameters form json file
                Double positionX = (Double) ((JSONObject) jsonPocket.get("position")).get("x");
                Double positionY = (Double) ((JSONObject) jsonPocket.get("position")).get("y");
                Double radius = (Double) jsonPocket.get("radius");

                //builder pattern
                PocketBuilder builder = new PoolPocketBuilder();
                builder.setRadius(radius);
                builder.setxPos(positionX);
                builder.setyPos(positionY);
                pockets.add(builder.build());
            }

            gameManager.setTable(new Table(tableColour, tableX, tableY, tableFriction,pockets));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
