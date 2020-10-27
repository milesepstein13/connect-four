package persistance;

import model.GameBoard;
import model.GamePiece;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Source: Json Serialization Demo
public class JsonReader {
    private static final int BOARD_WIDTH = 7;
    private static final int BOARD_HEIGHT = 7;
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public GameBoard read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGameBoard(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses gameboard from JSON object and returns it
    private GameBoard parseGameBoard(JSONObject jsonObject) {
        int redWins = jsonObject.getInt("redWins");
        int yellowWins = jsonObject.getInt("yellowWins");
        int ties = jsonObject.getInt("ties");
        GameBoard gb = new GameBoard();
        setBoard(gb, jsonObject);
        gb.setRedWins(redWins);
        gb.setYellowWins(yellowWins);
        gb.setTies(ties);
        return gb;
    }

    // MODIFIES: gb
    // EFFECTS: parses gamepieces from JSON object and adds them to Gameboard
    private void setBoard(GameBoard gb, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("board");
        int i = 1;
        for (Object json : jsonArray) {
            JSONArray nextColumn = (JSONArray) json;
            addColumn(gb, nextColumn, i);
            i++;
        }
    }

    // EFFECTS:
    // Adds each game piece from the json array column into given column of gb
    private void addColumn(GameBoard gb, JSONArray jsonArray, int column) {
        for (Object json: jsonArray) {
            gb.addPiece(column, parseGamePiece(json));
        }
    }

    // MODIFIES
    // EFFECTS: parses gamepiece from JSON object
    private GamePiece parseGamePiece(Object pieceObject) {
        JSONObject jsonPiece = (JSONObject) pieceObject;
        int color = jsonPiece.getInt("color");
        return new GamePiece(color);
    }
}
