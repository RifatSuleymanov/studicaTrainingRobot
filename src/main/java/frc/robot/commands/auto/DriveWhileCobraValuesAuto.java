package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.driveCommands.DriveWhileCobraValuesAbove;

public class DriveWhileCobraValuesAuto extends AutoCommand {
    public DriveWhileCobraValuesAuto() {
        super(new SequentialCommandGroup(
                        new DriveWhileCobraValuesAbove()
                )
        );
    }
}
