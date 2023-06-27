public class Main {
    public static void main(String[] args) throws InterruptedException {
        ImageModifier modifier = new ImageModifier();
        String loadPath = "";
        String savePath = "new_image.jpg";
        String savePath2 = "new_image2.jpg";
        String savePath3 = "new_image3.jpg";

        modifier.loadImage(loadPath);
        long timeBegin = System.currentTimeMillis();
        modifier.changeBrightness(50);
        long timeEnd = System.currentTimeMillis();
        modifier.saveImage(savePath);
        System.out.println("Time elapsed: " + (timeEnd - timeBegin));

        modifier.loadImage(loadPath);
        timeBegin = System.currentTimeMillis();
        modifier.changeBrightnessViaThreads(50);
        timeEnd = System.currentTimeMillis();
        modifier.saveImage(savePath2);
        System.out.println("Time elapsed using threads: " + (timeEnd - timeBegin));

        modifier.loadImage(loadPath);
        timeBegin = System.currentTimeMillis();
        modifier.changeBrightnessViaThreadsPool(50);
        timeEnd = System.currentTimeMillis();
        modifier.saveImage(savePath3);
        System.out.println("Time elapsed using thread pool: " + (timeEnd - timeBegin));

        modifier.loadImage(loadPath);
        modifier.calculateHistogram();
    }
}