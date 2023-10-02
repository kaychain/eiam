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
package cn.topiam.employee.audit.repository.result;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 审计统计
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/11/22 23:16
 */
@Data
@AllArgsConstructor
@Schema(description = "审计统计")
public class AuditRankResult implements Serializable {

    /**
     * 应用id
     */
    @Schema(description = "应用id")
    private String id;

    /**
     * 访问次数
     */
    @Schema(description = "访问次数")
    private Long   count;
}
