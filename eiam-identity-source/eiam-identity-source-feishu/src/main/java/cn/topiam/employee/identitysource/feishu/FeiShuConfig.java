/*
 * eiam-identity-source-feishu - Employee Identity and Access Management
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
package cn.topiam.employee.identitysource.feishu;

import java.io.Serial;

import cn.topiam.employee.common.jackjson.encrypt.JsonPropertyEncrypt;
import cn.topiam.employee.identitysource.core.IdentitySourceConfig;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;

/**
 * 飞书配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/28 23:17
 */
@Getter
@Setter
public class FeiShuConfig extends IdentitySourceConfig {

    @Serial
    private static final long serialVersionUID = -834514351515253275L;
    /**
     * 应用App key
     */
    @NotEmpty(message = "AppId不能为空")
    private String            appId;

    /**
     * 应用AppSecret
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "AppSecret不能为空")
    private String            appSecret;

    /**
     * encryptKey
     */
    @JsonPropertyEncrypt
    private String            encryptKey;

    /**
     * verificationToken
     */
    @JsonPropertyEncrypt
    private String            verificationToken;
}
