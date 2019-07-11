package roomieboomie.persistence;

import roomieboomie.business.room.Room;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageHandler {
    private final static String FORMAT = "png";

    private static ImageHandler instance;

    /**
     * @return Instanz von SoundHandler, ueber die anschliessend Sounds abfragbar sind.
     */
    public static ImageHandler get() {
        if (instance == null) {
            instance = new ImageHandler();
        }
        return instance;
    }

    /**
     * Laedt ein Thumbnail zu einem Raum
     * @param name Name des Raums
     * @param level true, wenn der Raum im Level-Modus spielbar ist; false, wenn Kreativ-Modus-Raum
     * @return Image, das Vorschaubild des Raums beinhaltet
     */
    public Image thumbnail(String name, boolean level) {
        String directory = level ? Config.get().LEVELTHUMBNAILPATH() : Config.get().CREATIVETHUMBNAILPATH();
        String noDirectory = Config.get().NOPICTUREPATH();

        Image image = null;
        try {
            image = new Image(new FileInputStream(noDirectory));
        } catch (FileNotFoundException e) {
            System.err.println("Kein Pseudo VorzeigeBild vorhanden. Die Datei noPicture.png wurde nicht gefunden.");
        }

        try {
            image = new Image(new FileInputStream(directory + name + "." + FORMAT));
        } catch (IOException e) {
            System.err.println("Anzeigebild des Raums " + name + " konnte nicht geladen werden.");
        }

        return image;
    }

    /**
     * Erstellt ein Thumbnail des Raums
     * @param room Raum, von dem das Thumbnail erstellt werden soll
     */
    public void drawThumbnail(Room room) {
        byte editorDoor = Config.get().EDITORDOORVALUE();
        byte layoutInterior = Config.get().LAYOUTINTERIORVALUE();
        byte layoutExterior = Config.get().LAYOUTEXTERIORVALUE();
        byte maxWindow = Config.get().EDITORMAXWINDOWVALUE();
        byte minWall = Config.get().EDITORMINWALLVALUE();

        final int FACTOR = 10; //TODO MAGIC

        byte[][] layout = room.getEffectiveLayout();
        int startX = room.getStartX();
        int startY = room.getStartY();
        int height = room.getHeight();
        int width = room.getWidth();

        int type = BufferedImage.TYPE_INT_ARGB;

        BufferedImage image = new BufferedImage(width * FACTOR, height * FACTOR, type);

        int wallC = Color.decode(Config.get().WALLCOLOR()).getRGB();
        int doorC = Color.decode(Config.get().DOORCOLOR()).getRGB();
        int windowC = Color.decode(Config.get().WINDOWCOLOR()).getRGB();
        int interiorC = Color.decode(Config.get().INTERIORCOLOR()).getRGB();
        int backgroundC = Color.decode(Config.get().BGCOLOR()).getRGB();

        int currCol;

        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                if (layout[i][j] == 0) {
                    currCol = interiorC;
                } else if (layout[i][j] >= minWall) {
                    currCol = wallC;
                } else if (layout[i][j] <= maxWindow) {
                    currCol = windowC;
                } else if (layout[i][j] == editorDoor) {
                    currCol = doorC;
                } else {
                    currCol = backgroundC;
                }

                for (int l = 0; l < FACTOR; l++) {
                    for (int k = 0; k < FACTOR; k++) {
                        int xCoord = (j * FACTOR) + k;
                        int yCoord = (i * FACTOR) + l;
                        image.setRGB(xCoord, yCoord, currCol);
                    }
                }
            }
        }

        String directory = room.isLevel() ? Config.get().LEVELTHUMBNAILPATH() : Config.get().CREATIVETHUMBNAILPATH();

        File outputfile = new File(directory + room.getName() + "." + FORMAT);
        try {
            ImageIO.write(image, FORMAT, outputfile);
        } catch (IOException e) {
            System.err.println(String.format("Thumbnail von Raum %s konnte nicht gespeichert werden. Bitte Pfad ueberpruefen."));
        }
    }

    /**
     * Gibt ein Bild aus dem Ordner der PlacableImage-Texturen zurueck
     * @param filename Name der Datei ohne .Endung
     * @return JavaFX-Image
     */
    public Image placableItemImage(String filename){
        try{
            String path = Config.get().PLACABLEITEMTEXTUREPATH() + filename + "." + FORMAT;
            return new Image(path);
        } catch (IllegalArgumentException e){
            System.err.println("Bild zu " + filename + " konnte nicht gefunden werden");
            return new Image("img/thumbnails/noPicture.png");
        }
    }
}
