package roomieboomie.persistence;

import roomieboomie.business.room.Room;
import roomieboomie.business.room.RoomPreview;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHandler {
    private final static String FORMAT = "png";

    public static BufferedImage getThumbnail(String name, boolean level){
        String directory = level ? Config.get().LEVELTHUMBNAILPATH() : Config.get().CREATIVETHUMBNAILPATH();

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(directory + name + "." + FORMAT));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static void drawThumbnail(Room room){
        final int FACTOR = 10; //TODO MAGIC

        byte[][] layout = room.getLayout();
        int startX = room.getRoomPreview().getStartX();
        int startY = room.getRoomPreview().getStartY();
        int height = room.getRoomPreview().getHeight();
        int width = room.getRoomPreview().getWidth();

        int type = BufferedImage.TYPE_INT_ARGB;

        BufferedImage image = new BufferedImage(width * FACTOR, height * FACTOR, type);

        int wallC = Color.decode(Config.get().WALLCOLOR()).getRGB(); // Waende > 2
        int doorC = Color.decode(Config.get().DOORCOLOR()).getRGB(); // Tuer -2
        int windowC = Color.decode(Config.get().WINDOWCOLOR()).getRGB(); // Fenster < -3
        int interiorC = Color.decode(Config.get().INTERIORCOLOR()).getRGB();
        int backgroundC = Color.decode(Config.get().BGCOLOR()).getRGB();

        int currCol = 0;
        //int x = 0;
        int y = 0;
        for(int i = startY; i < startY+height; i++) {
            int x = 0;
            for(int j = startX; j < startX+width; j++) {

                if (layout[i][j] == 0){
                    currCol = interiorC;
                }else if(layout[i][j] > 2){
                    currCol = wallC;
                } else if (layout[i][j] < -3){
                    currCol = windowC;
                } else if (layout[i][j] == -2){
                    currCol = doorC;
                } else{
                    currCol = backgroundC;
                }

                for (int l = 0; l < FACTOR; l++) {
                    for (int k = 0; k < FACTOR; k++) {
                        int xCoord = (x * FACTOR) + k;
                        int yCoord = (y * FACTOR) + l;
                        image.setRGB(xCoord, yCoord, currCol);
                    }
                }
                x++;
            }
            y++;
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
