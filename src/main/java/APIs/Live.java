package APIs;

import DataBase.GsonHandler;
import Holder.GameStateHolder;
import Models.Live.GameStateList;
import Models.Live.LiveGame;
import Models.Request;
import Models.Response;
import Models.ResponseType;
import StreamHandler.StreamHandler;

import java.util.stream.Stream;

public class Live {

    public void getLiveList(Request request) {
        GameStateList gameStateList = new GameStateList(GameStateHolder.gameStates);
        StreamHandler.sendResponse(request.dataOut, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(gameStateList)));
    }

    public void getLiveGame(Request request) {
        StreamHandler.sendResponse(request.dataOut, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(new LiveGame(GameStateHolder.gameStateHashMap.get(request.GameID)))));
    }
}
