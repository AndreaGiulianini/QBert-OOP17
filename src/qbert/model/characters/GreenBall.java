package qbert.model.characters;

import qbert.model.characters.states.DeathState;
import qbert.model.components.PointComponent;
import qbert.model.components.PointComponentImpl;
import qbert.model.components.TimerComponent;
import qbert.model.components.TimerComponentImpl;
import qbert.model.utilities.Position2D;
import qbert.model.components.graphics.CharacterGC;

/**
 * This class models a green ball that moves like a {@link RedBall} but can be catched
 * by Qbert to freeze enemies on moving and spawning. 
 */
public class GreenBall extends DownwardCharacter {

    /**
     * @param startPos the first {@link Position2D} of the {@link Character} in the map
     * @param speed the {@link Character} movement speed
     * @param graphics the {@link Character}'s {@link CharacterGC}
     * @param standingTime the time passed on standing state
     */
    public GreenBall(final Position2D startPos, final Float speed, final CharacterGC graphics, final Integer standingTime) {
        super(startPos, speed, graphics, standingTime);
    }

    @Override
    protected final void collide(final Player qbert, final PointComponent points, final TimerComponent timer) {
        timer.freezeEntities(TimerComponentImpl.GREEN_BALL_FREEZE_TIME);
        points.score(PointComponentImpl.KILL_GREEN_BALL_SCORE, qbert);
        this.setCurrentState(new DeathState(this));
    }
}
