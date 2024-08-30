/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam_lab7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMenu extends JFrame {
    private Steam steam;
    private JButton backButton;

    public AdminMenu() {
        steam = new Steam();

        setTitle("Menu de Administrador");
        setSize(400, 300);
        setLayout(new GridLayout(5, 1));

        JButton addGameButton = new JButton("Agregar Juego");
        JButton managePlayersButton = new JButton("Gestionar Jugadores");
        JButton reportsButton = new JButton("Generar Reportes");
        backButton = new JButton("Volver");

        add(addGameButton);
        add(managePlayersButton);
        add(reportsButton);
        add(backButton);

        addGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameForm(steam).setVisible(true);
            }
        });

        managePlayersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PlayerForm(steam, AdminMenu.this).setVisible(true);
            }
        });

        reportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String clientCodeStr = JOptionPane.showInputDialog("Ingrese el codigo del cliente:");
                try {
                    int clientCode = Integer.parseInt(clientCodeStr);
                    String fileName = JOptionPane.showInputDialog("Ingrese el nombre del archivo de reporte:");
                    steam.reportForClient(clientCode, fileName);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Codigo de cliente no valido", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginForm().setVisible(true);
                dispose();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
