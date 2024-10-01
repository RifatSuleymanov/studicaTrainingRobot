package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.driveCommands.SimpleDrive;

public class DriveForwardLeft extends AutoCommand {
    public DriveForwardLeft() {
        super(new SequentialCommandGroup(
                        new SimpleDrive(0.0, 0.5, 0.0).withTimeout(3), /** Проехать 50 см вперед*/
                        new SimpleDrive(0.0, 0.0, -0.5).withTimeout(1.5), /** Повернуть налево (90 градусов)*/
                        new SimpleDrive(0.0, 0.5, 0.0).withTimeout(3),/** Проехать 50 см вперед*/
                        new SimpleDrive(0.0, 0.0, -0.5).withTimeout(1.5),/** Повернуть налево (90 градусов)*/
                        new SimpleDrive(0.0, 0.5, 0.0).withTimeout(3),
                        new SimpleDrive(0.0, 0.0, -0.5).withTimeout(1.5),/** Повернуть налево (90 градусов)*/
                        new SimpleDrive(0.0, 0.5, 0.0).withTimeout(3)/** Проехать еще 50 см вперед*//** Проехать еще 50 см вперед*/
                )
        );
    }
}