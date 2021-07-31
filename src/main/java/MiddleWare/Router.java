package MiddleWare;

import APIs.*;
import DataBase.GsonHandler;
import Models.Request;
import Models.RequestType;
import Models.Response;
import Models.ResponseType;
import ReflectUtil.SuperMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashMap;

public class Router {
    private static final Logger logger = LogManager.getLogger(Router.class);
    EnumMap<RequestType, SuperMethod> routes;
    Authenticate authenticate;
    DataOutputStream dataOutputStream;
    public Router(Authenticate authenticate, DataOutputStream dataOutputStream) {
        this.authenticate = authenticate;
        this.dataOutputStream = dataOutputStream;
        Game game = new Game();
        Live live = new Live();
        ScoreBoard scoreBoard = new ScoreBoard();
        routes = new EnumMap<>(RequestType.class);
        try {
            routes.put(RequestType.SignUp,new SuperMethod(authenticate.getClass().getMethod("SignUp", Request.class)
            , authenticate));
            routes.put(RequestType.SignIn,new SuperMethod(authenticate.getClass().getMethod("SignIn", Request.class)
                    , authenticate));
            routes.put(RequestType.StartMatch,new SuperMethod(MatchMaking.class.getMethod("addMatchMakingRequest", Request.class)
                    , null));
            routes.put(RequestType.ReArrange, new SuperMethod(Game.class.getMethod("reArrange", Request.class), game));
            routes.put(RequestType.Ready, new SuperMethod(Game.class.getMethod("Ready", Request.class), game));
            routes.put(RequestType.Shot, new SuperMethod(Game.class.getMethod("Shot", Request.class), game));
            routes.put(RequestType.TurnDone, new SuperMethod(Game.class.getMethod("TurnDone", Request.class), game));
            routes.put(RequestType.LiveList, new SuperMethod(Live.class.getMethod("getLiveList", Request.class), live));
            routes.put(RequestType.Live, new SuperMethod(Live.class.getMethod("getLiveGame", Request.class), live));
            routes.put(RequestType.Online, new SuperMethod(Authenticate.class.getMethod("lastOnline", Request.class)
                    , authenticate));
            routes.put(RequestType.ScoreBoard, new SuperMethod(ScoreBoard.class.getMethod("getScoreBoard",
                    Request.class), scoreBoard));
            routes.put(RequestType.Profile, new SuperMethod(ProfileHandling.class.getMethod("getProfile", Request.class),
                    new ProfileHandling()));
        }catch (NoSuchMethodException e) {
            logger.error("Method not found in router");
        }
    }

    public void route(Request request) {
        try {
            routes.get(request.requestType).invoke(request);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            logger.error("Problem in running request : " + request.requestType);
            Response response = new Response(ResponseType.BadRequest, "Couldn't route");
            try {
                dataOutputStream.writeUTF(GsonHandler.getGson().toJson(response));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
