package Models.Game;

import Holder.IDHolder;

import java.time.LocalDateTime;

public class GameState {
    public Player player1;
    public Player player2;
    public int id;
    public LocalDateTime localDateTime;
    public GameState(String player1, String player2) {
        id = IDHolder.gameID++;
        localDateTime = LocalDateTime.now();
        this.player1 = new Player(player1, player2, id, GridChoice.left);
        this.player2 = new Player(player2, player1, id, GridChoice.right);
    }
}
