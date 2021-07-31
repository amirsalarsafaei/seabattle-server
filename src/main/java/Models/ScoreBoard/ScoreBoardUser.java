package Models.ScoreBoard;

import Models.User;

import java.time.LocalDateTime;

public class ScoreBoardUser implements Comparable<ScoreBoardUser> {
    public String username;
    public Integer points;
    public boolean Online;
    public ScoreBoardUser(User user) {
        if (user.lastOnline != null)
            Online = LocalDateTime.now().isBefore(user.lastOnline.plusSeconds(5));
        else
            Online = false;
        points = user.wins - user.loses;
        username = user.getUsername();
    }
    @Override
    public int compareTo(ScoreBoardUser scoreBoardUser) {
        return this.points.compareTo(scoreBoardUser.points);
    }
}
