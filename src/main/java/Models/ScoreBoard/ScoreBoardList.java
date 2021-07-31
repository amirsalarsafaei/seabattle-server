package Models.ScoreBoard;

import Models.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ScoreBoardList {
    ArrayList<ScoreBoardUser> ScoreBoardUser;

    public ScoreBoardList(ArrayList<User> users) {
        ScoreBoardUser = new ArrayList<>();
        for (User user : users) {
            ScoreBoardUser.add(new ScoreBoardUser(user));
        }
        Collections.sort(ScoreBoardUser, Collections.reverseOrder());
    }
}
