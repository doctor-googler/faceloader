package me.hutkovich.faceloader.main;

import me.hutkovich.faceloader.logic.DeviceCommander;
import me.hutkovich.faceloader.logic.exception.DeviceCommanderException;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MainForm {
  private JTable devicesList;
  private JButton button1;
  private JButton exitButton;
  private JPanel panel;
  private JScrollPane devicesListPane;
  private DeviceCommander commander;

  public MainForm() {
    try {
      commander = new DeviceCommander();
    } catch (DeviceCommanderException e) {
      e.printStackTrace();
    }
    DefaultTableModel model = (DefaultTableModel) devicesList.getModel();
    model.setColumnCount(2);
    //devicesList
    button1.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        try {
          commander.getDevicesID().forEach(deviceSerial->{
            model.addRow(new Object[]{deviceSerial, "Android Device"});
          });
        } catch (DeviceCommanderException e1) {
          e1.printStackTrace();
        }
      }
    });
    exitButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        System.exit(0);
      }
    });
  }

  public JPanel getPanel() {
    return panel;
  }

  public JScrollPane getDevicesListPane() {
    return devicesListPane;
  }
}
