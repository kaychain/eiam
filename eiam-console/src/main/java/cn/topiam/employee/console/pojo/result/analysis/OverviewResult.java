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
package cn.topiam.employee.console.pojo.result.analysis;

import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 概述总计结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/11/22 23:16
 */
@Data
@Schema(description = "概述总计响应")
public class OverviewResult implements Serializable {

    /**
     * 今日认证量
     */
    @Schema(description = "今日认证量")
    private String todayAuthnCount;

    /**
     * 认证源总数
     */
    @Schema(description = "认证源总数")
    private String idpCount;

    /**
     * 用户数量
     */
    @Schema(description = "用户数量")
    private String userCount;

    /**
     * 应用数量
     */
    @Schema(description = "应用数量")
    private String appCount;
}
