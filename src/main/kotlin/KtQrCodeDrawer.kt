import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Path
import javax.imageio.ImageIO

/**
 * Created by yy_yank on 2016/11/09.
 */
object KtQrCodeDrawer {
    private val BLACK = 0xFF000000.toInt()
    private val WHITE = 0xFFFFFFFF.toInt()

    fun draw(pair: Pair<Path, String>) {
        val format = BarcodeFormat.QR_CODE

        val pos = Position(300, 300)
        val hints = mapOf<EncodeHintType, Any>(
                EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
                EncodeHintType.CHARACTER_SET to "Shift-JIS"
        )
        val bitMatrix = QRCodeWriter().encode(pair.second, format, pos.width, pos.height, hints)
        val agbArray = createRgbArray(pos, bitMatrix)
        val qrImage = BufferedImage(pos.width, pos.height, BufferedImage.TYPE_INT_RGB).apply {
            setRGB(0, 0, width, height, agbArray, 0, width)
            flush()
        }
        ImageIO.write(qrImage, "png", File(pair.first.toString() + ".png"))
    }

    private fun createRgbArray(pos : Position, bitMatrix: BitMatrix): IntArray {
        val (width, height) = pos
        val pixels = IntArray(width * height)
        // All are 0, or black, by default
        for (y in 0..height - 1) {
            val offset = y * width
            for (x in 0..width - 1) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) BLACK else WHITE
            }
        }
        return pixels
    }
}

class Position(val width : Int, val height: Int) {
    operator fun  component1()  = width
    operator fun  component2() = height
}