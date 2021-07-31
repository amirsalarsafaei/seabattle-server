package APIs;

import DataBase.GsonHandler;
import DataBase.Load;
import Models.Request;
import Models.Response;
import Models.ResponseType;
import Models.ScoreBoard.ScoreBoardList;
import StreamHandler.StreamHandler;

public class ScoreBoard {

    public void getScoreBoard(Request request) {
        StreamHandler.sendResponse(request.dataOut, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(new ScoreBoardList(Load.getAllUsers()))));
    }
}
