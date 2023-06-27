import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class ImageModifier {
    private BufferedImage image;
    private int width, height;

    public void loadImage(String path) {
        try {
            image = ImageIO.read(new File(path));
            width = image.getWidth();
            height = image.getHeight();
            System.out.println("Image loaded");
           // System.out.println(width + " " + height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveImage(String path) {
        try {
            ImageIO.write(image, "jpg", new File(path));
            System.out.println("Image saved");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeBrightness(int amount) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                int r = (0xFF0000 & rgb) >> 16;
                int g = (0xFF00 & rgb) >> 8;
                int b = 0xFF & rgb;
                r = calculateBrightness(amount, r);
                g = calculateBrightness(amount, g);
                b = calculateBrightness(amount, b);

                int newRgb = b | (g << 8) | (r << 16);
                image.setRGB(x, y, newRgb);
            }
        }
    }

    class ImageThread extends Thread {
        private final int begin;
        private final int end;

        public ImageThread(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        public void changeBrightness(int amount) {
            for(int x = begin; x < end; x++) {
                for(int y = 0; y < height; y++) {
                    int rgb = image.getRGB(x, y);
                    int r = (0xFF0000 & rgb) >> 16;
                    int g = (0xFF00 & rgb) >> 8;
                    int b = 0xFF & rgb;
                    r = calculateBrightness(amount, r);
                    g = calculateBrightness(amount, g);
                    b = calculateBrightness(amount, b);

                    int newRgb = b | (g << 8) | (r << 16);
                    image.setRGB(x, y, newRgb);
                }
            }
        }
    }

    public void changeBrightnessViaThreads(int amount) {
        int threads = Runtime.getRuntime().availableProcessors();
        int tile = width / threads;

        ImageThread[] imageThreads = new ImageThread[threads];
        for(int i = 0; i < threads; i ++) {
            int begin = tile * i;
            int end = tile * (i + 1);
            imageThreads[i] = new ImageThread(begin, end);
            imageThreads[i].changeBrightness(amount);
        }
    }

    public void changeBrightnessViaThreadsPool(int amount) {
        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Future<Void>> futures = new ArrayList<>();

        for(int y = 0; y < height; y++) {
            final int currentRow = y;
            Callable<Void> task = () -> {
                for(int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, currentRow);
                    int r = (0xFF0000 & rgb) >> 16;
                    int g = (0xFF00 & rgb) >> 8;
                    int b = 0xFF & rgb;
                    r = calculateBrightness(amount, r);
                    g = calculateBrightness(amount, g);
                    b = calculateBrightness(amount, b);

                    int newRgb = b | (g << 8) | (r << 16);
                    image.setRGB(x, currentRow, newRgb);
                }
                return null;
            };

            Future<Void> future = executor.submit(task);
            futures.add(future);
        }

        for(Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        executor.shutdown();
    }

    public void calculateHistogram() throws InterruptedException {
        int threads = Runtime.getRuntime().availableProcessors();
        int tile = height / threads;
        int[][] histogram = new int[threads][256];

        Thread[] threadArr = new Thread[threads];
        for(int i = 0; i < threads; i++) {
            int index = i;
            threadArr[i] = new Thread(() -> {
                Arrays.fill(histogram[index], 0);
                int begin = tile * index;
                int end = tile * (index + 1);
                for(int y = begin; y < end; y++) {
                    for(int x = 0; x < width; x++) {
                        int rgb = image.getRGB(x, y);
                        int r = (0xFF0000 & rgb) >> 16;
                        histogram[index][r]++;
                    }
                }
            });
        }
        for(int i = 0; i < threads; i++) {
            threadArr[i].start();
        }
        for(int i = 0; i < threads; i++) {
            threadArr[i].join();
        }
        int[] finalHistogram = new int[256];
        for(int i = 0; i < threads; i++) {
            for(int j = 0; j < 256; j++) {
                finalHistogram[j] += histogram[i][j];
            }
        }
        for(int i = 0; i < 256; i++) {
            System.out.println(i + ", " + finalHistogram[i] + " ");
        }
    }

    private int calculateBrightness(int amount, int value) {
        value += amount;
        if(value > 255) value =  255;
        else if (value < 0) value = 0;
        return value;
    }
}
