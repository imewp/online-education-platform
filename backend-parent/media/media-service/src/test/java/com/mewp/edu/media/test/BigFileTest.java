package com.mewp.edu.media.test;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 大文件处理测试
 *
 * @author mewp
 * @version 1.0
 * @date 2023/11/5 19:15
 */
public class BigFileTest {

    /**
     * 文件分块流程：
     * 1. 获取源文件长度
     * 2. 根据设定的分块的大小计算出块数
     * 3. 从源文件读数据依次向每一个块写数据
     */
    @Test
    public void testChunk() throws IOException {
        File sourceFile = new File("/Users/mewp/Downloads/1082.mp4");
        String chunkPath = "/Users/mewp/Desktop/test/temp/chunk/";
        File chunkFolder = new File(chunkPath);
        if (!chunkFolder.exists()) {
            chunkFolder.mkdirs();
        }
        //分块大小
        long chunkSize = 1024 * 1024 * 50;
        //分块数量
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        System.out.println("分块总数：" + chunkNum);
        //缓冲区大小
        byte[] bytes = new byte[1024 * 1024];
        //使用RandomAccessFile访问文件
        RandomAccessFile rafRead = new RandomAccessFile(sourceFile, "r");
        //分块
        for (int i = 0; i < chunkNum; i++) {
            //创建分块文件
            File file = new File(chunkPath + i);
            if (file.exists()) {
                file.delete();
            }
            boolean newFile = file.createNewFile();
            if (newFile) {
                //向分块文件中写数据
                RandomAccessFile rafWrite = new RandomAccessFile(file, "rw");
                int len;
                while ((len = rafRead.read(bytes)) != -1) {
                    rafWrite.write(bytes, 0, len);
                    if (file.length() >= chunkSize) {
                        break;
                    }
                }
                rafWrite.close();
                System.out.println("完成分块 " + i);
            }
        }
        rafRead.close();
    }

    /**
     * 文件合并流程：
     * 1. 找到要合并的文件并按文件的先后进行排序
     * 2. 创建合并文件
     * 3. 依次从合并的文件中读取数据向合并文件写入数据
     */
    @Test
    void testMerge() throws IOException {
        //块文件目录
        String chunkPath = "/Users/mewp/Desktop/test/temp/chunk/";
        //原始文件
        File sourceFile = new File("/Users/mewp/Downloads/1082.mp4");
        //合并文件
        File mergeFile = new File("/Users/mewp/Downloads/1082-merge.mp4");
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        //创建新的合并文件
        mergeFile.createNewFile();
        //用于写文件
        RandomAccessFile rafWrite = new RandomAccessFile(mergeFile, "rw");
        //指针指向文件顶端
        rafWrite.seek(0);
        //缓冲区大小
        byte[] bytes = new byte[1024 * 1024];
        //分块列表
        File[] files = new File(chunkPath).listFiles();
        //转成集合，便于排序
        List<File> fileList = Arrays.asList(files);
        //从小到大排序
        fileList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getName())));
        //合并文件
        for (File chunkFile : fileList) {
            RandomAccessFile rafRead = new RandomAccessFile(chunkFile, "rw");
            int len;
            while ((len = rafRead.read(bytes)) != -1) {
                rafWrite.write(bytes, 0, len);
            }
            rafRead.close();
        }
        rafWrite.close();

        //校验文件
        try (FileInputStream sfis = new FileInputStream(sourceFile);
             FileInputStream mfis = new FileInputStream(mergeFile)) {
            //取出原始文件的md5
            String originalMd5 = DigestUtils.md5Hex(sfis);
            String mergeMd5 = DigestUtils.md5Hex(mfis);
            if (originalMd5.equals(mergeMd5)) {
                System.out.println("合并文件成功");
            } else {
                System.out.println("合并文件失败");
            }
        }
    }
}
