package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.driveCommands.SimpleDrive;

public class FigureEight extends AutoCommand {
    public FigureEight() {
        super(new SequentialCommandGroup(
                        new SimpleDrive(0.3, 0.3, 0.5).withTimeout(4), /** Половина круга влево*/
                        new SimpleDrive(0.3, 0.3, -0.5).withTimeout(4), /** Половина круга вправо*/
                        new SimpleDrive(0.3, 0.3, -0.5).withTimeout(4), /** Половина круга вправо*/
                        new SimpleDrive(0.3, 0.3, 0.5).withTimeout(4) /** Половина круга влево*/
                )
        );
    }
}