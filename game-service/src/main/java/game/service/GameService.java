package game.service;

import game.world.AppContext;

/**
 * Created by Gary on 2015/4/30.
 */
public class GameService {
    public static void main(String[] args) {
        new AppContext(new String[]{"spring*.xml"});
    }
}
