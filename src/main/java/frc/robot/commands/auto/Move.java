package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.util.Units;

public class Move {

  private Translation2d translation;
  private boolean isReference;

  public Move() {
    this(0, 0);
  }

  public Move(double x, double y) {
    translation = new Translation2d(Units.inchesToMeters(x), Units.inchesToMeters(y));
    isReference = false;
  }

  public Move markAsReference() {
    isReference = true;
    return this;
  }

  private Move translate(double x, double y) {
    if (!isReference) {
      translation =
          translation.plus(new Translation2d(Units.inchesToMeters(x), Units.inchesToMeters(y)));
    } else {
      throw new RuntimeException("Unable to modify a reference Move!");
    }
    return this;
  }

  public Move forward(double distance) {
    return translate(distance, 0);
  }

  public Move backward(double distance) {
    return translate(-distance, 0);
  }

  public Move left(double distance) {
    return translate(0, distance);
  }

  public Move right(double distance) {
    return translate(0, -distance);
  }

  public Move copy() {
    return new Move(
        Units.metersToInches(translation.getX()), Units.metersToInches(translation.getY()));
  }

  public Translation2d get() {
    return translation;
  }

  public Pose2d get(double headingDegrees) {
    return new Pose2d(get(), Rotation2d.fromDegrees(headingDegrees));
  }
}
