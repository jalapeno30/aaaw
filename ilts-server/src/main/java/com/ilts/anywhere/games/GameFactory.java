package com.ilts.anywhere.games;

public class GameFactory {

    public static Game makeGame(GameType gameType) {
        Game game = null;
        switch (gameType) {
            case PCSO:
                game = new PCSOGame();
                break;
            case STANDARD:
                game = new StandardGame();
                break;

            default:
                break;
        }
        return game;
    }

}
