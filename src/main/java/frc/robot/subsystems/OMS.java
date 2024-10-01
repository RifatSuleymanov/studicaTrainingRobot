package frc.robot.subsystems;

import com.studica.frc.Servo;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class OMS extends SubsystemBase {
    private TitanQuad elevator; /** Переменная для привода лифта*/
    private Servo claw; /** Переменная для сервоприввода*/
    private TitanQuadEncoder elevatorEncoder; /** Переменная для эндкодера лифта*/

    private ShuffleboardTab tab = Shuffleboard.getTab("Training Robot");  /** Создание вкладки "Training Robot"*/
    private NetworkTableEntry elevatorEncoderValue = tab.add("Elevator Encoder", 0) /** Добавление записи для отображения значения эндкодера лифта*/
                    .getEntry();

    public OMS() {
        elevator = new TitanQuad(Constants.TITAN_ID, Constants.M2); /** Инициализация мотора лифта*/
        claw = new Servo(Constants.DIF_SERVO); /** Инициализция сервопривода*/

        elevatorEncoder = new TitanQuadEncoder(elevator, Constants.M2, Constants.ELEVATOR_DIST_TICK); /** Инициализация эндкодера для лифта*/
    }
    /** Метод для установки скорости мотора лифта*/
    public void setElevatorMotorSpeed(double speed) {
        elevator.set(speed);
    }
    /** Метод для получения расстояния, пройденного лифтом*/
    public double getElevatorEncoderDistance() {
        return elevatorEncoder.getEncoderDistance();
    }
    /** Метод для установки угла сервопривода*/
    public void setServoPosition(double degrees) {
        claw.setAngle(degrees);
    }
    /** Метод для сброса эндкодера лифта*/
    public void resetEncoders() {
        elevatorEncoder.reset();
    }
    /** Метод который выполняется переодический*/
    @Override
    public void periodic() {
        /**Обновляет значение эндкодера лифта на Shuffleboard*/
        elevatorEncoderValue.setDouble(getElevatorEncoderDistance() / 10);
    }
}