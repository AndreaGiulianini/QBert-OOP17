package qbert.model;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import qbert.controller.Controller;
import qbert.model.characters.Player;
import qbert.model.components.MapComponent;
import qbert.model.components.MapComponentImpl;
import qbert.model.components.PointComponent;
import qbert.model.components.PointComponentImpl;
import qbert.model.components.TimerComponent;
import qbert.model.components.TimerComponentImpl;
import qbert.model.spawner.Spawner;
import qbert.model.spawner.SpawnerImpl;
import qbert.model.utilities.Dimensions;
import qbert.model.utilities.Position2D;
import qbert.model.components.graphics.GenericGC;
import qbert.model.components.graphics.GraphicComponent;
import qbert.model.components.graphics.GraphicComponentImpl;
import qbert.model.components.graphics.Renderable;
import qbert.model.components.graphics.RenderableObject;
import qbert.model.components.sounds.GameSC;
import qbert.model.components.sounds.SoundComponent;
import qbert.model.scenes.Game;

/**
 * Implementation of interface {@link Level}.
 * Manages classes that keeps information about the current game
 *
 */
public final class LevelImpl implements Level {

    private static final String USER_MESSAGE = "Application aborted. Please look at log file for more information.";

    private final Player qbert;
    private final Spawner spawner;
    private final PointComponent points;
    private final TimerComponent timer;
    private final LevelStatus status;
    private final Renderable background;
    private final LevelSettings settings;
    private MapComponent map;

    /**
     * Constructor of class LevelImpl.
     * @param levelSettings Set of settings that determine how to build the level
     * @param lives Number of lives the player is starting the level with
     * @param score Amount of points the player is starting the level with
     * @param controller Instance of controller
     */
    public LevelImpl(final LevelSettings levelSettings, final int lives, final int score, final Controller controller) {
        this.settings = levelSettings;
        final SoundComponent sounds = new GameSC(controller);
        this.spawner = new SpawnerImpl(levelSettings.getMapInfo(), levelSettings.getQBertSpeed(), controller, lives);
        this.qbert = this.spawner.spawnQbert();
        this.points = new PointComponentImpl(score);

        try {
            this.map = new MapComponentImpl(settings);
        } catch (IOException e) {
            Logger.getGlobal().log(java.util.logging.Level.SEVERE, e.getMessage(), e);
            controller.forceQuit(USER_MESSAGE);
        }

        this.status = new LevelStatusImpl(levelSettings, qbert, spawner, points, map, sounds);

        this.timer = new TimerComponentImpl(qbert, spawner, points, map, status);

        final GraphicComponent backgroundGC = new GenericGC(this.settings.getBackgroundImage(), 
                new Position2D(Dimensions.getBackgroundPos().getX(), Dimensions.getBackgroundPos().getY()));
        this.background = new RenderableObject(backgroundGC);
    }

    @Override
    public void addObserver(final Game gameObserver) {
        this.status.addObserver(gameObserver);
    }

    @Override
    public void notifyEndLevel() {
        this.status.notifyEndLevel();
    }

    @Override
    public Player getQBert() {
        return this.qbert;
    }

    @Override
    public int getPoints() {
        return this.points.getPoints();
    }

    @Override
    public int getLives() {
        return qbert.getLivesNumber();
    }

    @Override
    public List<Renderable> getRenderables() {
        Stream<Renderable> resultingStream = Stream.of(
                Stream.of(this.getTargetColor()),
                Stream.of(this.background),
                Stream.of(this.qbert),
                map.getTileList().stream(),
                map.getDiskList().stream(),
                spawner.getGameCharacters().stream()
        ).flatMap(i -> i);

        if (spawner.getCoily().isPresent()) {
            resultingStream = Stream.concat(resultingStream, Stream.of(spawner.getCoily().get()));
        }
        return resultingStream.collect(Collectors.toList());
    }

    @Override
    public void update(final float elapsed) {
        timer.update(elapsed);
    }

    private Renderable getTargetColor() {
        final Optional<Integer> i = settings.getColorMap().keySet().stream().collect(Collectors.toList()).stream().max((o1, o2) -> o1.compareTo(o2));
        if (i.isPresent()) {
            final GraphicComponent gc = new GraphicComponentImpl(settings.getColorMap().get(i.get()), new Position2D(Math.round(Dimensions.getWindowWidth() / 9f), Math.round(Dimensions.getWindowHeight() / 4f)));
            return new RenderableObject(gc);
        }

        return null;
    }
}
