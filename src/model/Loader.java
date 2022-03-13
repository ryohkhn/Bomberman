package model;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;
import java.io.File;

public class Loader {
    private BufferedImage image;
    
    public BufferedImage loadImage(String path) throws IOException{
        image = ImageIO.read(new File(path));
    	return image;
    }
}