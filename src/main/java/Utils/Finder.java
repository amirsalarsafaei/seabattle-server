package Utils;

import Models.Game.Coordinate;

import java.util.ArrayList;

public class Finder {

    public static boolean BoolFind(ArrayList<Coordinate> arrayList, Coordinate coordinate) {
        for (Coordinate tmp:arrayList) {
            if (tmp.x == coordinate.x && tmp.y == coordinate.y)
                return true;
        }
        return false;
    }
}
