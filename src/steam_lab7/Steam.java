/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam_lab7;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;

public class Steam {
    private RandomAccessFile codesFile;
    private RandomAccessFile gamesFile;
    private RandomAccessFile playersFile;

    public Steam() {
        try {
            File steamFolder = new File("steam");
            if (!steamFolder.exists()) {
                steamFolder.mkdir();
            }

            File downloadsFolder = new File(steamFolder, "downloads");
            if (!downloadsFolder.exists()) {
                downloadsFolder.mkdir();
            }

            codesFile = new RandomAccessFile(new File(steamFolder, "codes.stm"), "rw");
            gamesFile = new RandomAccessFile(new File(steamFolder, "games.stm"), "rw");
            playersFile = new RandomAccessFile(new File(steamFolder, "player.stm"), "rw");

            if (codesFile.length() == 0) {
                codesFile.writeInt(1);
                codesFile.writeInt(1);
                codesFile.writeInt(1);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error inicializando sistema Steam: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int getNextCode(String type) {
        try {
            codesFile.seek(0);
            int gameCode = codesFile.readInt();
            int playerCode = codesFile.readInt();
            int downloadCode = codesFile.readInt();

            switch (type) {
                case "game":
                    codesFile.seek(0);
                    codesFile.writeInt(gameCode + 1);
                    return gameCode;
                case "player":
                    codesFile.seek(4);
                    codesFile.writeInt(playerCode + 1);
                    return playerCode;
                case "download":
                    codesFile.seek(8);
                    codesFile.writeInt(downloadCode + 1);
                    return downloadCode;
                default:
                    throw new IllegalArgumentException("Tipo de codigo invalido");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error leyendo o actualizando codigo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }

    public void addGame(Game game) {
        try {
            gamesFile.seek(gamesFile.length());
            gamesFile.writeInt(game.getCode());
            gamesFile.writeUTF(game.getTitle());
            gamesFile.writeChar(game.getOperatingSystem());
            gamesFile.writeInt(game.getMinAge());
            gamesFile.writeDouble(game.getPrice());
            gamesFile.writeInt(game.getDownloadsCount());
            gamesFile.writeUTF(game.getImagePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error agregando juego: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addPlayer(Player player) {
        try {
            playersFile.seek(playersFile.length());
            playersFile.writeInt(player.getCode());
            playersFile.writeUTF(player.getUsername());
            playersFile.writeUTF(player.getPassword());
            playersFile.writeUTF(player.getName());
            playersFile.writeLong(player.getBirthdate());
            playersFile.writeInt(player.getDownloadsCount());
            playersFile.writeUTF(player.getImagePath());
            playersFile.writeUTF(player.getUserType().toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error agregando jugador: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean downloadGame(int gameCode, int playerCode, char os) {
        try {
            gamesFile.seek(0);
            boolean gameExists = false;
            boolean osMatch = false;
            Game game = null;
            while (gamesFile.getFilePointer() < gamesFile.length()) {
                int code = gamesFile.readInt();
                String title = gamesFile.readUTF();
                char gameOS = gamesFile.readChar();
                int minAge = gamesFile.readInt();
                double price = gamesFile.readDouble();
                int downloads = gamesFile.readInt();
                String imagePath = gamesFile.readUTF();
                if (code == gameCode) {
                    gameExists = true;
                    if (gameOS == os) {
                        osMatch = true;
                        game = new Game(code, title, gameOS, minAge, price, imagePath);
                    }
                    break;
                }
            }

            if (!gameExists || !osMatch) {
                return false;
            }

            playersFile.seek(0);
            boolean playerExists = false;
            boolean ageEligible = false;
            Player player = null;
            while (playersFile.getFilePointer() < playersFile.length()) {
                int code = playersFile.readInt();
                String username = playersFile.readUTF();
                String password = playersFile.readUTF();
                String name = playersFile.readUTF();
                long birthdate = playersFile.readLong();
                int downloads = playersFile.readInt();
                String playerImagePath = playersFile.readUTF();
                UserType userType = UserType.valueOf(playersFile.readUTF());
                Calendar birthCalendar = Calendar.getInstance();
                birthCalendar.setTimeInMillis(birthdate);
                int playerAge = Calendar.getInstance().get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
                if (code == playerCode) {
                    playerExists = true;
                    if (playerAge >= game.getMinAge()) {
                        ageEligible = true;
                        player = new Player(code, username, password, name, birthCalendar, playerImagePath, userType);
                    }
                    break;
                }
            }

            if (!playerExists || !ageEligible) {
                return false;
            }

            int downloadCode = getNextCode("download");
            File downloadFile = new File("steam/downloads/download_" + playerCode + "_" + downloadCode + ".stm");
            if (!downloadFile.exists()) {
                downloadFile.createNewFile();
            }
            RandomAccessFile downloadRAF = new RandomAccessFile(downloadFile, "rw");
            downloadRAF.writeUTF("Descarga #" + downloadCode);
            downloadRAF.writeUTF(player.getName() + " ha descargado " + game.getTitle() + " a un precio de $" + game.getPrice());
            downloadRAF.writeUTF("Fecha: " + Calendar.getInstance().getTime().toString());
            downloadRAF.close();

            player.incrementDownloads();
            game.incrementDownloads();
            updatePlayerDownloads(playerCode, player.getDownloadsCount());
            updateGameDownloads(gameCode, game.getDownloadsCount());

            return true;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error descargando juego: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void updatePlayerDownloads(int playerCode, int newDownloads) {
        try {
            playersFile.seek(0);
            while (playersFile.getFilePointer() < playersFile.length()) {
                int code = playersFile.readInt();
                if (code == playerCode) {
                    playersFile.seek(playersFile.getFilePointer() + 16 + playersFile.readUTF().length() + playersFile.readUTF().length() + playersFile.readUTF().length());
                    playersFile.writeInt(newDownloads);
                    break;
                } else {
                    playersFile.skipBytes(playersFile.readUTF().length() + playersFile.readUTF().length() + playersFile.readUTF().length() + 12);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error actualizando descargas del jugador: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateGameDownloads(int gameCode, int newDownloads) {
        try {
            gamesFile.seek(0);
            while (gamesFile.getFilePointer() < gamesFile.length()) {
                int code = gamesFile.readInt();
                if (code == gameCode) {
                    gamesFile.seek(gamesFile.getFilePointer() + gamesFile.readUTF().length() + 6);
                    gamesFile.writeInt(newDownloads);
                    break;
                } else {
                    gamesFile.skipBytes(gamesFile.readUTF().length() + 14);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error actualizando descargas del juego: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updatePriceFor(int gameCode, double newPrice) {
        try {
            gamesFile.seek(0);
            while (gamesFile.getFilePointer() < gamesFile.length()) {
                int code = gamesFile.readInt();
                if (code == gameCode) {
                    gamesFile.seek(gamesFile.getFilePointer() + gamesFile.readUTF().length() + 6);
                    gamesFile.writeDouble(newPrice);
                    break;
                } else {
                    gamesFile.skipBytes(gamesFile.readUTF().length() + 14);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error actualizando precio del juego: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void reportForClient(int clientCode, String txtFile) {
        try {
            playersFile.seek(0);
            boolean playerExists = false;
            StringBuilder report = new StringBuilder();

            while (playersFile.getFilePointer() < playersFile.length()) {
                int code = playersFile.readInt();
                if (code == clientCode) {
                    playerExists = true;
                    report.append("Codigo Cliente: ").append(code).append("\n")
                          .append("Usuario: ").append(playersFile.readUTF()).append("\n")
                          .append("Nombre: ").append(playersFile.readUTF()).append("\n")
                          .append("Fecha de Nacimiento: ").append(playersFile.readLong()).append("\n")
                          .append("Descargas: ").append(playersFile.readInt()).append("\n")
                          .append("Ruta de Imagen: ").append(playersFile.readUTF()).append("\n")
                          .append("Tipo de Usuario: ").append(playersFile.readUTF()).append("\n");
                    break;
                } else {
                    playersFile.skipBytes(playersFile.readUTF().length() + playersFile.readUTF().length() + 12 + playersFile.readUTF().length());
                }
            }

            if (playerExists) {
                try (RandomAccessFile reportFile = new RandomAccessFile(txtFile, "rw")) {
                    reportFile.setLength(0); // Limpiar el archivo si ya existe
                    reportFile.writeUTF(report.toString());
                    JOptionPane.showMessageDialog(null, "Reporte creado", "Exito", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error creando el archivo de reporte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cliente no encontrado, no se puede crear reporte", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error leyendo los datos del cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        try {
            gamesFile.seek(0);
            while (gamesFile.getFilePointer() < gamesFile.length()) {
                int code = gamesFile.readInt();
                String title = gamesFile.readUTF();
                char os = gamesFile.readChar();
                int minAge = gamesFile.readInt();
                double price = gamesFile.readDouble();
                int downloads = gamesFile.readInt();
                String imagePath = gamesFile.readUTF();
                games.add(new Game(code, title, os, minAge, price, imagePath));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error obteniendo juegos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return games;
    }

    public List<Game> getDownloadedGames(int playerCode) {
        List<Game> downloadedGames = new ArrayList<>();
        try {
            File downloadFolder = new File("steam/downloads");
            File[] downloadFiles = downloadFolder.listFiles((dir, name) -> name.startsWith("download_" + playerCode + "_"));

            if (downloadFiles != null) {
                for (File downloadFile : downloadFiles) {
                    RandomAccessFile downloadRAF = new RandomAccessFile(downloadFile, "r");
                    downloadRAF.seek(0);
                    String downloadDetails = downloadRAF.readUTF();

                    if (downloadDetails.contains("ha descargado")) {
                        String gameTitle = downloadDetails.split("ha descargado")[1].split(" a un precio de")[0].trim();
                        Game game = getGameByTitle(gameTitle);
                        if (game != null) {
                            downloadedGames.add(game);
                        }
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error obteniendo juegos descargados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return downloadedGames;
    }

    public Player getPlayerByUsername(String username) {
        try {
            playersFile.seek(0);
            while (playersFile.getFilePointer() < playersFile.length()) {
                int code = playersFile.readInt();
                String user = playersFile.readUTF();
                if (user.equals(username)) {
                    String password = playersFile.readUTF();
                    String name = playersFile.readUTF();
                    long birthdate = playersFile.readLong();
                    int downloads = playersFile.readInt();
                    String imagePath = playersFile.readUTF();
                    UserType userType = UserType.valueOf(playersFile.readUTF());
                    Calendar birthCalendar = Calendar.getInstance();
                    birthCalendar.setTimeInMillis(birthdate);
                    return new Player(code, user, password, name, birthCalendar, imagePath, userType);
                } else {
                    playersFile.skipBytes(playersFile.readUTF().length() + playersFile.readUTF().length() + 12 + playersFile.readUTF().length());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error obteniendo jugador: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public Game getGameByTitle(String title) {
        try {
            gamesFile.seek(0);
            while (gamesFile.getFilePointer() < gamesFile.length()) {
                int code = gamesFile.readInt();
                String gameTitle = gamesFile.readUTF();
                if (gameTitle.equals(title)) {
                    char os = gamesFile.readChar();
                    int minAge = gamesFile.readInt();
                    double price = gamesFile.readDouble();
                    int downloads = gamesFile.readInt();
                    String imagePath = gamesFile.readUTF();
                    return new Game(code, gameTitle, os, minAge, price, imagePath);
                } else {
                    gamesFile.skipBytes(21 + gamesFile.readUTF().length());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error obteniendo juego: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
