package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ColorSubsystem extends SubsystemBase {
    private NetworkTableInstance inst = NetworkTableInstance.getDefault();
    private NetworkTable table = inst.getTable("VisionRgb");
    private NetworkTableEntry rgbEntry;

    public ColorSubsystem() {
        rgbEntry = table.getEntry("RGB");
    }

    // Метод для обновления данных цвета
    public void updateColorData(int r, int g, int b) {
        rgbEntry.setDoubleArray(new double[]{r, g, b});
    }

    @Override
    public void periodic() {
    }
}