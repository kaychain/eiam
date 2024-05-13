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
package cn.topiam.employee.console.service.account;

import java.util.List;

import cn.topiam.employee.common.entity.account.OrganizationEntity;
import cn.topiam.employee.console.pojo.result.account.*;
import cn.topiam.employee.console.pojo.save.account.OrganizationCreateParam;
import cn.topiam.employee.console.pojo.update.account.OrganizationUpdateParam;

/**
 * <p>
 * 组织架构 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-09
 */
public interface OrganizationService {

    /**
     * 创建组织架构
     *
     * @param param {@link OrganizationCreateParam}
     * @return {@link Boolean}
     */
    Boolean createOrg(OrganizationCreateParam param);

    /**
     * 修改组织架构
     *
     * @param param {@link OrganizationUpdateParam}
     * @return {@link Boolean}
     */
    Boolean updateOrg(OrganizationUpdateParam param);

    /**
     * 启用/禁用
     *
     * @param id      {@link String}
     * @param enabled {@link Boolean}
     * @return {@link Boolean}
     */
    Boolean updateStatus(String id, boolean enabled);

    /**
     * 删除组织架构
     *
     * @param id {@link List}
     * @return {@link Boolean}
     */
    Boolean deleteOrg(String id);

    /**
     * 根据ID查询组织架构
     *
     * @param id {@link String}
     * @return {@link OrganizationResult}
     */
    OrganizationResult getOrganization(String id);

    /**
     * 移动组织机构
     *
     * @param id       {@link String}
     * @param parentId {@link String}
     * @return {@link Boolean}
     */
    Boolean moveOrganization(String id, String parentId);

    /**
     * 查询根组织
     *
     * @return {@link OrganizationRootResult}
     */
    OrganizationRootResult getRootOrganization();

    /**
     * 查询子组织
     *
     * @param parentId {@link String}
     * @return {@link OrganizationChildResult}
     */
    List<OrganizationChildResult> getChildOrganization(String parentId);

    /**
     * 查询子组织
     *
     * @param parentId         {@link String}
     * @param dataOrigin       {@link String}
     * @param identitySourceId {@link String}
     * @return {@link OrganizationEntity}
     */
    List<OrganizationEntity> getChildOrgList(String parentId, String dataOrigin,
                                             String identitySourceId);

    /**
     * 过滤组织树
     *
     * @param keyWord {@link String} 关键字
     * @return {@link List}
     */
    List<SearchOrganizationTreeResult> searchOrganizationTree(String keyWord);

    /**
     * 过滤组织
     *
     * @param keyWord {@link String} 关键字
     * @return {@link List}
     */
    List<SearchOrganizationResult> searchOrganization(String keyWord);

    /**
     * 根据ID查询组织架构
     *
     * @param id {@link String}
     * @return {@link OrganizationEntity}
     */
    OrganizationEntity getById(String id);

    /**
     * 根据外部ID查询组织架构
     *
     * @param id               {@link String}
     * @param identitySourceId {@link String}
     * @return {@link OrganizationEntity}
     */
    OrganizationEntity getOrganizationByExternalId(String id, String identitySourceId);

    /**
     * 批量获取组织
     *
     * @param ids {@link List}
     * @return  {@link List}
     */
    List<BatchOrganizationResult> batchGetOrganization(List<String> ids);
}
