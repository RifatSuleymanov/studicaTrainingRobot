package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.driveCommands.SimpleDrive;

public class DanceRoutine extends AutoCommand {
    public DanceRoutine() {
        super(new SequentialCommandGroup(
                        /** Вращение на месте*/
                        new SimpleDrive(0.0, 0.0, 0.5).withTimeout(1),  /** Поворот вправо*/
                        new SimpleDrive(0.0, 0.0, -0.5).withTimeout(1), /** Поворот влево*/
                        /** Вперед-назад*/
                        new SimpleDrive(0.0, 0.5, 0.0).withTimeout(1),  /** Вперед*/
                        new SimpleDrive(0.0, -0.5, 0.0).withTimeout(1), /** Назад*/
                        /** Зигзаги*/
                        new SimpleDrive(0.5, 0.0, 0.3).withTimeout(1),  /** Вперед вправо*/
                        new SimpleDrive(0.5, 0.0, -0.3).withTimeout(1), /** Вперед влево*/
                        /** Пауза и финальное вращение*/
                        new SimpleDrive(0.0, 0.0, 0.0).withTimeout(0.5), /** Пауза*/
                        new SimpleDrive(0.0, 0.0, 1.0).withTimeout(2)    /** Вращение на месте*/

                )
        );
    }
}