/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam_lab7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createPlayerButton;
    private JButton exitButton;

    public LoginForm() {
        setTitle("Inicio de Sesion");
        setSize(300, 200);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2));

        panel.add(new JLabel("Usuario:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Contrasena:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Iniciar Sesion");
        panel.add(loginButton);

        createPlayerButton = new JButton("Crear Jugador");
        panel.add(createPlayerButton);

        exitButton = new JButton("Salir");
        panel.add(exitButton);

        add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("admin") && password.equals("admin")) {
                    new AdminMenu().setVisible(true);
                } else {
                    new UserMenu(username).setVisible(true);
                }
                dispose();
            }
        });

        createPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PlayerForm(new Steam(), LoginForm.this).setVisible(true);
                setVisible(false);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
