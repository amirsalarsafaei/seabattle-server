package Holder;

import Models.Game.GameState;
import Models.Game.Player;

import java.io.DataOutputStream;
import java.util.HashMap;

public class PlayerDataOut {
    public static HashMap<Player, DataOutputStream> dataOut = new HashMap<>();
}
