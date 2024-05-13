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
package cn.topiam.employee.common.repository.app;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import cn.topiam.employee.common.entity.app.AppCertEntity;
import cn.topiam.employee.common.entity.app.AppOidcConfigEntity;
import cn.topiam.employee.common.enums.app.AppCertUsingType;
import static cn.topiam.employee.common.constant.AppConstants.APP_CERT_CACHE_NAME;

/**
 * AppCertificateRepository
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/31 21:52
 */
@CacheConfig(cacheNames = { APP_CERT_CACHE_NAME })
public interface AppCertRepository extends JpaRepository<AppCertEntity, String> {
    /**
     * 根据应用ID查询证书
     *
     * @param appId     {@link String}
     * @param usingType {@link AppCertUsingType}
     * @return {@link AppCertEntity}
     */
    @Cacheable(key = "#p1.code+':'+#p0", unless = "#result==null")
    Optional<AppCertEntity> findByAppIdAndUsingType(String appId, AppCertUsingType usingType);

    /**
     * save
     *
     * @param entity must not be {@literal null}.
     * @param <S>    {@link S}
     * @return {@link AppCertEntity}
     */
    @NotNull
    @Override
    @CacheEvict(allEntries = true)
    <S extends AppCertEntity> S save(@NotNull S entity);

    /**
     * delete
     *
     * @param id must not be {@literal null}.
     */
    @CacheEvict(allEntries = true)
    @Override
    void deleteById(@NotNull String id);

    /**
     * 根据应用ID删除应用
     *
     * @param appId {@link String}
     */
    @CacheEvict(allEntries = true)
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByAppId(@Param("appId") String appId);

    /**
     * find by id
     *
     * @param id must not be {@literal null}.
     * @return {@link AppOidcConfigEntity}
     */
    @NotNull
    @Override
    @Cacheable
    Optional<AppCertEntity> findById(@NotNull String id);
}
