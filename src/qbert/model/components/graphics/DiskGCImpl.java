package qbert.model.components.graphics;

import java.awt.image.BufferedImage;
import java.util.Map;

import qbert.model.utilities.Position2D;

/**
 * The implementation of {@link DiskGC}.
 */
public class DiskGCImpl extends ReversibleTileGC implements DiskGC {

    private int timeBuffer;
    private final int speed;

    /**
     * @param spritePos the {@link Position2D} of the sprite
     * @param sprites the set of sprites to be shown
     * @param speed the time in milliseconds to change from a sprite to another.
     */
    public DiskGCImpl(final Position2D spritePos, final Map<Integer, BufferedImage> sprites, final int speed) {
        super(sprites);
        this.setPosition(spritePos);
        this.timeBuffer = 0;
        this.speed = speed;
    }

    @Override
    public final void update(final float elapsedTime) {
        this.timeBuffer += elapsedTime;
        if ((this.timeBuffer) > speed) {
            this.timeBuffer = 0;
            this.setNextSprite();
        }
    }
}
