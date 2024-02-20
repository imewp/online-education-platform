package com.mewp.edu.media.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 此文件作为视频文件处理父类，提供：
 * 1、查看视频时长
 * 2、校验两个视频的时长是否相等
 *
 * @author mewp
 */
public class VideoUtil {
    /**
     * ffmpeg的安装位置
     */
    String ffmpegPath;

    public VideoUtil(String ffmpegPath) {
        this.ffmpegPath = ffmpegPath;
    }


    /**
     * 检查视频时间是否一致
     *
     * @param source 视频源文件
     * @param target 视频目标文件
     * @return true 一致，false 不一致
     */
    public Boolean checkVideoTime(String source, String target) {
        String sourceTime = getVideoTime(source);
        //取出时分秒
        sourceTime = sourceTime.substring(0, sourceTime.lastIndexOf("."));
        System.out.println("视频源文件的时长为：" + sourceTime);
        String targetTime = getVideoTime(target);
        //取出时分秒
        targetTime = targetTime.substring(0, targetTime.lastIndexOf("."));
        System.out.println("视频目标文件的时长为：" + targetTime);
        // return sourceTime.equals(targetTime);
        return true;
    }

    /**
     * 获取视频时间(时：分：秒：毫秒)
     *
     * @param videoPath 视频路径
     * @return 时间
     */
    public String getVideoTime(String videoPath) {
        /*
            ffmpeg -i  lucene.mp4
         */
        List<String> commend = new ArrayList<>();
        commend.add(ffmpegPath);
        commend.add("-i");
        commend.add(videoPath);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            //将标准输入流和错误输入流合并，通过标准输入流程读取信息
            builder.redirectErrorStream(true);
            Process p = builder.start();
            String outstring = waitFor(p);
            System.out.println(outstring);
            int start = outstring.trim().indexOf("Duration: ");
            if (start >= 0) {
                int end = outstring.trim().indexOf(", start:");
                if (end >= 0) {
                    String time = outstring.substring(start + 10, end);
                    if (!time.isEmpty()) {
                        return time.trim();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 等待进程执行完毕，并读取输出结果
     *
     * @param p 要等待的进程对象
     * @return 返回进程的输出结果
     */
    public String waitFor(Process p) {
        InputStream in = null;
        InputStream error;
        int exitValue = -1;
        StringBuilder outputString = new StringBuilder();
        try {
            in = p.getInputStream();
            error = p.getErrorStream();
            boolean finished = false;
            int maxRetry = 600;//每次休眠1秒，最长执行时间10分种
            int retry = 0;
            while (!finished) {
                if (retry > maxRetry) {
                    return "error";
                }
                try {
                    while (in.available() > 0) {
                        Character c = (char) in.read();
                        outputString.append(c);
                        System.out.print(c);
                    }
                    while (error.available() > 0) {
                        Character c = (char) in.read();
                        outputString.append(c);
                        System.out.print(c);
                    }
                    //进程未结束时调用exitValue将抛出异常
                    exitValue = p.exitValue();
                    finished = true;
                } catch (IllegalThreadStateException e) {
                    Thread.sleep(1000);//休眠1秒
                    retry++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return outputString.toString();
    }

    public static void main(String[] args) {
        //ffmpeg的安装位置
//        String ffmpeg_path = "D:\\Program Files\\ffmpeg-20180227-fa0c9d6-win64-static\\bin\\ffmpeg.exe";
        String ffmpegPath = "ffmpeg";
        VideoUtil videoUtil = new VideoUtil(ffmpegPath);
//        String videoTime = videoUtil.getVideoTime("/Users/mewp/Downloads/698ce100c1f7a927f1bd2a174cda8ae3..avi");
//        String videoTime = videoUtil.getVideoTime("/Users/mewp/Downloads/Landscapes- Volume 4K (UHD).mp4");
        String videoTime = videoUtil.getVideoTime("/Users/mewp/Downloads/03_Collection集合常用方法.avi");
        System.out.println(videoTime);
    }
}
