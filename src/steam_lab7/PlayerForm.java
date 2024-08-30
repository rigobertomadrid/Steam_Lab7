/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam_lab7;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

public class PlayerForm extends JFrame {
    private Steam steam;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField nameField;
    private JTextField birthYearField;
    private JTextField imagePathField;
    private JButton imageButton;
    private JButton dateButton;
    private JButton saveButton;
    private JButton backButton;
    private JFrame previousFrame;

    public PlayerForm(Steam steam, JFrame previousFrame) {
        this.steam = steam;
        this.previousFrame = previousFrame;

        setTitle("Crear Jugador");
        setSize(400, 350);
        setLayout(new GridLayout(7, 2));

        add(new JLabel("Usuario:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Contrasena:"));
        passwordField = new JTextField();
        add(passwordField);

        add(new JLabel("Nombre:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Fecha de Nacimiento:"));
        birthYearField = new JTextField();
        birthYearField.setEditable(false);
        add(birthYearField);

        dateButton = new JButton("Seleccionar Fecha");
        add(dateButton);

        add(new JLabel("Ruta de Imagen:"));
        imagePathField = new JTextField();
        imagePathField.setEditable(false);
        add(imagePathField);

        imageButton = new JButton("Seleccionar Imagen");
        add(imageButton);

        saveButton = new JButton("Guardar");
        add(saveButton);

        backButton = new JButton("Volver");
        add(backButton);

        dateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDateChooser dateChooser = new JDateChooser();
                int result = JOptionPane.showConfirmDialog(null, dateChooser, "Seleccionar Fecha de Nacimiento", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    Date selectedDate = dateChooser.getDate();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(selectedDate);
                    birthYearField.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
                }
            }
        });

        imageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    imagePathField.setText(fileChooser.getSelectedFile().getPath());
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = usernameField.getText();
                    String password = passwordField.getText();
                    String name = nameField.getText();
                    String birthdate = birthYearField.getText();
                    String imagePath = imagePathField.getText();
                    UserType userType = UserType.NORMAL;

                    Calendar birthDate = Calendar.getInstance();
                    String[] dateParts = birthdate.split("-");
                    int year = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]) - 1;
                    int day = Integer.parseInt(dateParts[2]);
                    birthDate.set(year, month, day);

                    int playerCode = steam.getNextCode("player");
                    Player player = new Player(playerCode, username, password, name, birthDate, imagePath, userType);
                    steam.addPlayer(player);

                    JOptionPane.showMessageDialog(null, "Jugador agregado exitosamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    if (previousFrame != null) {
                        previousFrame.setVisible(true);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Entrada no valida", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (previousFrame != null) {
                    previousFrame.setVisible(true);
                }
                dispose();
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
