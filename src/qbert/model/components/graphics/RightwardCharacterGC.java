package qbert.model.components.graphics;

import qbert.model.components.graphics.animations.ComposedAnimation;
import qbert.model.components.graphics.animations.StandingAnimation;
import qbert.model.components.graphics.animations.StraightMovementAnimation;
import qbert.model.sprites.OneSideCharacterSprites;
import qbert.model.utilities.Dimensions;
import qbert.model.utilities.Position2D;

/**
 * GC stands graphic component, this implementation is used to manage characters whose movements are unidirectional and 
 * rightward, doesn't support leftward animations.
 */
public class RightwardCharacterGC extends CharacterGCImpl {

    private final int jumpWidth = Dimensions.getCubeHeight();
    private final int jumpHeight = Dimensions.getCubeWidth() / 2;

    private final Position2D landPos;

    /**
     * A boolean to set true when the character is facing up.
     */
    private boolean up;

    private final OneSideCharacterSprites sprites;

    /**
     * @param sprites the sprites container
     * @param startSpritePos the first position (physic) of the {@link Character}
     */
    public RightwardCharacterGC(final OneSideCharacterSprites sprites, final Position2D startSpritePos) {
        super(sprites.getStandSprite(), startSpritePos);
        this.sprites = sprites;
        this.landPos = new Position2D(Dimensions.getBackgroundPos().getX() - this.jumpHeight, this.getSpawnPosition().getY()); 
        this.up = true;
    }

    @Override
    public final void setDeathAnimation() {
        this.setStandingAnimation();
    }

    @Override
    public final void setStandingAnimation() {
        this.setSprite(this.sprites.getStandSprite());
        if (!this.up) {
            this.flipOnXImage();
        }
        this.setCurrentAnimation(new StandingAnimation(this.getPosition()));
    }

    @Override
    public final void setSpawnAnimation() {
        this.setSprite(this.sprites.getMoveSprite());
        if (!this.up) {
            this.flipOnXImage();
        }
        this.setCurrentAnimation(new StraightMovementAnimation(this.getSpawnPosition(), this.landPos));
    }

    @Override
    public final void setFallAnimation() {
        this.setSprite(this.sprites.getMoveSprite());
        if (!this.up) {
            this.flipOnXImage();
        }
        this.setCurrentAnimation(new StraightMovementAnimation(this.getPosition(), 
                new Position2D(Dimensions.getWindowWidth() + this.getSpriteWidth(), this.getPosition().getY())));
    }

    @Override
    public final void setMoveDownLeftAnimation() {
        this.setSprite(this.sprites.getMoveSprite());
        this.up = false;
        this.flipOnXImage();
        this.setCurrentAnimation(new ComposedAnimation.JumpDownLeftRightward(this.getPosition(), 
                new Position2D(this.getPosition().getX() + this.jumpHeight, this.getPosition().getY() - this.jumpWidth)));
    }

    @Override
    public final void setMoveDownRightAnimation() {
        this.setSprite(this.sprites.getMoveSprite());
        this.up = true;
        this.setCurrentAnimation(new ComposedAnimation.JumpDownRightRightward(this.getPosition(), 
                new Position2D(this.getPosition().getX() + this.jumpHeight, this.getPosition().getY() + this.jumpWidth)));
    }
}
