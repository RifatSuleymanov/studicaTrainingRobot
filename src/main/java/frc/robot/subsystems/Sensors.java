package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Sensors extends SubsystemBase {

    private final AnalogInput sharp_1;
    private final AnalogInput sharp_2;
    private final Ultrasonic sonar;
    private final ShuffleboardTab tab = Shuffleboard.getTab("Sensors");

    private final SimpleWidget sharp_1_Value = tab.add("Sharp_1", 0)
            .withWidget(BuiltInWidgets.kNumberBar);

    private final SimpleWidget sharp_2_Value = tab.add("Sharp_2", 0)
            .withWidget(BuiltInWidgets.kNumberBar);
    private final SimpleWidget sonarValue = tab.add("Sonar", 0)
            .withWidget(BuiltInWidgets.kNumberBar);

    public Sensors() {
        sharp_1 = new AnalogInput(Constants.SHARP1_ANALOG_CHANEL);
        sharp_2 = new AnalogInput(Constants.SHARP2_ANALOG_CHANEL);
        sonar = new Ultrasonic(Constants.PING_CHANEL, Constants.ECHO_CHANEL);
        sonar.setAutomaticMode(true);
    }

    private double convertVoltageToDistance(double voltage) {
        if (voltage > 0) {
            return (27.86 * Math.pow(voltage, -1.15));
        } else {
            return 0;
        }
    }

    public double getDistance() {
        return sonar.getRangeMM();
    }

    public Ultrasonic getSonar() {
        return sonar;
    }

    public AnalogInput getSharp_1() {
        return sharp_1;
    }

    public AnalogInput getSharp_2() {
        return sharp_2;
    }

    public double getDistanceSharp(AnalogInput sharp) {
        double voltage_2 = sharp.getVoltage();
        return convertVoltageToDistance(voltage_2);
    }

    @Override
    public void periodic() {
        sharp_1_Value.getEntry().setDouble(getDistanceSharp(sharp_1));
        sharp_2_Value.getEntry().setDouble(getDistanceSharp(sharp_2));
        sonarValue.getEntry().setDouble(getDistance() / 10);
    }
}