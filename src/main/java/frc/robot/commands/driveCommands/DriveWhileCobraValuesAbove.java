package frc.robot.commands.driveCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.CobraSensor;
import frc.robot.subsystems.DriveTrain;

public class DriveWhileCobraValuesAbove extends CommandBase {
    private static final DriveTrain drive = RobotContainer.driveTrain;
    private final CobraSensor cobra = RobotContainer.cobraSensor;
    private final double threshold;

    public DriveWhileCobraValuesAbove() {
        threshold = 2000;
        addRequirements(drive);
    }

    private boolean areAllCobraValuesAboveThreshold() {
        return cobra.getCobra(0) > threshold &&
                cobra.getCobra(1) > threshold &&
                cobra.getCobra(2) > threshold &&
                cobra.getCobra(3) > threshold;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        /** Двигаем робота вперед, пока все значения от Cobra больше порога*/
        if (areAllCobraValuesAboveThreshold()) {
            drive.holonomicDrive(0.0, 0.1, 0.0);
        } else {
            drive.holonomicDrive(0, 0,0);/** Останавливаемся, если одно из значений меньше порога*/
        }
    }

    @Override
    public boolean isFinished() {
        return !areAllCobraValuesAboveThreshold(); /** Завершаем команду, если одно из значений ниже порога*/
    }

    @Override
    public void end(boolean interrupted) {
        drive.holonomicDrive(0, 0,0); /** Останавливаем робота при завершении команды*/
    }
}