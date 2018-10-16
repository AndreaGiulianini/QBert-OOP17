package qbert.model.characters.states;

import qbert.model.characters.Character;

/**
 * A simple {@link CharacterState} used to advise that the associated {@link Character} is death.
 */
public class DeathState extends CharacterStateImpl {

    /**
     * @param character {@link Character} associated with this {@link CharacterState} 
     */
    public DeathState(final Character character) {
        super(character);
        character.getGraphicComponent().setDeathAnimation();
        character.setDead(true);
    }

    @Override
    public final void update(final float dt) {
        this.getCharacter().getGraphicComponent().updateGraphics(dt);
    }
}
