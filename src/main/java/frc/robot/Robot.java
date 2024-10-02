package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.auto.*;

public class Robot extends TimedRobot {
    private RobotContainer robotContainer;
    private Command autonomousCommand;

    // Добавляем CameraManager

    @Override
    public void robotInit() {
        // Инициализируем сервер сетевых таблиц
        NetworkTableInstance.getDefault().startServer();
        NetworkTableInstance.getDefault().startClientTeam(1234);

        // Инициализируем контейнер робота
        robotContainer = new RobotContainer();

        // Инициализация CameraManager
    }

    @Override
    public void robotPeriodic() {
        // Запускаем планировщик команд
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
        // Настраиваем авто-выбор
        if (null == RobotContainer.autoChooser) {
            RobotContainer.autoChooser = new SendableChooser<>();
        }
        RobotContainer.autoChooser.setDefaultOption("Drive Forward", "Drive Forward");
        RobotContainer.autoMode.put("Drive Forward Left", new DriveForwardLeft());
        addAutoMode(RobotContainer.autoChooser, "Sonar DriveForward With PID", new SonarDriveForwardWithPID());
        addAutoMode(RobotContainer.autoChooser, "Zme", new SCurveMovement());
        addAutoMode(RobotContainer.autoChooser, "FigureEight", new FigureEight());
        addAutoMode(RobotContainer.autoChooser, "DanceRoutine", new DanceRoutine());
        addAutoMode(RobotContainer.autoChooser, "AutoRoutine", new AutoRoutineDrive());
        addAutoMode(RobotContainer.autoChooser, "DriveWhileCobraValuesAuto", new DriveWhileCobraValuesAuto());

        SmartDashboard.putData(RobotContainer.autoChooser);
    }

    public void addAutoMode(SendableChooser<String> chooser, String auto, AutoCommand cmd) {
        chooser.addOption(auto, auto);
        RobotContainer.autoMode.put(auto, cmd);
    }

    @Override
    public void disabledPeriodic() {
        // Показываем выбранный режим авто на SmartDashboard
        String mode = RobotContainer.autoChooser.getSelected();
        SmartDashboard.putString("Chosen Auto Mode", mode);
    }

    @Override
    public void autonomousInit() {
        // Инициализируем команду для автономного режима
        autonomousCommand = robotContainer.getAutonomousCommand();
        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }
    }

    @Override
    public void teleopInit() {
        // Отменяем автономную команду при переходе в телепериод
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
    }

    @Override
    public void teleopPeriodic() {

    }
}