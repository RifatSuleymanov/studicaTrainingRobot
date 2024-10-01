package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrain extends SubsystemBase {
    private final TitanQuad leftMotor; /** Левый мотор*/
    private final TitanQuad rightMotor;   /** Правый мотор*/
    private final TitanQuad backMotor;    /** Задний мотор*/
    private AnalogInput sharp;
    private final TitanQuadEncoder leftEncoder; /** Левый эндкодер*/
    private final TitanQuadEncoder rightEncoder; /** Правый эндкодер*/
    private final TitanQuadEncoder backEncoder; /** Задний эндкодер*/
    private final AHRS navx; /** Гироскоп NavX*/

    private final ShuffleboardTab tab = Shuffleboard.getTab("Training Robot");
    private final NetworkTableEntry leftEncoderValue = tab.add("Left Encoder", 0)
            .getEntry();                                    /** Создания записи для левого эндкодера*/
    private final NetworkTableEntry rightEncoderValue = tab.add("Right Encoder", 0)
                    .getEntry();                                    /** Создания записи для правого эндкодера*/
    private final NetworkTableEntry backEncoderValue = tab.add("Back Encoder", 0)
                    .getEntry();                                    /** Создания записи для заднего эндкодера*/
    private final NetworkTableEntry gyroValue = tab.add("NavX Yaw", 0)
                    .getEntry();                                    /** Создания записи для эндкодера гироскопа*/

    public DriveTrain() {
        leftMotor = new TitanQuad(Constants.TITAN_ID, Constants.M1); /** Инициаизация левого мотора с использованием ID и канала */
        rightMotor = new TitanQuad(Constants.TITAN_ID, Constants.M0); /** Инициаизация правого мотора с использованием ID и канала */
        backMotor = new TitanQuad(Constants.TITAN_ID, Constants.M3); /** Инициаизация заднего мотора с использованием ID и канала */

        leftEncoder = new TitanQuadEncoder(leftMotor, Constants.M1, Constants.WHEEL_DIST_PER_TICK); /** Инициаизация левого эндкодера */
        rightEncoder = new TitanQuadEncoder(rightMotor, Constants.M0, Constants.WHEEL_DIST_PER_TICK); /** Инициаизация правого эндкодера */
        backEncoder = new TitanQuadEncoder(backMotor, Constants.M3, Constants.WHEEL_DIST_PER_TICK); /** Инициаизация заднего эндкодера */

        navx = new AHRS(SPI.Port.kMXP); /** Инициализация гироскопа NavX с использованием порта SPI*/
    }

    public void setLeftMotorSpeed(double speed) {
        leftMotor.set(speed); /** Устанавливает скорость левого мотора*/
    }

    public void setRightMotorSpeed(double speed) {
        rightMotor.set(speed); /** Устанавливает скорость правого мотора*/
    }

    public void setBackMotorSpeed(double speed) {
        backMotor.set(speed); /** Устанавливает скорость заднего мотора*/
    }

    /** Устанавливает скорость мотора*/
    public void setDriveMotorSpeeds(double leftSpeed, double rightSpeed, double backSpeed) {
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
        backMotor.set(backSpeed);
    }
    /**Управляет движением робота в холономической модели управления. Холономическое движение позволяет роботу перемещаться в любом направлении,
     * включая боковое двиение и вращение, без необходимости изменять ориентацию самого робота
     */
    public void holonomicDrive(double x, double y, double z) {
        /** Вычисление скорости для каждого мотора на основе входных значений x, y, z*/
        double rightSpeed = ((x / 3) - (y / Math.sqrt(3)) + z) * Math.sqrt(3);
        double leftSpeed = ((x / 3) + (y / Math.sqrt(3)) + z) * Math.sqrt(3);
        double backSpeed = (-2 * x / 3) + z;

        double max = Math.abs(rightSpeed); /** Определение максимальной скорости для нормализации*/
        if (Math.abs(leftSpeed) > max) max = Math.abs(leftSpeed); /** Обновление максимальной скорости, если небходимо*/
        if (Math.abs(backSpeed) > max) max = Math.abs(backSpeed); /** Обновление максимальной скорости, если небходимо*/

        if (max > 1) /** Проверка, превышает ли максимальная скорость 1*/
        {
            rightSpeed /= max; /** Нормализация скоростей*/
            leftSpeed /= max; /** Нормализация скоростей*/
            backSpeed /= max; /** Нормализация скоростей*/
        }
        /**Установка скорости моторов*/
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
        backMotor.set(backSpeed);
    }
    /** Возвращает растояние, пройденное левым приводом, в миллиметрах*/
    public double getLeftEncoderDistance() {
        return leftEncoder.getEncoderDistance() * -1;
    }
    /** Возвращает растояние, пройденное правым приводом, в миллиметрах с измененным знаком*/
    public double getRightEncoderDistance() {
        return rightEncoder.getEncoderDistance() * 1;
    }
    /** Возвращает растояние, пройденное задним приводом, в миллиметрах*/
    public double getBackEncoderDistance() {
        return backEncoder.getEncoderDistance();
    }
    /** Возвращает среднее растояние, пройденное левым и правым приводом, в миллиметрах*/
    public double getAverageForwardEncoderDistance() {
        return (getLeftEncoderDistance() + getRightEncoderDistance()) / 2;
    }
    /** Возврашает текущий угол наклона с гироскопа NavX в градусах ( в диапазоне от -180* до 180*)*/
    public double getYaw() {
        return navx.getYaw();
    }
    /** Сбрасывает значений эндкодеров*/
    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
        backEncoder.reset();
    }
    /** Сбрасывает значение угла наклона гироскопа NavX*/
    public void resetYaw() {
        navx.zeroYaw();
    }

    public double getDistanceSharp() {
        return (Math.pow(sharp.getAverageVoltage(), -1.2045)) * 27.726;
    }
    /** Обновляет значение эндкодера на Shuffleboard*/
    @Override
    public void periodic() {
        leftEncoderValue.setDouble(getLeftEncoderDistance() / 10);
        rightEncoderValue.setDouble(getRightEncoderDistance() / 10);
        backEncoderValue.setDouble(getBackEncoderDistance() / 10);
        gyroValue.setDouble(getYaw());
    }
}