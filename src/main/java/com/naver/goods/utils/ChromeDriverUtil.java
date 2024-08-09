//package com.naver.goods.utils;
//
//import lombok.SneakyThrows;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.Proxy;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.support.ui.ExpectedCondition;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * ChromeDriver工具类，提供多种初始化和操作方法
// *
// * @Author: zhouquan
// * @Date: 2024/6/23
// * @Version: 1.0
// * @Description: ChromeDriver工具类，提供多种初始化和操作方法
// */
//public class ChromeDriverUtil {
//    // 文件版本, 防止多线程缓存文件和用户文件共享, 导致创建错误
//    private static AtomicInteger fileSerial = new AtomicInteger(0);
//    private ChromeDriver driver;
//
//    /**
//     * 构造函数，初始化ChromeDriver
//     * @param path ChromeDriver路径
//     * @param pd 是否显示浏览器
//     * @param img 是否加载图片
//     */
//    public ChromeDriverUtil(String path, boolean pd, boolean img) {
//        init(path, pd, img);
//    }
//
//    @SneakyThrows
//    private void init(String path, boolean pd, boolean img) {
//        // 设置ChromeDriver路径
//        System.setProperty("webdriver.chrome.driver", path);
//        ChromeOptions options = new ChromeOptions();
//
//        if (!pd) {
//            options.addArguments("--headless"); // 无浏览器模式
//        }
//
//        // 优化参数设置
//        options.addArguments("--disable-gpu"); // 禁用GPU
//        options.addArguments("--disable-software-rasterizer"); // 禁用3D软件光栅化器
////        options.addArguments("--no-sandbox"); // 允许Linux root用户执行
//        options.addArguments("--disable-dev-shm-usage"); // 解决某些VM环境中Chrome崩溃问题
//
//        if (img) {
//            options.addArguments("blink-settings=imagesEnabled=false"); // 禁止加载图片
//            options.addArguments("--disable-images");
//        }
//
//        // 设置临时文件夹
//        String tmpdir = System.getProperty("java.io.tmpdir");
//        String dir = tmpdir + File.separator + "chrome_file_data_cache" + File.separator + fileSerial.incrementAndGet();
//
//        File dataDir = new File(dir + File.separator + "data");
//        if (!dataDir.exists()) {
//            dataDir.mkdirs();
//        }
//
//        File cacheDir = new File(dir + File.separator + "cache");
//        if (!cacheDir.exists()) {
//            cacheDir.mkdirs();
//        }
//
//        options.addArguments("--user-data-dir=" + dataDir.getAbsolutePath()); // 设置用户数据目录
//        options.addArguments("--disk-cache-dir=" + cacheDir.getAbsolutePath()); // 设置缓存目录
//        options.addArguments("--incognito"); // 无痕模式
//        options.addArguments("--disable-plugins"); // 禁用插件
//        options.addArguments("--disable-extensions"); // 禁用扩展
//        options.addArguments("--disable-popup-blocking"); // 关闭弹窗拦截
//        options.addArguments("--ignore-certificate-errors"); // 忽略证书错误
//        options.addArguments("--allow-running-insecure-content"); // 允许加载不安全内容
//        options.addArguments("--disable-infobars"); // 禁用浏览器正在被自动化程序控制的提示
//
//        if (!pd) {
//            // 无浏览器模式-最大化窗口，防止有些元素被隐藏
//            int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
//            int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
//            options.addArguments("window-size=" + screenWidth + "," + screenHeight);
//        }
//
//        // 随机设置请求头
//        options.addArguments("--user-agent=" + UserAgent.getUserAgentWindows());
//        proxy(options, false); // 设置代理，true 开启代理
//
//        driver = new ChromeDriver(options); // 实例化ChromeDriver
//
//        if (pd) {
//            driver.manage().window().maximize(); // 显示模式下最大化窗口，防止有些元素被隐藏
//        }
//
//        // 设置隐式等待
//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//    }
//
//    // 无头模式，不加载图片
//    public static ChromeDriverUtil buildHide(String path) {
//        return new ChromeDriverUtil(path, false, true);
//    }
//
//    // 无头模式，加载图片
//    public static ChromeDriverUtil buildHideImg(String path) {
//        return new ChromeDriverUtil(path, false, false);
//    }
//
//    // 显示浏览器，全功能
//    public static ChromeDriverUtil build(String path) {
//        return new ChromeDriverUtil(path, true, false);
//    }
//
//    public ChromeDriver getDriver() {
//        return driver;
//    }
//
//    // 强制等待，代码在执行到某个位置时强制等待一段时间
//    @SneakyThrows
//    public void sleep(long ms) {
//        Thread.sleep(ms);
//    }
//
//    // 显示等待，为了解决隐式等待遗留的问题
//    public WebElement wait(int seconds, ExpectedCondition<WebElement> expectedCondition) {
//        WebDriverWait webDriverWait = new WebDriverWait(driver, seconds);
//        WebElement until = webDriverWait.until(expectedCondition);
//        return until;
//    }
//
//    // 设置代理
//    private void proxy(ChromeOptions options, boolean pd) {
//        if (pd) {
//            String prox = "58.217.74.153:" + 27434; // 代理地址
//            Proxy p = new Proxy();
//            p.setHttpProxy(prox); // 设置HTTP代理
//            options.setProxy(p);
//        }
//    }
//
//    // 截图
//    public void screenshotPNG(TakesScreenshot takesScreenshot, File file) {
//        byte[] screenshotAs = takesScreenshot.getScreenshotAs(OutputType.BYTES);
//        ReadWriteFileUtils.writeByte(screenshotAs, file);
//        try (FileOutputStream fos = new FileOutputStream(file);
//             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
//            bos.write(screenshotAs, 0, screenshotAs.length); // 写入数据
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}