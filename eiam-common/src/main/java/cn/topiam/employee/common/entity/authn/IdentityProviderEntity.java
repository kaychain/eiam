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
package cn.topiam.employee.common.entity.authn;

import java.io.Serial;

import org.hibernate.annotations.SoftDelete;

import cn.topiam.employee.support.repository.SoftDeleteConverter;
import cn.topiam.employee.support.repository.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import static cn.topiam.employee.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * <p>
 * 社交身份认证源配置
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-16
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "eiam_identity_provider")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class IdentityProviderEntity extends BaseEntity {

    @Serial
    private static final long  serialVersionUID    = -7936931011805155568L;

    public static final String CATEGORY_FIELD_NAME = "category";

    public static final String NAME_FIELD_NAME     = "name";

    /**
     * 名称
     */
    @Column(name = "name_")
    private String             name;

    /**
     * 唯一CODE 不可修改
     */
    @Column(name = "code_")
    private String             code;

    /**
     * 平台
     */
    @Column(name = "type_")
    private String             type;

    /**
     * 分类
     */
    @Column(name = "category_")
    private String             category;

    /**
     * 配置JSON串
     */
    @Column(name = "config_")
    private String             config;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled")
    private Boolean            enabled;

    /**
     * 是否展示
     */
    @Column(name = "is_displayed")
    private Boolean            displayed;

}
