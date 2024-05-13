/*
 * eiam-authentication-mail - Employee Identity and Access Management
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
package cn.topiam.employee.authentication.otp.mail.constant;

import static cn.topiam.employee.support.security.constant.SecurityConstants.LOGIN_PATH;

/**
 * 验证码认证常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/19 23:19
 */
public final class MailOtpAuthenticationConstants {

    public static final String RECIPIENT_KEY = "recipient";
    public static final String CODE_KEY      = "code";
    /**
     * 验证码登录路径
     */
    public static final String OTP_LOGIN     = LOGIN_PATH + "/otp";
}