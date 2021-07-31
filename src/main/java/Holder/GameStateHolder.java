package Holder;

import Models.Game.GameState;

import java.util.ArrayList;
import java.util.HashMap;

public class GameStateHolder {
    public static HashMap<Integer, GameState> gameStateHashMap = new HashMap<>();
    public static ArrayList<GameState> gameStates = new ArrayList<>();
}
