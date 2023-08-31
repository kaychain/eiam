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
package cn.topiam.employee.openapi.service;

import cn.topiam.employee.openapi.pojo.request.app.query.AppPermissionRoleListQuery;
import cn.topiam.employee.openapi.pojo.response.app.AppPermissionRoleListResult;
import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.repository.page.domain.PageModel;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on  2020-08-10
 */
public interface AppPermissionRoleService {

    /**
     * 获取所有角色（分页）
     *
     * @param page  {@link PageModel}
     * @param query {@link AppPermissionRoleListQuery}
     * @return {@link AppPermissionRoleListResult}
     */
    Page<AppPermissionRoleListResult> getPermissionRoleList(PageModel page,
                                                            AppPermissionRoleListQuery query);
}
