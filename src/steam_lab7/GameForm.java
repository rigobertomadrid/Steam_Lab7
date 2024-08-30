/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam_lab7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameForm extends JFrame {
    private Steam steam;
    private JTextField titleField;
    private JComboBox<String> osComboBox;
    private JTextField minAgeField;
    private JTextField priceField;
    private JTextField imagePathField;
    private JButton imageButton;
    private JButton saveButton;
    private JButton backButton;

    public GameForm(Steam steam) {
        this.steam = steam;

        setTitle("Agregar Juego");
        setSize(400, 300);
        setLayout(new GridLayout(7, 2));

        add(new JLabel("Titulo:"));
        titleField = new JTextField();
        add(titleField);

        add(new JLabel("Sistema Operativo:"));
        osComboBox = new JComboBox<>(new String[]{"Windows", "Mac", "Linux"});
        add(osComboBox);

        add(new JLabel("Edad Minima:"));
        minAgeField = new JTextField();
        add(minAgeField);

        add(new JLabel("Precio:"));
        priceField = new JTextField();
        add(priceField);

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
                    String title = titleField.getText();
                    String os = osComboBox.getSelectedItem().toString();
                    int minAge = Integer.parseInt(minAgeField.getText());
                    double price = Double.parseDouble(priceField.getText());
                    String imagePath = imagePathField.getText();

                    int gameCode = steam.getNextCode("game");
                    Game game = new Game(gameCode, title, os.charAt(0), minAge, price, imagePath);
                    steam.addGame(game);

                    JOptionPane.showMessageDialog(null, "Juego agregado exitosamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Entrada no valida", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
