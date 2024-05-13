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
package cn.topiam.employee.console.authentication;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;

import cn.topiam.employee.support.security.password.authentication.NeedChangePasswordAuthenticationToken;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/3/22 21:02
 */
public class AuthenticationTrustResolverImpl implements AuthenticationTrustResolver {
    private final AuthenticationTrustResolver delegate = new org.springframework.security.authentication.AuthenticationTrustResolverImpl();

    @Override
    public boolean isAnonymous(Authentication authentication) {
        return this.delegate.isAnonymous(authentication)
               || (authentication instanceof NeedChangePasswordAuthenticationToken);
    }

    @Override
    public boolean isRememberMe(Authentication authentication) {
        return this.delegate.isRememberMe(authentication);
    }
}
