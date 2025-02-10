package com.xuecheng.media.service.jobhandler;

import com.xuecheng.base.utils.Mp4VideoUtil;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileProcessService;
import com.xuecheng.media.service.MediaFileService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 视频处理任务类
 */
@Slf4j
@Component
public class VideoTask {

    @Autowired
    MediaFileService mediaFileService;

    @Autowired
    MediaFileProcessService mediaFileProcessService;

    @Value("${videoprocess.ffmpegpath}")
    String ffmpegpath;

    @XxlJob("videoJobHandler")
    public void videoJobHandler() throws Exception {

        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        List<MediaProcess> mediaProcessList = null;
        int size = 0;

        try {
            // 取出cpu核心数作为一次处理数据的条数
            int processors = Runtime.getRuntime().availableProcessors();

            // 一次处理视频数量不要超过cpu核心数
            mediaProcessList = mediaFileProcessService.getMediaProcessList(shardIndex, shardTotal, processors);

            size = mediaProcessList.size();
            log.debug("取出待处理视频任务{}条", size);
            if (size == 0) return;

        } catch (Exception e) {
            log.error("取出待处理视频任务异常：{}", e.getMessage());
            return;
        }

        // 启动size个线程的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(size);

        // 计数器
        CountDownLatch countDownLatch = new CountDownLatch(size);

        // 将处理任务加入线程池
        mediaProcessList.forEach(mediaProcess -> {
            threadPool.execute(() -> {
                //任务id
                Long taskId = mediaProcess.getId();
                //抢占任务,修改数据库状态
                if (!mediaFileProcessService.startTask(taskId)) {
                    return;
                }
                log.debug("开始执行任务:{}", mediaProcess);

                File originalFile = null;
                File mp4File = null;
                try {
                    //下边是处理逻辑
                    String bucket = mediaProcess.getBucket();
                    String filePath = mediaProcess.getFilePath();
                    String fileId = mediaProcess.getFileId();
                    String filename = mediaProcess.getFilename();

                    //将要处理的文件下载到服务器上
                    originalFile = mediaFileService.downloadFileFromMinIO(bucket, filePath);
                    if (originalFile == null) {
                        log.debug("下载待处理文件失败,originalFile:{}", bucket.concat(filePath));
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "下载待处理文件失败");
                        return;
                    }

                    //处理下载的视频文件
                    try {
                        mp4File = File.createTempFile("tmp", ".mp4");
                    } catch (IOException e) {
                        log.error("创建mp4临时文件失败");
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "创建mp4临时文件失败");
                        return;
                    }


                    //视频处理结果
                    String result = "";
                    try {

                        String originalFilePath = originalFile.getAbsolutePath();
                        String mp4FilePath = mp4File.getAbsolutePath();
                        //开始处理视频
                        Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpegpath, originalFilePath, mp4FilePath);
                        //开始视频转换，成功将返回success
                        result = videoUtil.generateMp4();

                    } catch (Exception e) {
                        log.error("处理视频文件:{},出错:{}", mediaProcess.getFilePath(), e.getMessage());
                    }

                    if (!result.equals("success")) {
                        //记录错误信息
                        log.error("处理视频失败,视频地址:{},错误信息:{}", bucket + filePath, result);
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, result);
                        return;
                    }


                    //将mp4上传至minio
                    //mp4在minio的存储路径
                    String objectName = getFilePath(fileId);
                    //访问url
                    String url = "/" + bucket + "/" + objectName;
                    String localPath = mp4File.getAbsolutePath();
                    try {
                        mediaFileService.addMediaFilesToMinIO(localPath, "video/mp4", bucket, objectName);

                        //将url存储至数据，并更新状态为成功，并将待处理视频记录删除存入历史
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "2", fileId, url, null);
                    } catch (Exception e) {
                        log.error("上传视频失败或入库失败,视频地址:{},错误信息:{}", bucket + objectName, e.getMessage());
                        mediaFileProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "处理后视频上传或入库失败");
                    }
                } finally {
                    if (originalFile != null) {
                        originalFile.delete();
                    }
                    if (mp4File != null) {
                        mp4File.delete();
                    }
                    countDownLatch.countDown();
                }

            });
        });
        //等待，给一个充裕的超时时间，防止无限等待，到达超时时间还没有处理完成则结束任务
        boolean await = countDownLatch.await(30, TimeUnit.MINUTES);
        // TODO 超时之后做一些处理，把状态设置成3，
        if (!await){
            log.error("任务处理超时，任务结束");
        }
    }

    /**
     * 获取mp4文件存储路径
     */
    private String getFilePath(String fileMd5) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + fileMd5 + ".mp4";
    }

}