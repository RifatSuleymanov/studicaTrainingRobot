package frc.robot.subsystems;

import com.studica.frc.Cobra;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CobraSensor extends SubsystemBase {

    private final Cobra cobra;
    private final ShuffleboardTab tab = Shuffleboard.getTab("Cobra");

    private final SimpleWidget cobraValue1 = tab.add("Cobra1", 0)
            .withWidget(BuiltInWidgets.kNumberBar);
    private final SimpleWidget cobraValue2 = tab.add("Cobra2", 0)
            .withWidget(BuiltInWidgets.kNumberBar);
    private final SimpleWidget cobraValue3 = tab.add("Cobra3", 0)
            .withWidget(BuiltInWidgets.kNumberBar);
    private final SimpleWidget cobraValue4 = tab.add("Cobra4", 0)
            .withWidget(BuiltInWidgets.kNumberBar);

    public CobraSensor() {
        cobra = new Cobra();

    }

    public double getCobra(int chanel) {
        return cobra.getRawValue(chanel);
    }

    @Override
    public void periodic() {
        cobraValue1.getEntry().setDouble(getCobra(0));
        cobraValue2.getEntry().setDouble(getCobra(1));
        cobraValue3.getEntry().setDouble(getCobra(2));
        cobraValue4.getEntry().setDouble(getCobra(3));
    }
}