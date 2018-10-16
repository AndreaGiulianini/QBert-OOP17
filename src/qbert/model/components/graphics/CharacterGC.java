package qbert.model.components.graphics;

import qbert.model.components.graphics.animations.MovementAnimation;
import qbert.model.utilities.Position2D;

/**
 * GC stands for graphic component. This interface extends {@link GraphicComponent} to 
 * manage the animations of a {@link Character}. It provides the most generic functionalities.
 */
public interface CharacterGC extends GraphicComponent {

    /**
     * @return the current {@link Character} spawn position in the space
     */
    Position2D getSpawnPosition();

    /**
     * @param newPos the new sprite spawn position ({@link Position2D}) in the space
     */
    void setSpawnPosition(Position2D newPos);

    /**
     * @return the current {@link MovementAnimation}
     */
    MovementAnimation getCurrentAnimation();

    /**
     * @param animation the new animation to set
     */
    void setCurrentAnimation(MovementAnimation animation);

    /**
     * Set the {@link Character} relative death animation.
     */
    void setDeathAnimation();

    /**
     * Set the {@link Character} relative standing animation.
     */
    void setStandingAnimation();

    /**
     * Set the {@link Character} relative spawning animation.
     */
    void setSpawnAnimation();

    /**
     * Set the {@link Character} relative falling animation.
     */
    void setFallAnimation();

    /**
     * Set the {@link Character} relative moving down-left animation.
     */
    void setMoveDownLeftAnimation();

    /**
     * Set the {@link Character} relative moving down-right animation.
     */
    void setMoveDownRightAnimation();

    /**
     * A method to flip on Y axis the current sprite.
     */
    void flipOnYImage();

    /**
     * A method to flip on X axis the current sprite.
     */
    void flipOnXImage();

    /**
     * This function is called to update the graphics, e.g., the current {@link MovementAnimation}.
     * @param graphicsSpeed the graphics' speed calculated from the time passed since the last game cycle 
     */
    void updateGraphics(float graphicsSpeed);

}
