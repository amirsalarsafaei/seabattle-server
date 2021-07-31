package APIs;

import DataBase.GsonHandler;
import Holder.GameStateHolder;
import Holder.PlayerDataOut;
import Models.Game.GameState;
import Models.Request;
import Models.Response;
import Models.ResponseType;
import StreamHandler.StreamHandler;
import Utils.RandomGenerator;

import java.util.LinkedList;

public class MatchMaking {
    private static LinkedList<Request> matchMakingRequests = new LinkedList<>();
    public synchronized static void addMatchMakingRequest(Request request) {
        matchMakingRequests.add(request);
        if (request.user == null)
            System.out.println("wtf");
        if (matchMakingRequests.size() > 1) {
            Request player1, player2;
            if (RandomGenerator.RandomInt(2) == 1) {
                player1 = matchMakingRequests.get(0);
                player2 = matchMakingRequests.get(1);
            }
            else {
                player2 = matchMakingRequests.get(0);
                player1 = matchMakingRequests.get(1);
            }
            matchMakingRequests.removeFirst();
            matchMakingRequests.removeFirst();
            GameState gameState = new GameState(player1.user.username, player2.user.username);
            GameStateHolder.gameStateHashMap.put(gameState.id, gameState);
            GameStateHolder.gameStates.add(gameState);
            PlayerDataOut.dataOut.put(gameState.player1, player1.dataOut);
            PlayerDataOut.dataOut.put(gameState.player2, player2.dataOut);

            StreamHandler.sendResponse(player1.dataOut, new Response(ResponseType.Accepted,
                    GsonHandler.getGson().toJson(gameState.player1)));
            StreamHandler.sendResponse(player2.dataOut, new Response(ResponseType.Accepted,
                    GsonHandler.getGson().toJson(gameState.player2)));
        }
    }
}
