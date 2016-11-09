package com.github.yyyank.zxing.sample

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import javax.imageio.ImageIO

/**
 * Created by yy_yank on 2016/11/09.
 */
object KtRunner {
    val textList = mutableListOf<Pair<Path, String>>()
    val visitor = object : FileVisitor<Path> {
        override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = FileVisitResult.CONTINUE
        override fun visitFile(file: Path, attrs: BasicFileAttributes) : FileVisitResult = FileVisitResult.CONTINUE.apply {
            println(file.fileName)
            when {
                file.toString().endsWith("png") -> Files.delete(file)
                file.toString().endsWith("txt") -> {
                    val lines = Files.readAllLines(file)
                    textList.add(Pair(file, lines.reduce { s1, s2 -> s1 + s2 }))
                }
            }
        }
        override fun visitFileFailed(file: Path, exc: IOException): FileVisitResult = FileVisitResult.CONTINUE
        override fun postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = FileVisitResult.CONTINUE
    }
    @JvmStatic fun main(args: Array<String>) {
        println("[START]")
        val url = Thread.currentThread().contextClassLoader.getResource("test.txt")
        Files.walkFileTree(Paths.get(url!!.toURI()), visitor)
        textList.forEach { KtQrCodeDrawer.draw(it) }
        println("[END]")
    }
}

