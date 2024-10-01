package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.driveCommands.SimpleDrive;

public class SCurveMovement extends AutoCommand {
    public SCurveMovement() {
        super(new SequentialCommandGroup(
                        new SimpleDrive(0.0, 0.5, 0.2).withTimeout(1.5), /** Влево с движением вперед*/
                        new SimpleDrive(0.0, 0.5, -0.2).withTimeout(1.5),/** Вправо с движением вперед// Вправо с движением вперед*/
                        new SimpleDrive(0.0, 0.5, 0.2).withTimeout(1.5), /** Влево с движением вперед*/
                        new SimpleDrive(0.0, 0.5, -0.2).withTimeout(1.5),/** Вправо с движением вперед// Вправо с движением вперед*/
                        new SimpleDrive(0.0, 0.5, 0.2).withTimeout(1.5), /** Влево с движением вперед*/
                        new SimpleDrive(0.0, 0.5, -0.2).withTimeout(1.5) /** Вправо с движением вперед// Вправо с движением вперед*/
                )
        );
    }
}