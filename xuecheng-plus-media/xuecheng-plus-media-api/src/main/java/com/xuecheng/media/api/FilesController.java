package com.xuecheng.media.api;

import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 */

@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/upload")
public class FilesController {

    @Autowired
    MediaFileService mediaFileService;

    @ApiOperation("上传文件")
    @PostMapping(value = "/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MediaFiles upload(@RequestPart("filedata") MultipartFile filedata,
                             @RequestParam(value = "objectName", required = false) String objectName) {
        return mediaFileService.uploadFile(filedata, objectName);
    }

    @ApiOperation(value = "文件上传前检查文件")
    @PostMapping("/checkfile")
    public RestResponse<Boolean> checkfile(@RequestParam("fileMd5") String fileMd5) {
        return mediaFileService.checkFile(fileMd5);
    }


    @ApiOperation(value = "分块文件上传前的检测")
    @PostMapping("/checkchunk")
    public RestResponse<Boolean> checkchunk(@RequestParam("fileMd5") String fileMd5, @RequestParam("chunk") Integer chunk) {
        return mediaFileService.checkChunk(fileMd5, chunk);
    }

    @ApiOperation(value = "上传分块文件")
    @PostMapping("/uploadchunk")
    public RestResponse<Boolean> uploadchunk(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileMd5") String fileMd5,
            @RequestParam("chunk") Integer chunk) {

        return mediaFileService.uploadChunk(file, fileMd5, chunk);
    }

    @ApiOperation(value = "合并文件")
    @PostMapping("/mergechunks")
    public RestResponse<Boolean> mergechunks(
            @RequestParam("fileMd5") String fileMd5,
            @RequestParam("fileName") String fileName,
            @RequestParam("chunkTotal") int chunkTotal){
        Long companyId = 1232141425L;
        return mediaFileService.mergechunks(companyId, fileMd5, fileName, chunkTotal);

    }
}
