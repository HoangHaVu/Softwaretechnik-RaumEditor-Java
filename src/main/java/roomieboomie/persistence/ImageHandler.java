package roomieboomie.persistence;

import roomieboomie.business.room.Room;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageHandler {
    private final static String FORMAT = "png";

    public static Image getThumbnail(String name, boolean level){
        String directory = level ? Config.get().LEVELTHUMBNAILPATH() : Config.get().CREATIVETHUMBNAILPATH();

        Image image = null;
        try {
            image = new Image(new FileInputStream(directory + name + "." + FORMAT));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static void drawThumbnail(Room room){
        final int FACTOR = 10; //TODO MAGIC

        //byte[][] layout = room.getLayout();
        byte[][] layout = room.getEffectiveLayout();
        int startX = room.getStartX();
        int startY = room.getStartY();
        int height = room.getHeight();
        int width = room.getWidth();

        int type = BufferedImage.TYPE_INT_ARGB;

        BufferedImage image = new BufferedImage(width * FACTOR, height * FACTOR, type);

        int wallC = Color.decode(Config.get().WALLCOLOR()).getRGB(); // Waende > 0
        int doorC = Color.decode(Config.get().DOORCOLOR()).getRGB(); // Tuer -2
        int windowC = Color.decode(Config.get().WINDOWCOLOR()).getRGB(); // Fenster < -3
        int interiorC = Color.decode(Config.get().INTERIORCOLOR()).getRGB();
        int backgroundC = Color.decode(Config.get().BGCOLOR()).getRGB();

        int currCol;

        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                if (layout[i][j] == 0){
                    currCol = interiorC;
                }else if(layout[i][j] > 0){
                    currCol = wallC;
                } else if (layout[i][j] < -2){
                    currCol = windowC;
                } else if (layout[i][j] == -2){
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
