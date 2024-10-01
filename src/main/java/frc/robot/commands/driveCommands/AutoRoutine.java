package frc.robot.commands.driveCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.OMS;

public class AutoRoutine extends CommandBase {

    private final DriveTrain driveTrain = RobotContainer.driveTrain;
    private final OMS oms = RobotContainer.oms;
    private final Timer timer = new Timer();

    private enum AutoState {
        DRIVE_FORWARD,
        CLOSE_SERVO,
        LIFT_ELEVATOR,
        TURN_180,
        DRIVE_BACKWARD,
        LOWER_ELEVATOR_AND_OPEN_SERVO,
        LOWER_ELEVATOR_AT_END,
        FINISHED
    }

    private AutoState currentState;

    public AutoRoutine() {
        addRequirements(driveTrain, oms);
    }

    @Override
    public void initialize() {
        /** Отпускаем элеватор*/
        currentState = AutoState.LOWER_ELEVATOR_AND_OPEN_SERVO;
        timer.reset();
        timer.start();

        /** Сбрасываем значения эндкодеров*/
        driveTrain.resetEncoders();
        driveTrain.resetYaw();
    }

    @Override
    public void execute() {
        switch (currentState) {

            case LOWER_ELEVATOR_AND_OPEN_SERVO:
                oms.setElevatorMotorSpeed(0.5); /** Опускаем элеватор вниз*/
                oms.setServoPosition(0); /** Открываем сервопривод*/

                if (timer.get() > 3.0) { /** Ожидание, пока элеватор опустится (3 секунды)*/
                    oms.setElevatorMotorSpeed(0.0); /** Останавливаем элеватор*/
                    timer.reset();
                    currentState = AutoState.DRIVE_FORWARD; /** Переход к движению вперед*/
                }
                break;

            case DRIVE_FORWARD:
                if (timer.get() < 5.0) {
                    driveTrain.holonomicDrive(0.0, 0.5, 0.0); /** Двигаться вперед со скоростью 0.5*/
                } else {
                    driveTrain.holonomicDrive(0.0, 0.0, 0.0); /** Остановка после 5 секунд*/
                    oms.setServoPosition(150); /** Закрываем сервопривод*/
                    timer.reset();
                    currentState = AutoState.CLOSE_SERVO; /** Переход к закрытию сервопривода*/
                }
                break;

            case CLOSE_SERVO:
                if (timer.get() > 1.0) { /** Ждем 1 секунду, пока сервопривод полностью закроется*/
                    oms.setElevatorMotorSpeed(-0.5); /** Начинаем поднимать элеватор*/
                    timer.reset();
                    currentState = AutoState.LIFT_ELEVATOR; /** Переход к поднятию элеватора*/
                }
                break;

            case LIFT_ELEVATOR:
                if (timer.get() > 3.5) { /** Поднимаем элеватор в течение 2.5 секунд*/
                    oms.setElevatorMotorSpeed(0.0); /** Останавливаем элеватор*/
                    driveTrain.resetYaw(); /** Сбрасываем угол поворота*/
                    timer.reset();
                    currentState = AutoState.TURN_180; /** Переход к повороту*/
                }
                break;

            case TURN_180:
                if (timer.get() < 5.0) {
                    driveTrain.holonomicDrive(0.0, 0.0, 0.5); /** Поворачиваем в течение 5 секунд с угловой скоростью 0.5*/
                } else {
                    driveTrain.holonomicDrive(0.0, 0.0, 0.0); /** Останавливаем поворот*/
                    timer.reset();
                    currentState = AutoState.DRIVE_BACKWARD; /** Переход к движению назад*/
                }
                break;

                /*
                if (Math.abs(driveTrain.getYaw()) < 180) {
                    driveTrain.holonomicDrive(0.0, 0.0, 0.5);  Поворот на 180 градусов
                } else {
                    driveTrain.holonomicDrive(0.0, 0.0, 0.0);  Остановка после поворота
                    timer.reset();
                    currentState = AutoState.DRIVE_BACKWARD;  Переход к движению назад
                }
                break;
                */

            case DRIVE_BACKWARD:
                if (timer.get() < 5.0) {
                    driveTrain.holonomicDrive(0.0, 0.5, 0.0); /** Едем назад 5 секунд*/
                } else {
                    driveTrain.holonomicDrive(0.0, 0.0, 0.0); /** Остановка*/
                    timer.reset();
                    currentState = AutoState.LOWER_ELEVATOR_AT_END; /** Переход к опусканию элеватора перед завершением*/
                }
                break;

            case LOWER_ELEVATOR_AT_END:
                oms.setElevatorMotorSpeed(0.5); /** Опускаем элеватор вниз*/

                if (timer.get() > 3.5) { /** Ждем, пока элеватор полностью опустится и сервопривод откроется*/
                    oms.setElevatorMotorSpeed(0.0); /** Останавливаем элеватор*/
                    oms.setServoPosition(0); /** Открываем сервопривод*/
                    currentState = AutoState.FINISHED; /** Конец задачи*/
                }
                break;

            case FINISHED:
                break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        driveTrain.holonomicDrive(0.0, 0.0, 0.0); /** Остановка робота*/
        oms.setElevatorMotorSpeed(0.0); /** Остановка элеватора*/
    }

    @Override
    public boolean isFinished() {
        return currentState == AutoState.FINISHED;
    }
}