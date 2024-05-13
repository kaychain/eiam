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
package cn.topiam.employee.console.converter.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import cn.topiam.employee.common.entity.account.OrganizationEntity;
import cn.topiam.employee.console.pojo.result.account.*;
import cn.topiam.employee.console.pojo.save.account.OrganizationCreateParam;
import cn.topiam.employee.console.pojo.update.account.OrganizationUpdateParam;

/**
 * 组织架构数据映射
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/14 21:25
 */
@Mapper(componentModel = "spring")
public interface OrganizationConverter {

    /**
     * 组织实体转换为List结果
     *
     * @param data {@link List}
     * @return {@link List}
     */
    default List<OrganizationChildResult> entityConvertToChildOrgListResult(List<OrganizationEntity> data) {
        List<OrganizationChildResult> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(data)) {
            for (OrganizationEntity entity : data) {
                list.add(entityConvertToChildOrgListResult(entity));
            }
        }
        return list;
    }

    /**
     * 组织实体转换为TreeList结果
     *
     * @param data     {@link List}
     * @param parentId {@link String}
     * @return {@link List}
     */
    default List<SearchOrganizationTreeResult> entityConvertToChildOrgTreeListResult(String parentId,
                                                                                     List<OrganizationEntity> data) {
        List<SearchOrganizationTreeResult> list = new ArrayList<>();
        List<OrganizationEntity> entityList = data.stream()
            .filter(one -> Objects.equals(parentId, one.getParentId())).toList();
        for (OrganizationEntity organizationEntity : entityList) {
            SearchOrganizationTreeResult result = entityConvertToChildOrgTreeListResult(
                organizationEntity);
            result.setChildren(
                entityConvertToChildOrgTreeListResult(organizationEntity.getId(), data));
            list.add(result);
        }
        return list;
    }

    /**
     * 组织实体转换为组织分页结果
     *
     * @param data {@link OrganizationEntity}
     * @return {@link OrganizationResult}
     */
    @Mapping(target = "type", source = "type.desc")
    @Mapping(target = "dataOrigin", source = "dataOrigin")
    OrganizationChildResult entityConvertToChildOrgListResult(OrganizationEntity data);

    /**
     * 组织实体转换为组织分页结果
     *
     * @param data {@link OrganizationEntity}
     * @return {@link OrganizationResult}
     */
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "type", source = "type.desc")
    @Mapping(target = "dataOrigin", source = "dataOrigin")
    SearchOrganizationTreeResult entityConvertToChildOrgTreeListResult(OrganizationEntity data);

    /**
     * 组织实体转换为组织分页结果
     *
     * @param data {@link OrganizationEntity}
     * @return {@link OrganizationResult}
     */
    @Mapping(target = "type", source = "type.desc")
    @Mapping(target = "dataOrigin", source = "dataOrigin")
    OrganizationRootResult entityConvertToRootOrgListResult(OrganizationEntity data);

    /**
     * 组织创建参数转换为组织实体
     *
     * @param param {@link OrganizationCreateParam}
     * @return {@link OrganizationEntity}
     */

    @Mapping(target = "identitySourceId", ignore = true)
    @Mapping(target = "path", ignore = true)
    @Mapping(target = "displayPath", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataOrigin", expression = "java(cn.topiam.employee.support.security.userdetails.DataOrigin.INPUT.getType())")
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "leaf", expression = "java(Boolean.TRUE)")
    @Mapping(target = "enabled", expression = "java(Boolean.TRUE)")
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "order", source = "order", defaultValue = "9999L")
    @Mapping(target = "parentId", source = "parentId", defaultExpression = "java(cn.topiam.employee.support.constant.EiamConstants.ROOT_NODE)")
    @Mapping(target = "externalId", source = "externalId", defaultExpression = "java(new org.springframework.util.JdkIdGenerator().generateId().toString())")
    OrganizationEntity orgCreateParamConvertToEntity(OrganizationCreateParam param);

    /**
     * 组织修改参数转换为组织实体
     *
     * @param param {@link OrganizationUpdateParam}
     * @return {@link OrganizationEntity}
     */

    @Mapping(target = "identitySourceId", ignore = true)
    @Mapping(target = "path", ignore = true)
    @Mapping(target = "displayPath", ignore = true)
    @Mapping(target = "dataOrigin", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "leaf", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    OrganizationEntity orgUpdateParamConvertToEntity(OrganizationUpdateParam param);

    /**
     * 实体转组织详情结果
     *
     * @param organization {@link OrganizationEntity}
     * @return {@link OrganizationResult}
     */
    OrganizationResult entityConvertToGetOrganizationResult(OrganizationEntity organization);

    /**
     * 实体转批量获取组织结果
     *
     * @param organization {@link OrganizationEntity}
     * @return {@link OrganizationResult}
     */
    BatchOrganizationResult entityConvertToBatchGetOrganizationResult(OrganizationEntity organization);

    /**
     * 实体转批量获取组织结果
     *
     * @param list {@link List}
     * @return {@link List}
     */
    default List<BatchOrganizationResult> entityConvertToBatchGetOrganizationResult(List<OrganizationEntity> list) {
        return list.stream().map(this::entityConvertToBatchGetOrganizationResult).toList();
    }

    /**
     * 实体转搜索组织结果
     *
     * @param list {@link OrganizationEntity}
     * @return {@link List}
     */
    List<SearchOrganizationResult> entityConvertToSearchOrganizationResult(List<OrganizationEntity> list);
}
