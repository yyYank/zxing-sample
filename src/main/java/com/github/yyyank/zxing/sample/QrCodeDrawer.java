package com.github.yyyank.zxing.sample;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yy_yank on 2016/11/09.
 */
public class QrCodeDrawer {


    private final static int BLACK = 0xFF000000;
    private final static int WHITE = 0xFFFFFFFF;

    public static void draw(Tuple tuple) {

        try {
            BarcodeFormat format = BarcodeFormat.QR_CODE;
            int width = 300;
            int height = 300;

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.CHARACTER_SET, "Shift-JIS");

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(tuple.value, format, width, height, hints);

            int[] agbArray = createRgbArray(width, height, bitMatrix);

            BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            qrImage.setRGB(0, 0, width, height, agbArray, 0, width);
            qrImage.flush();
            ImageIO.write(qrImage, "png", new File(tuple.key.toString() + ".png"));
        } catch (IOException | WriterException e) {
            System.err.println(e.getMessage() + " : filePath......." + tuple.key.toString());
        }
    }

    private static int[] createRgbArray(int width, int height, BitMatrix bitMatrix) {
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
            }
        }
        return pixels;
    }
}
