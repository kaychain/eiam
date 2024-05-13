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
package cn.topiam.employee.common.entity.account;

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
 * 用户组表
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-07-31
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "eiam_user_group")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class UserGroupEntity extends BaseEntity {

    @Serial
    private static final long  serialVersionUID = -2619231849746900857L;

    public static final String CODE_FIELD_NAME  = "code";

    public static final String NAME_FIELD_NAME  = "name";

    /**
     * 用户名
     */
    @Column(name = "name_")
    private String             name;

    /**
     * 密码
     */
    @Column(name = "code_")
    private String             code;
}
