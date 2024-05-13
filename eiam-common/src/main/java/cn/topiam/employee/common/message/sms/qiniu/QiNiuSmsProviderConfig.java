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
package cn.topiam.employee.common.message.sms.qiniu;

import cn.topiam.employee.common.jackjson.encrypt.JsonPropertyEncrypt;
import cn.topiam.employee.common.message.sms.SmsProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;

/**
 * 验证码提供商配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QiNiuSmsProviderConfig extends SmsProviderConfig {
    public QiNiuSmsProviderConfig() {
    }

    /**
     * accessKey
     */
    @NotEmpty(message = "accessKey不能为空")
    private String accessKey;
    /**
     * secretKey
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "secretKey不能为空")
    private String secretKey;
}
