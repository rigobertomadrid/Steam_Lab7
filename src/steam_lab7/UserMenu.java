/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam_lab7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserMenu extends JFrame {
    private Steam steam;
    private String username;
    private JButton backButton;

    public UserMenu(String username) {
        this.steam = new Steam();
        this.username = username;

        setTitle("Menu de Usuario");
        setSize(400, 300);
        setLayout(new GridLayout(4, 1));

        JButton viewGamesButton = new JButton("Ver Juegos");
        JButton downloadGameButton = new JButton("Descargar Juego");
        JButton profileButton = new JButton("Ver Perfil");
        backButton = new JButton("Volver");

        add(viewGamesButton);
        add(downloadGameButton);
        add(profileButton);
        add(backButton);

        viewGamesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Game> games = steam.getAllGames();
                for (Game game : games) {
                    StringBuilder gameInfo = new StringBuilder();
                    gameInfo.append("Codigo: ").append(game.getCode()).append("\n")
                            .append("Titulo: ").append(game.getTitle()).append("\n")
                            .append("Sistema Operativo: ").append(game.getOperatingSystem()).append("\n")
                            .append("Edad Minima: ").append(game.getMinAge()).append("\n")
                            .append("Precio: $").append(game.getPrice()).append("\n");

                    ImageIcon gameIcon = new ImageIcon(game.getImagePath());
                    Image image = gameIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    gameIcon = new ImageIcon(image);

                    JOptionPane.showMessageDialog(null, gameInfo.toString(), "Detalles del Juego", JOptionPane.INFORMATION_MESSAGE, gameIcon);
                }
            }
        });

        downloadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player player = steam.getPlayerByUsername(username);
                if (player == null) {
                    JOptionPane.showMessageDialog(null, "Error: Jugador no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String gameCodeStr = JOptionPane.showInputDialog("Ingrese el codigo del juego:");
                String osStr = JOptionPane.showInputDialog("Ingrese el sistema operativo (Windows/Mac/Linux):");

                try {
                    int gameCode = Integer.parseInt(gameCodeStr);
                    char os = osStr.toUpperCase().charAt(0);

                    if (steam.downloadGame(gameCode, player.getCode(), os)) {
                        JOptionPane.showMessageDialog(null, "Descarga exitosa", "Exito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Descarga fallida", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Codigo no valido", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player player = steam.getPlayerByUsername(username);
                if (player == null) {
                    JOptionPane.showMessageDialog(null, "Error: Jugador no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                StringBuilder profileInfo = new StringBuilder();
                profileInfo.append("Nombre: ").append(player.getName()).append("\n")
                        .append("Usuario: ").append(player.getUsername()).append("\n")
                        .append("Contrasena: ").append(player.getPassword()).append("\n")
                        .append("Descargas: ").append(player.getDownloadsCount()).append("\n")
                        .append("Juegos Descargados:\n");

                List<Game> downloadedGames = steam.getDownloadedGames(player.getCode());
                for (Game game : downloadedGames) {
                    profileInfo.append(" - ").append(game.getTitle()).append("\n");
                }

                JOptionPane.showMessageDialog(null, profileInfo.toString(), "Mi Perfil", JOptionPane.INFORMATION_MESSAGE);
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
