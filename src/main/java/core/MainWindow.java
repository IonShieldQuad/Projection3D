package core;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow {
    private JPanel rootPanel;
    private JTextArea log;
    private JTextField lowerX;
    private JTextField upperX;
    private JButton displayButton;
    private Display3D graph;
    private JComboBox modelSel;
    private JComboBox modeSel;
    private JTextField camOffsetField;
    private JCheckBox xCheckBox;
    private JCheckBox yCheckBox;
    private JCheckBox zCheckBox;
    private JTextField offXField;
    private JTextField offYField;
    private JTextField offZField;
    private JTextField rotXField;
    private JTextField rotYField;
    private JTextField rotZField;
    private JTextField scaleXField;
    private JTextField scaleYField;
    private JTextField scaleZField;
    private JComboBox angleSel;
    private JTextField functionField;
    private JTextField lowerY;
    private JTextField upperY;
    private JTextField lowerZ;
    private JTextField upperZ;
    private JTextField Zsteps;
    private JTextField Xoffset;
    private JTextField Yoffset;
    private JTextField shiftX;
    private JTextField shiftY;
    private JCheckBox useCustomFunctionCheckBox;
    private JCheckBox displayHiddenCheckBox;
    private JTextField hiddenAlpha;
    
    private List<Model> models = new ArrayList<>();
    
    private MainWindow() {
        initComponents();
        models.add(Model.cube(100));
        models.add(Model.cube2(100));
    }
    
    private void initComponents() {
        displayButton.addActionListener(e -> display());
    }
    
    private void display() {
        try {
            graph.getModels().clear();
            
            double offX = Double.parseDouble(offXField.getText());
            double offY = Double.parseDouble(offYField.getText());
            double offZ = Double.parseDouble(offZField.getText());
            
            double rotX = Double.parseDouble(rotXField.getText());
            double rotY = Double.parseDouble(rotYField.getText());
            double rotZ = Double.parseDouble(rotZField.getText());
            
            double scaleX = Double.parseDouble(scaleXField.getText());
            double scaleY = Double.parseDouble(scaleYField.getText());
            double scaleZ = Double.parseDouble(scaleZField.getText());
            Transform3D transform = new Transform3D(new Point3D(offX, offY, offZ), new Point3D(Math.toRadians(rotX), Math.toRadians(rotY), Math.toRadians(rotZ)), new Point3D(scaleX, scaleY, scaleZ));
            
            graph.getModels().put(models.get(modelSel.getSelectedIndex()), transform);
            
            graph.setParallelMode(modeSel.getSelectedIndex() == 0);
            
            if (modeSel.getSelectedIndex() != 0) {
                graph.setAngleA(0);
                graph.setFactorD(Double.parseDouble(camOffsetField.getText()));
                graph.setWarpX(xCheckBox.isSelected());
                graph.setWarpY(yCheckBox.isSelected());
                graph.setWarpZ(zCheckBox.isSelected());
            }
            else {
                if (angleSel.getSelectedIndex() == 0) {
                    graph.setAngleA(Math.toRadians(30));
                    graph.setFactorL(0.5);
                }
                else {
                    graph.setAngleA(Math.toRadians(45));
                    graph.setFactorL(1);
                }
            }
            
            graph.repaint();
        }
        catch (NumberFormatException e) {
            log.append("Invalid format!\n");
        }
    }
    
    
    
    private void updateGraph() {
        graph.repaint();
    }
    
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Projection3D");
        MainWindow gui = new MainWindow();
        frame.setContentPane(gui.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
}
