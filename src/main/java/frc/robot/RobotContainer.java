package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.Teleop;
import frc.robot.commands.TeleopOMS;
import frc.robot.commands.auto.AutoCommand;
import frc.robot.commands.auto.DriveForwardLeft;
import frc.robot.commands.driveCommands.ColorCommand;
import frc.robot.commands.driveCommands.VisionCommand;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.*;

import java.util.HashMap;
import java.util.Map;

public class RobotContainer {
    public static DriveTrain driveTrain;
    public static OMS oms;
    public static OI oi;
    public static Sensors sensors;
    public static CobraSensor cobraSensor;
    public static SendableChooser<String> autoChooser;
    public static Map<String, AutoCommand> autoMode = new HashMap<>();

    public static VisionSubsystem vision;
    public static ColorSubsystem colorSubsystem;
    public static ColorCommand colorCommand;

    // Добавляем менеджер камеры
    private final DepthCamera camera;

    public RobotContainer() {
        driveTrain = new DriveTrain();
        oms = new OMS();
        oi = new OI();
        sensors = new Sensors();
        cobraSensor = new CobraSensor();

        driveTrain.setDefaultCommand(new Teleop());
        oms.setDefaultCommand(new TeleopOMS());

        vision = new VisionSubsystem();
        vision.setDefaultCommand(new VisionCommand());

        colorSubsystem = new ColorSubsystem();
        colorCommand = new ColorCommand();

        camera = new DepthCamera();
    }

    public Command getAutonomousCommand() {
        String mode = autoChooser.getSelected();
        return autoMode.getOrDefault(mode, new DriveForwardLeft());
    }
}