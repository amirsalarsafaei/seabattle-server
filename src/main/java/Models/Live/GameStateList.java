package Models.Live;

import Models.Game.GameState;

import java.util.ArrayList;

public class GameStateList {
    public ArrayList<GameStateListMode> gameStateLists;

    public GameStateList(ArrayList<GameState> gameStates) {
        gameStateLists = new ArrayList<>();
        for (GameState gameState:gameStates) {
            gameStateLists.add(new GameStateListMode(gameState));
        }
    }
}
