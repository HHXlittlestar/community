package com.huhx.community.provider;

import com.google.gson.Gson;
import com.huhx.community.exception.CustomizeErrorCode;
import com.huhx.community.exception.CustomizeException;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
public class QiNiuFileProvider {
    @Value("${qiniu.access-key}")
    private String ACCESS_KEY;
    @Value("${qiniu.secret-key}")
    private String SECRET_KEY;
    @Value("${qiniu.domain-of-bucket}")
    private String domainOfBucket;
    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("${qiniu.expires}")
    private Long expires;

    public String upload(InputStream fileStream, String fileName) {
        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1){
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        }else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(bucket);
        try {
            Configuration cfg = new Configuration(Region.region0());
            UploadManager uploadManager = new UploadManager(cfg);
            Response response = uploadManager.put(fileStream, generatedFileName, upToken, null, null);
            //解析上传成功的结果
            if (response != null){
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                String publicUrl = String.format("%s/%s", domainOfBucket, putRet.key);
                return auth.privateDownloadUrl(publicUrl, expires);
            }else {
                throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
            }
        } catch (QiniuException ex) {
            ex.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
    }
}
