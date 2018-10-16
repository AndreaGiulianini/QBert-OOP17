package qbert.model.characters.states;

import qbert.model.characters.Character;

/**
 * A simple {@link CharacterState} used to advise that the relative {@link Character} is landed after a jump or spawn.
 */
public class LandState extends CharacterStateImpl {

    /**
     * @param character {@link Character} associated with this {@link CharacterState} 
     */
    public LandState(final Character character) {
        super(character);
        this.getCharacter().getGraphicComponent().setStandingAnimation();
    }

    @Override
    public final void update(final float dt) {
        this.getCharacter().getGraphicComponent().updateGraphics(dt);
    }
}
