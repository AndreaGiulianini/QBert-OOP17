package qbert.model.components.graphics.animations;

import qbert.model.utilities.Position2D;

/**
 * Animation to move the character in a clockwise arc, increasing the angle. The Y-axis is
 * reversed so to increase y in the imaginary goniometric circle it must be decreased.
 */
public class ArcClockwiseAnimation extends MovementAnimationImpl {

    private final double radius;
    private final Position2D centerPos;

    private int currentAngle;
    private final int targetAngle;

    /**
     * @param startPos the first {@link Position2D}
     * @param targetPos the last {@link Position2D}
     * @param startAngle the first current angle value
     * @param targetAngle the last current angle value
     */
    public ArcClockwiseAnimation(final Position2D startPos, final Position2D targetPos, final int startAngle, final int targetAngle) {
        super(startPos, targetPos);

        //If the given target position has equals X coordinate then radius and centered is calculated on Y axis
        if (this.getCurrentPosition().getX() == targetPos.getX()) {
            this.radius = Math.abs(this.getCurrentPosition().getY() - targetPos.getY()) / 2;

            //If the target has an Y coordinate that is less than the current the center of the circumference is under the current position, above otherwise
            if (this.getCurrentPosition().getY() > this.getTargetPosition().getY()) {
                this.centerPos = new Position2D(this.getCurrentPosition().getX(), (int) (this.getCurrentPosition().getY() - this.radius));
            } else {
                this.centerPos = new Position2D(this.getCurrentPosition().getX(), (int) (this.getCurrentPosition().getY() + this.radius));
            }
        } else {
            this.radius = Math.abs(this.getCurrentPosition().getX() - targetPos.getX()) / 2;
            this.centerPos = new Position2D((int) (this.getCurrentPosition().getX() + this.radius), this.getCurrentPosition().getY());
        }

        this.currentAngle = startAngle;
        this.targetAngle = targetAngle;
    }

    @Override
    public final Position2D next() {
        this.currentAngle += 1;

        //Check if the angle limit has been reached
        if (this.currentAngle > this.targetAngle) {
            this.currentAngle = this.targetAngle;
            return this.getTargetPosition();
        } else {
            // Calculate position using the goniometric circumference
            return this.calculateCircumferenceCoords(this.centerPos, this.currentAngle, this.radius);
        }
    }
}
