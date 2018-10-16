package qbert.model.scenes;

import java.util.ArrayList;
import java.util.List;

import qbert.controller.Controller;
import qbert.controller.GameStatus;
import qbert.model.Level;
import qbert.model.LevelImpl;
import qbert.model.LevelSettings;
import qbert.model.characters.Player;
import qbert.model.characters.states.MoveState;
import qbert.model.utilities.Position2D;
import qbert.model.components.graphics.Renderable;
import qbert.model.components.sounds.GameSC;
import qbert.model.components.sounds.SoundComponent;

/**
 * The implementation of {@link Model} for gameplay logic.
 */
public class Game implements Model {

    private static final int LEVELSNUMBER = 9;
    private static final int ROUNDSNUMBER = 4;

    private Level gameLevel;
    private final Controller controller;

    private final SoundComponent sounds;

    private int levelNumber;
    private int roundNumber;
    private int lives;
    private int score;

    private final GUILogic levelAndRound;
    private final GUILogic scoreAndLives;
    private final List<GUILogic> gui;

    /**
     * @param controller the application controller
     */
    public Game(final Controller controller) {
        this.controller = controller;
        this.sounds = new GameSC(this.controller);
        this.gui = new ArrayList<>();
        this.levelAndRound = new GUILogicImpl(TextPosition.RIGHTSIDE);
        this.scoreAndLives = new GUILogicImpl(TextPosition.LEFTSIDE);
        this.gui.add(this.levelAndRound);
        this.gui.add(this.scoreAndLives);
    }

    @Override
    public final void initialize() {
        this.levelNumber = 1;
        this.roundNumber = 1;
        this.lives = 3;
        this.score = 0;

        this.createNewLevel();
        this.updateScoreAndLives();
        this.gameLevel.addObserver(this);

        this.sounds.setGameStartSound();
    }

    @Override
    public final void update(final float elapsed) {
        this.gameLevel.update(elapsed);

        this.updateScoreAndLives();

        if (this.hasFinished()) {
            this.controller.setScore(this.score);
            this.controller.changeScene(GameStatus.GAMEOVER);
        }
    }

    /**
     * The method used by the current {@link Level} to advise that must be instantiated a new level.
     */
    public final void changeRound() {
        if (this.roundNumber >= ROUNDSNUMBER && this.levelNumber < LEVELSNUMBER) {
            this.roundNumber = 1;
            this.levelNumber++;
        } else if (this.roundNumber < ROUNDSNUMBER && this.levelNumber <= LEVELSNUMBER) {
            this.roundNumber++;
        }

        this.createNewLevel();
    }

    @Override
    public final List<Renderable> getRenderables() {
        if (this.gameLevel != null) {
            return this.gameLevel.getRenderables();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public final List<GUILogic> getGUI() {
        if (this.gameLevel != null) {
            return this.gui;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public final void moveDown() {
        final Player qbert = this.gameLevel.getQBert();
        if (!qbert.isMoving() && !qbert.isDead()) {
            qbert.setNextPosition(
                    new Position2D(qbert.getCurrentPosition().getX() - qbert.getStep(), qbert.getCurrentPosition().getY() - qbert.getStep()));

            qbert.setCurrentState(new MoveState.DownLeft(qbert));
        }
    }

    @Override
    public final void moveLeft() {
        final Player qbert = this.gameLevel.getQBert();
        if (!qbert.isMoving() && !qbert.isDead()) {
            qbert.setNextPosition(
                    new Position2D(qbert.getCurrentPosition().getX() - qbert.getStep(), qbert.getCurrentPosition().getY() + qbert.getStep()));

            qbert.setCurrentState(new MoveState.UpLeft(qbert));
        }
    }

    @Override
    public final void moveRight() {
        final Player qbert = this.gameLevel.getQBert();
        if (!qbert.isMoving() && !qbert.isDead()) {
            qbert.setNextPosition(
                new Position2D(qbert.getCurrentPosition().getX() + qbert.getStep(), qbert.getCurrentPosition().getY() - qbert.getStep()));

            qbert.setCurrentState(new MoveState.DownRight(qbert));
        }
    }

    @Override
    public final void moveUp() {
        final Player qbert = this.gameLevel.getQBert();
        if (!qbert.isMoving() && !qbert.isDead()) {
            qbert.setNextPosition(
                    new Position2D(qbert.getCurrentPosition().getX() + qbert.getStep(), qbert.getCurrentPosition().getY() + qbert.getStep()));

            qbert.setCurrentState(new MoveState.UpRight(qbert));
        }
    }

    @Override
    public final void confirm() {
        this.changeRound();
    }

    @Override
    public final boolean hasFinished() {
        return this.lives <= 0;
    }

    /**
     * A method used to create and initialize a new level. It also updates the GUI.
     */
    private void createNewLevel() {
        this.levelAndRound.removeAllData();
        this.levelAndRound.addData("LEVEL: " + this.levelNumber);
        this.levelAndRound.addData("ROUND: " + this.roundNumber);

        final LevelSettings ls = controller.getLevelSettings(this.levelNumber, this.roundNumber);
        this.gameLevel = new LevelImpl(ls, lives, score, this.controller);
        this.gameLevel.addObserver(this);
    }

    /**
     * A method used to update local variables (and GUI) from level changes.
     */
    private void updateScoreAndLives() {
        this.score = this.gameLevel.getPoints();
        this.lives = this.gameLevel.getLives();

        this.scoreAndLives.removeAllData();
        this.scoreAndLives.addData("SCORE: " + this.score);
        this.scoreAndLives.addData("LIVES: " + this.lives);
        this.scoreAndLives.addData("CHANGE COLOR TO:");
    }
}
