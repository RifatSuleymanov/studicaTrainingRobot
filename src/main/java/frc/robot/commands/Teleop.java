package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.DriveTrain;

public class Teleop extends CommandBase {
    /**
     * Подключаем классы DriveTrain и OI
     */
    private static final DriveTrain driveTrain = RobotContainer.driveTrain;
    private static final OI oi = RobotContainer.oi;
    /**
     * Входные данные от джойстика
     */
    double inputLeftY = 0;
    /**
     * Переменная для хранения значений Y (вперед/назад) левого джойстика
     */
    double inputLeftX = 0;
    /**
     * Переменная для хранения значений Х (влево/вправо) левого джойстика
     */
    double inputRightX = 0;
    /**
     * Переменные для управления плавностью
     */
    double deltaLeftY = 0;
    double deltaLeftX = 0;
    double deltaRightX = 0;
    double prevLeftY = 0;
    double prevLeftX = 0;
    /**
     * Переменные для управления моторами и хранения для скорости каждого мотора
     */
    double leftMotor = 0;
    double rightMotor = 0;
    double backMotor = 0;
    /**
     * Для хранения максимальной скорости
     */
    double max = 0;
    /**
     * Константы для плавного увеличения скорости
     */
    private static final double RAMP_UP = 0.05;
    /**
     * Константы для плавного уменьшения скорости
     */
    private static final double RAMP_DOWN = 0.05;
    /**
     * Константа, задающая предел изменения скорости
     */
    private static final double DELTA_LIMIT = 0.075;

    public Teleop() {
        addRequirements(driveTrain);
    }

    /**
     * Код здесь будет выполнен один раз, когда команда будет вызвана в первый раз
     */
    @Override
    public void initialize() {
        /** Сбрасывает эндкодеры*/
        driveTrain.resetEncoders();
        driveTrain.resetYaw();
    }

    /**
     * Код здесь будет выполнятся непрерывно в каждом цикле робота, пока команда не будет остановлена
     */
    @Override
    public void execute() {
        /** Получение данных от джойстика*/
        inputLeftX = oi.getLeftDriveX();
        inputLeftY = -oi.getLeftDriveY(); /** Отрицательный для правильного направления*/

        /** Управление плавностью изменения значения*/
        deltaLeftX = inputLeftX - prevLeftX;
        deltaLeftY = inputLeftY - prevLeftY;

        /** Плавное увелечение скорости*/
        if (deltaLeftX >= DELTA_LIMIT)
            inputLeftX += RAMP_UP;
        else if (deltaLeftX <= -DELTA_LIMIT)
            inputLeftX -= RAMP_DOWN;
        if (deltaLeftY >= DELTA_LIMIT)
            inputLeftY += RAMP_UP;
        else if (deltaLeftY <= -DELTA_LIMIT)
            inputLeftY -= RAMP_DOWN;

        /** Обновление предыдущих значений джойстиков*/
        prevLeftY = inputLeftY;
        prevLeftX = inputLeftX;

        /** Холономическое преоброзование*/
        driveTrain.holonomicDrive(inputLeftX, inputLeftY, inputRightX); /** Управляет движением робота на основе полученных значений*/
    }

    @Override
    public void end(boolean interrupted) {
        driveTrain.setDriveMotorSpeeds(0.0, 0.0, 0.0); /** Останавливает все моторы*/
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}