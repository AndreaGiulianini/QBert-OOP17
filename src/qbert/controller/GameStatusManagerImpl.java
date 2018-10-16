package qbert.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import qbert.model.scenes.Game;
import qbert.model.scenes.GameOver;
import qbert.model.scenes.Introduction;
import qbert.model.scenes.Menu;
import qbert.model.scenes.Model;
import qbert.model.scenes.Ranking;

/**
 * The implementation of {@link GameStatusManager}.
 */
public class GameStatusManagerImpl implements GameStatusManager {

    private final Map<GameStatus, Model> models;
    private GameStatus currentGameStatus;

    /**
     * @param firstGameStatus the first {@link GameStatus} of the application
     * @param controller the game {@link Controller} to be passed to models
     */
    public GameStatusManagerImpl(final GameStatus firstGameStatus, final Controller controller) {
        this.models = new HashMap<>();

        this.models.put(GameStatus.INTRODUCTION, new Introduction(controller));
        this.models.put(GameStatus.MENU, new Menu(controller));
        this.models.put(GameStatus.RANKING, new Ranking(controller));
        this.models.put(GameStatus.GAMEPLAY, new Game(controller));
        this.models.put(GameStatus.GAMEOVER, new GameOver(controller));

        this.currentGameStatus = firstGameStatus;

        if (!this.models.keySet().equals(GameStatus.getAll())) {
            final String errorMessage = "Program aborted. Not all the game status have been initialized.";
            Logger.getGlobal().log(Level.SEVERE, errorMessage);
            controller.forceQuit(errorMessage);
        }
    }

    @Override
    public final Model getModel() {
        return this.models.get(this.currentGameStatus);
    }

    @Override
    public final GameStatus getCurrentStatus() {
        return this.currentGameStatus;
    }

    @Override
    public final void setCurrentStatus(final GameStatus newGameStatus) {
        this.currentGameStatus = newGameStatus;
        this.getModel().initialize();
    }
}
