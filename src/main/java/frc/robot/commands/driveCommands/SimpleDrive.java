package frc.robot.commands.driveCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;

public class SimpleDrive extends CommandBase {
    /**
     * Переменная DriveTrain
     */
    private static final DriveTrain driveTrain = RobotContainer.driveTrain;
    /**
     * Переменная для хранения значений движений по осям X, Y, и угла поворота
     */
    private final double x;
    private final double y;
    private final double z;

    /**
     * Конструктор принимающий значения для движения
     */
    public SimpleDrive(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        addRequirements(driveTrain);
    }

    @Override
    public void initialize() {
        /** Сбрасываем значения эндкодеров*/
        driveTrain.resetEncoders();
        driveTrain.resetYaw();

    }

    /**
     * Метод, выполняющийся при каждом вызове команды
     * Управляет движением робота с использованием метода holonomicDrive
     * Передает значения для движение по осям Х У и угла поворота
     */
    @Override
    public void execute() {
        driveTrain.holonomicDrive(x, y, z);
    }

    /**
     * Метод вызываемый при завершении команды
     * Остановка всех двигателей робота
     */
    @Override
    public void end(boolean interrupted) {
        driveTrain.setDriveMotorSpeeds(0.0, 0.0, 0.0);
    }

    /**
     * Метод проверяет завершена ли команда
     * Комана никогда не считается завершенной (действует до тех пор, пока не будет прервана)
     */
    @Override
    public boolean isFinished() {
        return false;
    }
}