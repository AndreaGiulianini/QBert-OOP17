package qbert.model.components.graphics.animations;

import qbert.model.utilities.Position2D;

/**
 * A very rapid animation to move an object from a {@link Position2D} to another instantly.
 */
public class DisplaceAnimation extends MovementAnimationImpl {

    /**
     * @param startPos the first {@link Position2D}
     * @param targetPos the last {@link Position2D}
     */
    public DisplaceAnimation(final Position2D startPos, final Position2D targetPos) {
        super(startPos, targetPos);
    }

    @Override
    public final Position2D next() {
        return this.getTargetPosition();
    }
}
