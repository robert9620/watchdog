import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame {
    private int frameWidth = 340;
    private int frameHeight = 130;

    private JButton spoilButton;
    private JLabel watchDogTextField;
    private JLabel watchDogCounterTextField;
    private JLabel CPUTextField;
    private JLabel CPURestartedField;
    private JLabel CPUWorksField;
    private JLabel CPUWorksColor;

    public View(String name) throws HeadlessException {
        super(name);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setResizable(false);
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setLocation((screenWidth-frameWidth)/2, (screenHeight-frameHeight)/2);

        this.setSize(frameWidth, frameHeight);

        this.setLayout(null);

        spoilButton = new JButton("Zepsuj procesor");
        spoilButton.setSize(spoilButton.getPreferredSize());
        spoilButton.setLocation(100,70);
        spoilButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Psuje procesor");
                Main.spoilCPU();
            }
        });
        this.add(spoilButton);

        watchDogTextField = new JLabel("Watchdog:");
        watchDogTextField.setSize(watchDogTextField.getPreferredSize());
        watchDogTextField.setLocation(10,10);
        this.add(watchDogTextField);

        watchDogCounterTextField = new JLabel("10");
        watchDogCounterTextField.setSize(watchDogCounterTextField.getPreferredSize());
        watchDogCounterTextField.setLocation(10,30);
        this.add(watchDogCounterTextField);

        CPUTextField = new JLabel("CPU:");
        CPUTextField.setSize(CPUTextField.getPreferredSize());
        CPUTextField.setLocation(100,10);
        this.add(CPUTextField);

        CPURestartedField = new JLabel("Restartowany: 0");
        CPURestartedField.setSize(CPURestartedField.getPreferredSize());
        CPURestartedField.setLocation(100,30);
        this.add(CPURestartedField);

        CPUWorksField = new JLabel("Procesor działa?:");
        CPUWorksField.setSize(CPUWorksField.getPreferredSize());
        CPUWorksField.setLocation(220,10);
        this.add(CPUWorksField);

        CPUWorksColor = new JLabel("");
        CPUWorksColor.setSize(16,16);
        CPUWorksColor.setBackground(Color.green);
        CPUWorksColor.setOpaque(true);
        CPUWorksColor.setLocation(266,30);
        this.add(CPUWorksColor);

        this.setVisible(true);
    }

    void setCounter(int timer){
        watchDogCounterTextField.setText(String.valueOf(timer));
        watchDogCounterTextField.setSize(watchDogCounterTextField.getPreferredSize());
    }

    void setRestarted(int restarted){
        CPURestartedField.setText("Restartowany: "+ restarted);
        CPURestartedField.setSize(CPURestartedField.getPreferredSize());
    }

    public void setCPUWorksColor(Color color) {
        CPUWorksColor.setBackground(color);
    }
}
