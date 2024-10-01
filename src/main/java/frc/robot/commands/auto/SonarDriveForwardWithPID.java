package frc.robot.commands.auto;

import frc.robot.commands.driveCommands.SonarDriveWithPID;

public class SonarDriveForwardWithPID extends AutoCommand {
    public SonarDriveForwardWithPID() {
        super(new SonarDriveWithPID(100000, 5, 0, 1).withTimeout(5));
    }
}