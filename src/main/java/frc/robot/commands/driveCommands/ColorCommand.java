package frc.robot.commands.driveCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ColorSubsystem;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.io.IOException;

public class ColorCommand extends CommandBase {
    private static final ColorSubsystem colorSubsystem = RobotContainer.colorSubsystem;
    private NetworkTable visionTable;
    private Process pythonProcess; // Переменная для хранения запущенного процесса
    private boolean isRunning = false; // Статус работы скрипта

    public ColorCommand() {
        addRequirements(colorSubsystem);
        visionTable = NetworkTableInstance.getDefault().getTable("VisionRgb");

        // Добавление кнопки в Shuffleboard
        SmartDashboard.putData("Toggle Python Script", new CommandBase() {
            @Override
            public void initialize() {
                togglePythonScript(); // Вызов метода при нажатии на кнопку
            }

            @Override
            public boolean isFinished() {
                return true; // Завершаем команду сразу после выполнения
            }
        });
    }

    @Override
    public void initialize() {
        SmartDashboard.putString("Robot Log", "Color Command Initialized.");
    }

    @Override
    public void execute() {
        // Считываем данные RGB из NetworkTables
        String rgbValues = visionTable.getEntry("rgb_values").getString("rgb{0,0,0}"); // Получаем RGB значения
        SmartDashboard.putString("RGB Raw Values", rgbValues); // Логируем сырые значения

        // Проверяем, что массив содержит три элемента
        String[] rgbArray = rgbValues.replace("rgb{", "").replace("}", "").split(",");
        if (rgbArray.length == 3) {
            try {
                int red = Integer.parseInt(rgbArray[0].trim());
                int green = Integer.parseInt(rgbArray[1].trim());
                int blue = Integer.parseInt(rgbArray[2].trim());

                // Обновляем ColorSubsystem с полученными значениями
                colorSubsystem.updateColorData(red, green, blue);

                // Обновляем SmartDashboard
                SmartDashboard.putString("RGB Output", String.format("R: %d, G: %d, B: %d", red, green, blue));
            } catch (NumberFormatException e) {
                SmartDashboard.putString("Robot Log", "Ошибка парсинга RGB данных: " + e.getMessage());
            }
        } else {
            SmartDashboard.putString("Robot Log", "Неверный формат RGB данных: " + rgbValues);
        }
    }



    public void togglePythonScript() {
        if (!isRunning) {
            startPythonScript(); // Запускаем скрипт, если он не запущен
        } else {
            stopPythonScript(); // Останавливаем скрипт, если он уже запущен
        }
    }

    private void startPythonScript() {
        try {
            ProcessBuilder pb = new ProcessBuilder("python3", "/home/pi/color_capture.py");
            pythonProcess = pb.start();
            isRunning = true; // Обновляем статус
            SmartDashboard.putString("Robot Log", "Python script started.");
        } catch (IOException e) {
            SmartDashboard.putString("Robot Log", "Failed to start Python script: " + e.getMessage());
        }
    }

    private void stopPythonScript() {
        if (pythonProcess != null) {
            pythonProcess.destroy(); // Останавливаем процесс
            SmartDashboard.putString("Robot Log", "Python script stopped.");
            isRunning = false; // Обновляем статус
            pythonProcess = null; // Обнуляем ссылку на процесс
        }
    }

    @Override
    public boolean isFinished() {
        return false; // Команда продолжается до принудительного завершения
    }
}
