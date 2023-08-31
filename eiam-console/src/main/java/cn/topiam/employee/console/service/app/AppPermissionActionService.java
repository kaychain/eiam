/*
 * eiam-console - Employee Identity and Access Management
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
package cn.topiam.employee.console.service.app;

import java.util.List;

import cn.topiam.employee.console.pojo.query.app.AppPermissionActionListQuery;
import cn.topiam.employee.console.pojo.result.app.AppPermissionActionGetResult;
import cn.topiam.employee.console.pojo.result.app.AppPermissionActionListResult;
import cn.topiam.employee.console.pojo.save.app.AppPermissionActionCreateParam;
import cn.topiam.employee.console.pojo.update.app.AppPermissionActionUpdateParam;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on  2020-08-10
 */
public interface AppPermissionActionService {

    /**
     * 获取资源权限列表
     *
     * @param query {@link AppPermissionActionListQuery}
     * @return {@link AppPermissionActionListResult}
     */
    List<AppPermissionActionListResult> getPermissionActionList(AppPermissionActionListQuery query);

    /**
     * 获取权限详情
     *
     * @param id {@link String}
     * @return {@link AppPermissionActionGetResult}
     */
    AppPermissionActionGetResult getPermissionAction(String id);

    /**
     * 删除权限
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean deletePermissionAction(String id);

    /**
     * 创建权限
     *
     * @param param {@link AppPermissionActionCreateParam}
     * @return {@link Boolean}
     */
    Boolean createPermissionAction(AppPermissionActionCreateParam param);

    /**
     * 更新权限
     *
     * @param param {@link AppPermissionActionUpdateParam}
     * @return {@link Boolean}
     */
    Boolean updatePermissionAction(AppPermissionActionUpdateParam param);
}
