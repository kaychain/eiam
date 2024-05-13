/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Jinan Yuanchuang Network Technology Co., Ltd. (support@topiam.cn)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.topiam.employee.common.storage.impl;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.jetbrains.annotations.NotNull;

import com.alibaba.fastjson2.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import cn.topiam.employee.common.jackjson.encrypt.JsonPropertyEncrypt;
import cn.topiam.employee.common.storage.AbstractStorage;
import cn.topiam.employee.common.storage.StorageConfig;
import cn.topiam.employee.common.storage.StorageProviderException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.constraints.NotEmpty;

/**
 * 七牛kodo
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/10 21:33
 */
@Slf4j
public class QiNiuKodoStorage extends AbstractStorage {

    private final UploadManager uploadManager;
    private final Config        qiNiuConfig;

    public QiNiuKodoStorage(StorageConfig config) {
        super(config);
        qiNiuConfig = (Config) this.config.getConfig();
        Configuration cfg = new Configuration(Region.createWithRegionId("z0"));
        uploadManager = new UploadManager(cfg);
    }

    @Override
    public String upload(@NotNull String fileName,
                         InputStream inputStream) throws StorageProviderException {
        try {
            super.upload(fileName, inputStream);
            Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
            String upToken = auth.uploadToken(qiNiuConfig.getBucket());
            Response response = uploadManager.put(inputStream,
                qiNiuConfig.getLocation() + SEPARATOR + getFileName(fileName), upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            log.info("qi niu upload response: {}", putRet);
            return qiNiuConfig.getDomain() + SEPARATOR
                   + URLEncoder.encode(putRet.key, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error("qi niu upload fail response: {}", r.toString());
            try {
                log.error("qi niu upload fail response body： {}", r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            throw new StorageProviderException("qiu niu upload exception", ex);
        } catch (Exception e) {
            throw new StorageProviderException("qiu niu upload exception", e);
        }
    }

    @Override
    public String download(String path) throws StorageProviderException {
        try {
            super.download(path);
            DownloadUrl url = new DownloadUrl(qiNiuConfig.getDomain(),
                getUrlSecure(qiNiuConfig.getDomain()), path);
            Auth auth = Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
            // 1小时，可以自定义链接过期时间
            return url.buildURL(auth, EXPIRY_SECONDS);
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error("qi niu download fail response: {}", r.toString());
            try {
                log.error("qi niu download fail response body： {}", r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            throw new StorageProviderException("qiu niu download exception", ex);
        } catch (Exception e) {
            throw new StorageProviderException("qiu niu download exception", e);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Config extends StorageConfig.Config {
        /**
         * AccessKey
         */
        @NotEmpty(message = "AccessKey不能为空")
        private String accessKey;
        /**
         * SecretKey
         */
        @JsonPropertyEncrypt
        @NotEmpty(message = "SecretKey不能为空")
        private String secretKey;
        /**
         * bucket
         */
        @NotEmpty(message = "Bucket不能为空")
        private String bucket;
    }
}
