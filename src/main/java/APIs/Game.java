package APIs;

import DataBase.GsonHandler;
import DataBase.Load;
import DataBase.Save;
import Holder.GameStateHolder;
import Holder.PlayerDataOut;
import Models.Game.*;
import Models.Request;
import Models.Response;
import Models.ResponseType;
import Models.User;
import StreamHandler.StreamHandler;
import Utils.Finder;

import java.time.LocalDateTime;

public class Game {

    public void reArrange(Request request) {
        int gameID = Integer.valueOf(request.data);
        Player player = null;
        GameState gameState = GameStateHolder.gameStateHashMap.get(gameID);
        if (gameState.player1.username.equals(request.user.username))
            player = gameState.player1;
        else
            player = gameState.player2;
        if (player.timesReArranged == 3) {
            StreamHandler.sendResponse(request.dataOut, new Response(ResponseType.Denied, ""));

            System.out.println("Shit");
            return;
        }
        player.turnEndTime = player.turnEndTime.plusSeconds(10);
        player.shipLayout = new ShipLayout();
        player.timesReArranged++;
        StreamHandler.sendResponse(request.dataOut, new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(player)));
    }

    public synchronized void Ready(Request request) {
        Player player = null, versus = null;
        GameState gameState = GameStateHolder.gameStateHashMap.get(request.GameID);
        if (gameState.player1.username.equals(request.user.username)) {
            player = gameState.player1;
            versus = gameState.player2;
        }
        else {
            player = gameState.player2;
            versus = gameState.player1;
        }
        if (player.gamePhase != GamePhase.Arranging) {
            StreamHandler.sendResponse(request.dataOut, new Response(ResponseType.BadRequest, ""));
            System.out.println("impossible");
            return;
        }
        player.gamePhase = GamePhase.Ready;
        if (versus.gamePhase == GamePhase.Ready) {
            gameState.player1.gamePhase = GamePhase.Playing;
            player.turnEndTime = versus.turnEndTime = LocalDateTime.now().plusSeconds(25);
            gameState.player2.gamePhase = GamePhase.Waiting;
            StreamHandler.sendResponse(PlayerDataOut.dataOut.get(player),
                    new Response(ResponseType.Accepted, GsonHandler.getGson().toJson(player)));
            StreamHandler.sendResponse(PlayerDataOut.dataOut.get(versus),
                    new Response(ResponseType.Accepted, GsonHandler.getGson().toJson(versus)));
        }
        else
            StreamHandler.sendResponse(request.dataOut, new Response(ResponseType.Accepted,
                    GsonHandler.getGson().toJson(player)));
    }

    public synchronized void Shot(Request request) {
        Player player = null, versus = null;
        GameState gameState = GameStateHolder.gameStateHashMap.get(request.GameID);
        if (gameState.player1.username.equals(request.user.username)) {
            player = gameState.player1;
            versus = gameState.player2;
        }
        else {
            player = gameState.player2;
            versus = gameState.player1;
        }
        if (player.gamePhase == GamePhase.Waiting) {
            System.exit(0);
        }
        if (LocalDateTime.now().isAfter(player.turnEndTime)) {
            StreamHandler.sendResponse(PlayerDataOut.dataOut.get(player),new Response(ResponseType.Accepted,
                    GsonHandler.getGson().toJson(player)));
            StreamHandler.sendResponse(PlayerDataOut.dataOut.get(versus),new Response(ResponseType.Accepted,
                    GsonHandler.getGson().toJson(versus)));
            return;
        }

        Coordinate coordinate = GsonHandler.getGson().fromJson(request.data, Coordinate.class);
        if (Finder.BoolFind(player.landedShots, coordinate) || Finder.BoolFind(player.missedShots, coordinate)) {
            StreamHandler.sendResponse(PlayerDataOut.dataOut.get(player),new Response(ResponseType.Accepted,
                    GsonHandler.getGson().toJson(player)));
            StreamHandler.sendResponse(PlayerDataOut.dataOut.get(versus),new Response(ResponseType.Accepted,
                    GsonHandler.getGson().toJson(versus)));
            return;
        }
        for (Ship ship:versus.shipLayout.ships) {
            if (Finder.BoolFind(ship.ship, coordinate)) {
                player.landedShots.add(coordinate);
                versus.enemyLandedShots.add(coordinate);
                boolean isBroke = true;
                for (Coordinate tmp:ship.ship) {
                    if (!Finder.BoolFind(player.landedShots, tmp)) {
                        isBroke = false;
                    }
                }
                if (isBroke) {
                    Grid grid = new Grid();
                    for (Coordinate tmp:grid.cells) {
                        boolean shouldBreak = false;
                        for (Coordinate tmp2:ship.ship) {
                            if (Math.abs(tmp.x - tmp2.x) + Math.abs(tmp.y - tmp2.y) == 1 ||
                                    (Math.abs(tmp.x - tmp2.x) == 1 && Math.abs(tmp.y - tmp2.y) == 1)) {
                                shouldBreak = true;
                            }
                        }
                        if (shouldBreak &&!Finder.BoolFind(player.missedShots, tmp) && !Finder.BoolFind(ship.ship, tmp)) {
                            player.missedShots.add(tmp);
                            versus.enemyMissedShots.add(tmp);
                        }
                    }
                    player.BrokeShips.add(ship);
                    if (player.BrokeShips.size() == versus.shipLayout.ships.size()) {
                        player.gamePhase = GamePhase.Win;
                        versus.gamePhase = GamePhase.Lose;
                        User playerUser = Load.LoadUser(player.username), versusUser = Load.LoadUser(player.versusUsername);
                        playerUser.wins++;
                        versusUser.loses++;
                        Save.save(playerUser);
                        Save.save(versusUser);
                        GameStateHolder.gameStates.remove(gameState);
                    }
                }
                StreamHandler.sendResponse(PlayerDataOut.dataOut.get(player),new Response(ResponseType.Accepted,
                        GsonHandler.getGson().toJson(player)));
                StreamHandler.sendResponse(PlayerDataOut.dataOut.get(versus),new Response(ResponseType.Accepted,
                        GsonHandler.getGson().toJson(versus)));
                return;
            }
        }
        player.missedShots.add(coordinate);
        versus.enemyMissedShots.add(coordinate);
        player.gamePhase = GamePhase.Waiting;
        versus.gamePhase = GamePhase.Playing;
        player.turnEndTime = versus.turnEndTime = LocalDateTime.now().plusSeconds(25);
        StreamHandler.sendResponse(PlayerDataOut.dataOut.get(player),new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(player)));
        StreamHandler.sendResponse(PlayerDataOut.dataOut.get(versus),new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(versus)));
    }

    public synchronized void TurnDone(Request request) {
        Player player = null, versus = null;
        GameState gameState = GameStateHolder.gameStateHashMap.get(request.GameID);
        if (gameState.player1.username.equals(request.user.username)) {
            player = gameState.player1;
            versus = gameState.player2;
        }
        else {
            player = gameState.player2;
            versus = gameState.player1;
        }
        player.gamePhase = GamePhase.Waiting;
        versus.gamePhase = GamePhase.Playing;
        player.turnEndTime = versus.turnEndTime = LocalDateTime.now().plusSeconds(25);
        StreamHandler.sendResponse(PlayerDataOut.dataOut.get(player),new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(player)));
        StreamHandler.sendResponse(PlayerDataOut.dataOut.get(versus),new Response(ResponseType.Accepted,
                GsonHandler.getGson().toJson(versus)));
    }
}
