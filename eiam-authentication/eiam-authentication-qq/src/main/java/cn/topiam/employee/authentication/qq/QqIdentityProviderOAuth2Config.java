/*
 * eiam-authentication-qq - Employee Identity and Access Management
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
package cn.topiam.employee.authentication.qq;

import java.io.Serial;

import cn.topiam.employee.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * QQ 认证配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/4 23:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QqIdentityProviderOAuth2Config extends IdentityProviderConfig {
    @Serial
    private static final long serialVersionUID = -6850223527422243076L;

    /**
     * APP ID
     */
    @NotBlank(message = "APP ID 不能为空")
    private String            appId;

    /**
     * APP Key
     */
    @NotBlank(message = "APP KEY 不能为空")
    private String            appKey;
}
