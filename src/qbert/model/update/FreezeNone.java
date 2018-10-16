package qbert.model.update;

import qbert.model.LevelStatus;
import qbert.model.characters.Player;
import qbert.model.components.MapComponent;
import qbert.model.components.PointComponent;
import qbert.model.components.TimerComponent;
import qbert.model.spawner.Spawner;

/**
 * Update Implementation.
 * In this implementation everything is updated.
 */
public class FreezeNone extends UpdateManager {

    /**
     * @param qbert Instance of {@link Qbert}
     * @param spawner Instance of {@link SpawnerImpl}
     * @param points Instance of {@link PointComponent}
     * @param map Instance of {@link MapComponent}
     * @param timer Instance of {@link TimerComponent}
     * @param status Instance of {@link MapComponent}
     */
    public FreezeNone(final Player qbert, final Spawner spawner, final PointComponent points, final MapComponent map, final TimerComponent timer,
            final LevelStatus status) {
        super(qbert, spawner, points, map, timer, status);
    }

    /**
     * @param elapsed the time passed since the last game cycle
     */
    public void update(final float elapsed) {
        this.getQbert().update(elapsed);
        this.getSpawner().getGameCharacters().stream().forEach(e -> {
            e.update(elapsed);
        });
        if (this.getSpawner().getCoily().isPresent()) {
            this.getSpawner().getCoily().get().update(elapsed);
        }

        this.updateCollisions(elapsed);

        this.updateQbert(elapsed);
        this.updateDisks(elapsed);
        this.updateEntities(elapsed);
    }
}
