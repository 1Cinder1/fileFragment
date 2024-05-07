package com.cinder.filefragment.utils;

import com.cinder.filefragment.threadPool.ReadFileThreadPoolFactory;
import com.cinder.filefragment.vo.FragmentFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FileEncryptionExampleParallelForAES implements ApplicationRunner {

    private final static List<Long> encryFileList = new ArrayList<>();
    private final static List<Long> outFileList = new ArrayList<>();

    public final static ConcurrentHashMap<String,Long> MAP = new ConcurrentHashMap<>();

    private final static String basePath = "/Users/cinder/code/java/hyperchain/";
    private final static Integer stepLength = 1024*8000;

    public static void main(String[] args) throws Exception {
        String inputFile = "1.33G_ppt&video.zip"; // 待加密的文件路径
        String outputFile = "output.zip"; // 加密后的文件路径
        String password = "MySecretPassword"; // 对称加密所使用的密码（密钥）

        try {
            int count = 1;
            for (int i = 0; i < count; i++) {
                long startTime = System.currentTimeMillis();
                fragmentFile(inputFile, outputFile, password);
                encryFileList.add(System.currentTimeMillis() - startTime);
                System.out.println("当前完成第" + i + "次");
            }
            double average = encryFileList.parallelStream()
                    .mapToLong(Long::longValue)
                    .average()
                    .orElse(0.0);
            System.out.println("加密平均时间：" + average + "毫秒");
            System.out.println("文件加密完成！");
            Thread.sleep(10000);
            double av = MAP.values().parallelStream().filter(v -> v > 0).mapToLong(Long::longValue).average().orElse(0.0);
            System.out.println("read 平均时间：" + av + "毫秒");
        } catch (OutOfMemoryError error) {
            Thread.sleep(10000000);
        }
    }
    public static void fragmentFile(String inputFile,String outputFile, String password) {
        try {
            // 读取待加密的文件
            Path inputPath = Paths.get(basePath+inputFile);
            Path outputPath = Paths.get(basePath+UUID.randomUUID()+outputFile);

            // 创建输入流和输出流
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputPath.toFile()));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputPath.toFile()));
            BigDecimal available = new BigDecimal(inputPath.toFile().length());
            log.info("available: "+available.intValue());
            BigDecimal step = new BigDecimal(stepLength);
            int total = available.divide(step).setScale(0, BigDecimal.ROUND_UP).intValue();
            inputFile = inputFile+UUID.randomUUID();
            // 记时用
            MAP.put(inputFile,System.currentTimeMillis());
            for(long i=0;i<total;i++) {
                FragmentFile fragmentFile;
                if (i==total-1) {
                    fragmentFile = new FragmentFile(inputFile,(int) i,total,i*stepLength,available.intValue()-(int) i*stepLength,inputPath.toFile(),outputStream,password);
                }else {
                    fragmentFile = new FragmentFile(inputFile,(int) i,total,i*stepLength,stepLength,inputPath.toFile(),outputStream,password);
                }
//                ReadFileThreadPoolFactory.submitTask(fragmentFile);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void encryFile(String inputFile, String outputFile, String password) {
        try {
            // 读取待加密的文件
            Path inputPath = Paths.get(basePath+inputFile);
            Path outputPath = Paths.get(basePath+outputFile);

            // 创建输入流和输出流
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputPath.toFile()));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputPath.toFile()));

            // 创建线程池
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

            // 并行处理文件内容的加密操作
            byte[] buffer = new byte[1024 * 8000];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] data = new byte[bytesRead];
                System.arraycopy(buffer, 0, data, 0, bytesRead);

                // 提交加密任务给线程池处理
                executorService.submit(() -> {
                    byte[] encryptedData = new byte[0];
                    try {
                        encryptedData = encryptData(data, password);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    long l1 = System.currentTimeMillis();
                    // 写入加密后的数据到输出流
                    try {
                        outputStream.write(encryptedData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    outFileList.add(System.currentTimeMillis() - l1);
                });
            }

            // 关闭线程池
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            // 关闭流
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void myEncrypt(String inputFile, String outputFile, String password) {
        try {
            // 读取待加密的文件
            Path inputPath = Paths.get(basePath+inputFile);
            Path outputPath = Paths.get(basePath+outputFile);

            // 创建输入流和输出流
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputPath.toFile()));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputPath.toFile()));

            // 创建线程池
            ExecutorService executorService = Executors.newFixedThreadPool(5);

            // 并行处理文件内容的加密操作
            byte[] buffer = new byte[1024 * 8000];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] data = new byte[bytesRead];
                System.arraycopy(buffer, 0, data, 0, bytesRead);

                // 提交加密任务给线程池处理
                executorService.submit(() -> {
                    byte[] encryptedData = new byte[0];

                    try {
                        encryptedData = encryptData(data, password);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    long l1 = System.currentTimeMillis();
                    // 写入加密后的数据到输出流
                    try {
                        outputStream.write(encryptedData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    outFileList.add(System.currentTimeMillis() - l1);
                });
            }

            // 关闭线程池
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            // 关闭流
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static byte[] encryptData(byte[] data, String password) throws Exception {
        // 加密操作逻辑
        // 创建对称密钥
        Key secretKey = new SecretKeySpec(password.getBytes(), "AES");

        // 创建加密器
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // 执行加密操作
        return cipher.doFinal(data);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}