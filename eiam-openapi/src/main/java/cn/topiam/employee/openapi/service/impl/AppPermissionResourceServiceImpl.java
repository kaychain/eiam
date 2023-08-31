/*
 * eiam-openapi - Employee Identity and Access Management
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
package cn.topiam.employee.openapi.service.impl;

import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import cn.topiam.employee.common.entity.app.AppPermissionResourceEntity;
import cn.topiam.employee.common.repository.app.AppPermissionResourceRepository;
import cn.topiam.employee.openapi.converter.app.AppPermissionResourceConverter;
import cn.topiam.employee.openapi.pojo.request.app.query.AppResourceListQuery;
import cn.topiam.employee.openapi.pojo.request.app.query.OpenApiPolicyQuery;
import cn.topiam.employee.openapi.pojo.response.app.AppPermissionResourceListResult;
import cn.topiam.employee.openapi.service.AppPermissionResourceService;
import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.repository.page.domain.PageModel;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 资源权限 服务实现类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on  2020-08-10
 */
@Service
@RequiredArgsConstructor
public class AppPermissionResourceServiceImpl implements AppPermissionResourceService {

    /**
     * 获取资源列表
     *
     * @param page  {@link PageModel}
     * @param query {@link OpenApiPolicyQuery}
     * @return {@link AppPermissionResourceListResult}
     */
    @Override
    public Page<AppPermissionResourceListResult> getPermissionResourceList(PageModel page,
                                                                           AppResourceListQuery query) {
        org.springframework.data.domain.Page<AppPermissionResourceEntity> data;
        Predicate predicate = appPermissionResourceConverter
            .resourcePaginationParamConvertToPredicate(query);
        QPageRequest request = QPageRequest.of(page.getCurrent(), page.getPageSize());
        data = appResourceRepository.findAll(predicate, request);
        return appPermissionResourceConverter.entityConvertToResourceListResult(data);
    }

    private final AppPermissionResourceConverter  appPermissionResourceConverter;

    private final AppPermissionResourceRepository appResourceRepository;
}
