package frc.robot.commands.driveCommands;

import com.studica.frc.Cobra;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Sensors;

public class SonarDriveWithPID extends CommandBase {
    /**
     * Статическая переменна DriveTrain
     */
    private static final DriveTrain drive = RobotContainer.driveTrain;
    private final Sensors sensors = RobotContainer.sensors;
    private final Timer timer = new Timer();
    private final Ultrasonic sonar = sensors.getSonar();
    private final AnalogInput sharp_1 = sensors.getSharp_1();
    private final AnalogInput sharp_2 = sensors.getSharp_2();

    /**
     * Переменная дистанции
     */
    private final double setpointDistance;
    /**
     * Переменная угла поворота
     */
    private final double setpointYaw;
    /**
     * PID-контроллеры
     */
    PIDController pidYAxis;
    PIDController pidZAxis;
    /**
     * Начальное значение предполагает, что робот уже остановился
     */
    private boolean hasStopped = false;
    private boolean hasTurned = false;
    /**
     * Конструктор класса
     */
    public SonarDriveWithPID(double setpointDistance, double epsilonDistance, double setpointYaw, double epsilonYaw) {
        /** Установка целевого значения дистанции*/
        this.setpointDistance = setpointDistance;

        /** Установка целевого значения угла поворота*/
        this.setpointYaw = setpointYaw;
        addRequirements(drive);

        /** Инициализация PID-контроллера для управления движением вперед назад*/
        pidYAxis = new PIDController(1, 0, 0);
        /** Устанвка допустимой погрешности для дистанции*/
        pidYAxis.setTolerance(epsilonDistance);

        /** Инициализация PID-контроллера для управления поворотом*/
        pidZAxis = new PIDController(0.1, 0, 0);
        /** Установка допустимой погрешности для угла поворота*/
        pidZAxis.setTolerance(epsilonYaw);
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        /** Сброс энкодеров для начала нового отсчета дистанции*/
        drive.resetEncoders();
        /** Сброс эндкодера для начала нового отсчета угла поворота*/
        drive.resetYaw();
        /** Сброс PID-контроллера для движения*/
        pidYAxis.reset();
        /** Сброс PID-контроллера для поворота*/
        pidZAxis.reset();
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        double dis = sonar.getRangeMM() / 10;
        double sharp_1_distance = sensors.getDistanceSharp(sharp_1);
        double sharp_2_distance = sensors.getDistanceSharp(sharp_2);

        if (dis > 20) {
            /** Движение вперед/назад и поворот при расстоянии больше 20*/
            drive.holonomicDrive(
                    0.0,
                    MathUtil.clamp(pidYAxis.calculate(drive.getAverageForwardEncoderDistance(), setpointDistance), -0.5, 0.5),
                    MathUtil.clamp(pidZAxis.calculate(drive.getYaw(), setpointYaw), -1, 1)
            );
            /** Сброс состояния остановки и поворота*/
            hasStopped = false;
            hasTurned = false;

        } else {
            if (!hasStopped) {
                /** Остановка робота*/
                drive.holonomicDrive(0.0, 0.0, 0.0);
                hasStopped = true; /** Устанавливаем состояние остановки*/
                hasTurned = false; /** Устанавливаем состояние поворота в начальное значение*/
            } else if (!hasTurned) {
                /** Оценка данных Sharp и принятие решения о повороте*/
                if (sharp_1_distance > 20) {
                    /** Поворот направо*/
                    drive.holonomicDrive(0.0, 0.0, 0.5); /** Пример скорости поворота, замените на нужное значение*/
                    if (Math.abs(drive.getYaw() - (drive.getYaw() + 90)) < 1) {
                        drive.holonomicDrive(0.0, 0.0, 0.0);
                        hasTurned = true;
                    }
                } else if (sharp_2_distance > 20) {
                    /** Поворот влево*/
                    drive.holonomicDrive(0.0, 0.0, -0.5); /** Пример скорости поворота, замените на нужное значение*/
                    if (Math.abs(drive.getYaw() - (drive.getYaw() - 90)) < 1) {
                        drive.holonomicDrive(0.0, 0.0, 0.0);
                        hasTurned = true;
                    }
                } else {
                    /** Полная остановка при отсутствии подходящих условий*/
                    drive.holonomicDrive(0.0, 0.0, 0.0);
                }
            }
        }
    }
    /**
     * Остановка двигателей робота
     */
    @Override
    public void end(boolean interrupted) {
        drive.setDriveMotorSpeeds(0.0, 0.0, 0.0);
    }
    /**
     * Возвращает true если цель по дистанции достигнута
     */
    @Override
    public boolean isFinished() {
        return pidYAxis.atSetpoint();
    }
}