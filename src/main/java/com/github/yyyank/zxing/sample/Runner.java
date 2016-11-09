package com.github.yyyank.zxing.sample;


import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy_yank on 2016/11/09.
 */
public class Runner {



    public static void main(String... args) throws IOException, URISyntaxException {
        new Runner().run();
    }


    private FileVisitor<Path> visitor = new FileVisitor<Path>() {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            System.out.println(file.getFileName());
            if (file.toString().endsWith("png")) {
                Files.delete(file);
            } else if (file.toString().endsWith("txt")) {
                StringBuilder builder = new StringBuilder();
                List<String> lines = Files.readAllLines(file);
                lines.forEach(builder::append);
                textList.add(new Tuple(file, builder.toString()));
            }

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    };

    private List<Tuple> textList = new ArrayList<>();

    /**
     * runner.run 1. start. 2. file walking and collect file texts. 3.
     * encoding to qrcode for-each file 4. end
     */
    public void run() throws IOException, URISyntaxException {
        System.out.println("[START]");
        URL url = Thread.currentThread().getContextClassLoader().getResource("test.txt");
        Files.walkFileTree(Paths.get(url.toURI()), visitor);
        textList.forEach(QrCodeDrawer::draw);
        System.out.println("[END]");
    }

}

