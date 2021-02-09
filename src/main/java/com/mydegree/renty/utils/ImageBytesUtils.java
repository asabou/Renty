package com.mydegree.renty.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageBytesUtils {
    private final String filename;

    public ImageBytesUtils(String filename) {
        this.filename = Constants.imagePath + filename;
    }

    public byte[] extractBytes() {
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            boolean jpg = ImageIO.write(originalImage, "jpg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public void convertToJpg(byte[] bytes) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
