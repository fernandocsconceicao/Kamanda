package br.app.camarada.backend.utilitarios;


import br.app.camarada.backend.exception.ErroPadrao;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static byte[] createTestImage() throws IOException {
        BufferedImage bImage = new BufferedImage(550, 1200, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Graphics2D graphics = bImage.createGraphics();

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, 550, 1200);
        graphics.setColor(Color.WHITE);
        graphics.fillOval(20, 20, 80, 80);
        graphics.dispose();

        ImageIO.write(bImage, "png", baos);
        return baos.toByteArray();
    }



    public static byte[] getLocationIcon()  {
        try {
            File icon = new File("C:\\Users\\Fernando\\Documents\\Intellij Workspace\\ubuntu-back-end\\src\\main\\resources\\images\\location-icon.png");
            BufferedImage read = ImageIO.read(icon);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(read, "png", baos);
            return baos.toByteArray();
        }catch (IOException e){
            throw new ErroPadrao("Imagem não encontrada");
        }

    }
}
