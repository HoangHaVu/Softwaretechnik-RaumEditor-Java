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

    public static Image getThumbnail(String name, boolean level){
        String directory = level ? Config.get().LEVELTHUMBNAILPATH() : Config.get().CREATIVETHUMBNAILPATH();
        String noDirectory= Config.get().NOPICTUREPATH();


        Image image = null;
        try {
            image = new Image(new FileInputStream(noDirectory));
        } catch (FileNotFoundException e) {
            System.out.println("kein Pseudo VorzeigeBild vorhanden - Die Datei noPicture.png wurde nicht gefunden");
            //e.printStackTrace();
        }
        try {
            image = new Image(new FileInputStream(directory + name + "." + FORMAT));
        } catch (IOException e) {
            System.out.println("VorzeigeBild vom Raum wurde nicht gefunden");
            //e.printStackTrace();
        }

        return image;
    }

    public static void drawThumbnail(Room room){
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
                if (layout[i][j] == 0){
                    currCol = interiorC;
                }else if(layout[i][j] >= minWall){
                    currCol = wallC;
                } else if (layout[i][j] <= maxWindow){
                    currCol = windowC;
                } else if (layout[i][j] == editorDoor){
                    currCol = doorC;
                } else{
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
            e.printStackTrace();
        }
    }
}
