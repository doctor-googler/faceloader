package me.hutkovich.faceloader.main;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame jframe = new JFrame();
      jframe.setLayout(new BorderLayout());
      JPanel panel = new MainForm().getPanel();
      panel.setVisible(true);
      jframe.add(panel, BorderLayout.CENTER);
      jframe.setVisible(true);
      jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      jframe.pack();
    });
  }
}
