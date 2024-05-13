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
package cn.topiam.employee.common.entity.identitysource;

import java.time.LocalDateTime;

import org.hibernate.annotations.SoftDelete;

import cn.topiam.employee.common.enums.SyncStatus;
import cn.topiam.employee.common.enums.identitysource.IdentitySourceActionType;
import cn.topiam.employee.common.enums.identitysource.IdentitySourceObjectType;
import cn.topiam.employee.support.repository.SoftDeleteConverter;
import cn.topiam.employee.support.repository.base.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import static cn.topiam.employee.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * 身份源事件记录
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/22 23:51
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "eiam_identity_source_event_record")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class IdentitySourceEventRecordEntity extends BaseEntity {

    public static final String       IDENTITY_SOURCE_ID_FIELD_NAME = "identitySourceId";

    public static final String       ACTION_TYPE_FIELD_NAME        = "actionType";

    public static final String       OBJECT_TYPE_FIELD_NAME        = "objectType";

    public static final String       STATUS_FIELD_NAME             = "status";

    /**
     * 身份源ID
     */
    @Column(name = "identity_source_id")
    private String                   identitySourceId;

    /**
     * 动作类型
     */
    @Column(name = "action_type")
    private IdentitySourceActionType actionType;

    /**
     * 对象ID
     */
    @Column(name = "object_id")
    private String                   objectId;

    /**
     * 对象名称
     */
    @Column(name = "object_name")
    private String                   objectName;

    /**
     * 对象类型
     */
    @Column(name = "object_type")
    private IdentitySourceObjectType objectType;

    /**
     * 事件时间
     */
    @Column(name = "event_time")
    private LocalDateTime            eventTime;

    /**
     * 事件状态
     */
    @Column(name = "status_")
    private SyncStatus               status;

    /**
     * 描述
     */
    @Column(name = "desc_")
    private String                   desc;
}
