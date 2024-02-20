package com.mewp.edu.media.test;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.j256.simplemagic.ContentType;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mewp
 * @version 1.0
 * @date 2023/11/4 13:21
 */
public class MinioTest {

    /**
     * 使用 MinIO 服务器 playground、其访问密钥和密钥创建一个 minioClient
     */
    MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://192.168.3.64:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    /**
     * 上传文件
     *
     * @throws Exception 异常信息
     */
    @Test
    void upload() throws Exception {
        try {
            // make 'asiatrip' bucket if not exist.
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("asiatrip").build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("asiatrip").build());
            } else {
                System.out.println("Bucket 'asiatrip' already exists.");
            }

            //根据扩展名取MineType
            ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".jar");
            String mimeType = ContentType.EMPTY.getMimeType();
            if (extensionMatch != null) {
                mimeType = extensionMatch.getMimeType();
            }

            //上传文件的参数信息
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket("asiatrip")     //桶名
                    .filename("/Users/mewp/Desktop/test/闪电项目/oss/oss-1.0.0.jar")    //文件路径
//                    .object("oss.jar")      //对象名，存储在根目录下
                    .object("test/1/oss.jar")      //对象名，存储在子目录下
                    .contentType(mimeType)         //默认根据扩展名确定文件内容类型，也可以指定
                    .build();
            //上传文件
            minioClient.uploadObject(uploadObjectArgs);
            System.out.println("上传成功");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }

    /**
     * 删除文件
     */
    @Test
    void remove() {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket("asiatrip")     //桶名
                    .object("oss.jar")      //对象名
                    .build();
            minioClient.removeObject(removeObjectArgs);
            System.out.println("删除成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询文件
     */
    @Test
    void getFile() {
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket("asiatrip")     //桶名
                    .object("asiaphotos-2015.zip")      //对象名
                    .build();
            //查询远程服务获取到一个流对象
            FilterInputStream fis = minioClient.getObject(getObjectArgs);
            FileOutputStream fos = new FileOutputStream("/Users/mewp/Desktop/test/temp/2015.zip");
            IOUtils.copy(fis, fos);

            //校验文件的完整性，对文件的内容进行MD5  fixme：该方法不正确
            String sourceMd5 = DigestUtils.md5Hex(fis);
            String targetMd5 = DigestUtils.md5Hex(Files
                    .newInputStream(Paths.get("/Users/mewp/Desktop/test/temp/2015.zip")));
            if (sourceMd5.equals(targetMd5)) {
                System.out.println("下载成功");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将分块文件上传到minio
     */
    @Test
    void uploadChunk() throws Exception {
        String chunkPath = "/Users/mewp/Desktop/test/temp/chunk/";
        File chunkFolder = new File(chunkPath);
        //分块文件
        File[] files = chunkFolder.listFiles();
        if (Objects.isNull(files) || files.length == 0) {
            return;
        }
        //将分块文件上传到minio
        for (int i = 0; i < files.length; i++) {
            //上传文件的参数信息
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket("asiatrip")     //桶名
                    .filename(files[i].getAbsolutePath())    //文件路径
                    .object("chunk/" + i)      //对象名，存储在子目录下
                    .build();
            minioClient.uploadObject(uploadObjectArgs);
            System.out.println("上传分块成功 " + i);
        }
    }

    /**
     * 合并文件，要求分块文件最小5M
     */
    @Test
    void uploadMerge() throws Exception {
        List<ComposeSource> sources = Stream.iterate(0, i -> ++i)
                .limit(5)
                .map(i -> ComposeSource.builder()
                        .bucket("asiatrip")
                        .object("chunk/".concat(Integer.toString(i)))
                        .build()
                ).collect(Collectors.toList());
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                .bucket("asiatrip")
                .object("merge01.mp4")
                .sources(sources)       //指定源文件
                .build();
        minioClient.composeObject(composeObjectArgs);
    }

    /**
     * 清除分块文件
     */
    @Test
    public void testRemoveObjects() {
        //合并分块完成将分块文件清除
        List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                .limit(7)
                .map(i -> new DeleteObject("chunk/".concat(Integer.toString(i))))
                .collect(Collectors.toList());

        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                .bucket("asiatrip")
                .objects(deleteObjects).build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
        results.forEach(r -> {
            DeleteError deleteError = null;
            try {
                deleteError = r.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
