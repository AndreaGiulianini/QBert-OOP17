package qbert.model.spawner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import qbert.controller.Controller;
import qbert.model.characters.Character;
import qbert.model.characters.CharactersList;
import qbert.model.characters.Player;
import qbert.model.characters.Snake;
import qbert.model.characters.states.DeathState;
import qbert.model.characters.states.SpawnState;
import qbert.model.utilities.Dimensions;
import qbert.model.utilities.Position2D;

/**
 * The implementation of {@link Spawner}.
 */
public final class SpawnerImpl implements Spawner {

    private final Player qbert;
    private Optional<Snake> coily = Optional.empty();
    private List<Character> gameCharacters;
    private final EnemyFactory ef;
    private final Map<CharactersList, EnemyInfoImpl> mapInfo;

    /**
     * @param mapInfo the map of the characters
     * @param qBertSpeed the player speed
     * @param controller the game {@link Controller}
     * @param qbertLives number of lives the {@link Player} is starting the level with
     */
    public SpawnerImpl(final Map<CharactersList, EnemyInfoImpl> mapInfo, final float qBertSpeed, final Controller controller, final int qbertLives) {
        this.gameCharacters = new ArrayList<>();
        this.mapInfo = mapInfo;
        this.ef = new EnemyFactoryImpl(controller);
        this.qbert = ef.createQbert(qBertSpeed, qbertLives);
    }

    @Override
    public Player spawnQbert() {
        return this.qbert;
    }

    @Override
    public void respawnQbert() {
        this.qbert.setNextPosition(new Position2D(this.qbert.getCurrentPosition()));
        this.qbert.setCurrentState(new SpawnState(this.qbert));
    }

    @Override
    public void update(final float dt) {
        for (final Map.Entry<CharactersList, EnemyInfoImpl> entry : mapInfo.entrySet()) {
            if (entry.getValue().getSpawningTime() <= entry.getValue().getElapsedTime()) {
                entry.getValue().resetElapsedTime();
                if (entry.getValue().getCurrentQuantity() < entry.getValue().getTotalQuantity()) {
                    final Character character;
                    switch (entry.getKey()) {
                    case COILY:
                        final Snake snake = ef.createCoily(entry.getValue().getSpeed(), entry.getValue().getStandingTime(), this.qbert);
                        snake.setCurrentPosition(new Position2D(Dimensions.UNDEFINED_POSITION));
                        this.coily = Optional.of(snake);
                        break;
                    case RED_BALL:
                        character = ef.createRedBall(entry.getValue().getSpeed(), entry.getValue().getStandingTime());
                        character.setCurrentPosition(new Position2D(Dimensions.UNDEFINED_POSITION));
                        this.gameCharacters.add(character);
                        break;
                    case GREEN_BALL:
                        character = ef.createGreenBall(entry.getValue().getSpeed(), entry.getValue().getStandingTime());
                        character.setCurrentPosition(new Position2D(Dimensions.UNDEFINED_POSITION));
                        this.gameCharacters.add(character);
                        break;
                    case UGG:
                        character = ef.createUgg(entry.getValue().getSpeed(), entry.getValue().getStandingTime());
                        character.setCurrentPosition(new Position2D(Dimensions.UNDEFINED_POSITION));
                        this.gameCharacters.add(character);
                        break;
                    case WRONGWAY:
                        character = ef.createWrongway(entry.getValue().getSpeed(), entry.getValue().getStandingTime());
                        character.setCurrentPosition(new Position2D(Dimensions.UNDEFINED_POSITION));
                        this.gameCharacters.add(character);
                        break;
                    case SAM_AND_SLICK:
                        character = ef.createSamAndSlick(entry.getValue().getSpeed(), entry.getValue().getStandingTime());
                        character.setCurrentPosition(new Position2D(Dimensions.UNDEFINED_POSITION));
                        this.gameCharacters.add(character);
                        break;
                    default:
                    }
                    entry.getValue().incCurrentQuantity();
                }
            } else {
                entry.getValue().incElapsedTime(dt);
            }
        }
    }

    @Override
    public void death(final Character character) {
        final String name = character.getClass().getSimpleName();

        if (this.mapInfo.get(CharactersList.getEnumConstantByValue(name)) != null && this.mapInfo.get(CharactersList.getEnumConstantByValue(name)).getCurrentQuantity() > 0) {
            this.mapInfo.get(CharactersList.getEnumConstantByValue(name)).decCurrentQuantity();
        }
    }

    @Override
    public List<Character> getGameCharacters() {
        return Collections.unmodifiableList(this.gameCharacters);
    }

    @Override
    public Optional<Snake> getCoily() {
        return this.coily;
    }

    @Override
    public void killAll() {
        this.updateGameCharacters(this.getGameCharacters().stream().peek(e -> {
            e.setCurrentState(new DeathState(e));
            this.death(e);
        }).filter(e -> !e.isDead()).collect(Collectors.toList()));
        this.killCoily();
    }

    @Override
    public void killCoily() {
        if (this.coily.isPresent()) {
            this.coily.get().setCurrentState(new DeathState(this.coily.get()));
            this.death(this.coily.get());
            this.coily = Optional.empty();
        }
    }

    @Override
    public void updateGameCharacters(final List<Character> gc) {
        this.gameCharacters = gc;
    }
}
