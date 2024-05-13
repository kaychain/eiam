/*
 * eiam-authentication-feishu - Employee Identity and Access Management
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
package cn.topiam.employee.authentication.feishu;

import java.io.Serial;

import cn.topiam.employee.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * 飞书扫码 认证配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/19 22:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FeiShuIdentityProviderOAuth2Config extends IdentityProviderConfig {
    @Serial
    private static final long serialVersionUID = -6850223527422243076L;

    /**
     * APP ID
     */
    @NotBlank(message = "APP ID 不能为空")
    private String            appId;

    /**
     * APP Secret
     */
    @NotBlank(message = "APP Secret 不能为空")
    private String            appSecret;
}
