package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.po.MediaFiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 媒资文件管理业务类
 */
public interface MediaFileService {

    /**
     * 媒资文件查询方法
     */
    PageResult<MediaFiles> queryMediaFiels(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);


    /**
     * 上传文件
     */
    MediaFiles uploadFile(MultipartFile filedata, String objectName);


    /**
     * 检查文件
     */
    RestResponse<Boolean> checkFile(String fileMd5);

    /**
     * 检查分块
     */
    RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex);

    /**
     * 上传分块
     */
    RestResponse<Boolean> uploadChunk(MultipartFile file, String fileMd5, Integer chunk);

    /**
     * 合并分块
     */
    RestResponse<Boolean> mergechunks(Long companyId, String fileMd5, String fileName, int chunkTotal);

    /**
     * 从minio下载文件
     */
    File downloadFileFromMinIO(String bucket, String objectName);

    /**
     * 将文件上传到minio
     */
    boolean addMediaFilesToMinIO(String localFilePath,String mimeType,String bucket, String objectName);

    /**
     * 根据媒资id查询文件信息
     */
    MediaFiles getFileById(String mediaId);

    /**
     * 信息入库
     */
    MediaFiles addMediaFilesToDb(Long companyId, String fileMd5, UploadFileParamsDto uploadFileParamsDto, String bucket, String objectName);





}
