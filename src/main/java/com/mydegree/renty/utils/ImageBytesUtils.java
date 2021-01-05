package com.mydegree.renty.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class ImageBytesUtils {
    private final String filename;

    public ImageBytesUtils(String filename) {
        this.filename = Constants.imagePath + filename;
    }

    public byte[] extractBytes()  throws Exception {
        BufferedImage originalImage = ImageIO.read(new File(filename));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean jpg = ImageIO.write(originalImage, "jpg", baos);
        return baos.toByteArray();
    }

    public void convertToJpg(byte[] bytes) throws Exception {
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(bytes);
    }
}
