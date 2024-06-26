/*
 * eiam-authentication-alipay - Employee Identity and Access Management
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
package cn.topiam.employee.authentication.alipay.configurer;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import cn.topiam.employee.authentication.alipay.filter.AlipayAuthorizationRequestRedirectFilter;
import cn.topiam.employee.authentication.alipay.filter.AlipayLoginAuthenticationFilter;
import cn.topiam.employee.authentication.common.IdentityProviderAuthenticationService;
import cn.topiam.employee.authentication.common.client.RegisteredIdentityProviderClientRepository;

import lombok.NonNull;
import lombok.Setter;
import static cn.topiam.employee.support.security.util.HttpSecurityFilterOrderRegistrationUtils.putFilterBefore;

/**
 * 认证配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/19 15:52
 */
public class AlipayAuthenticationConfigurer extends
                                            AbstractAuthenticationFilterConfigurer<HttpSecurity, AlipayAuthenticationConfigurer, AlipayLoginAuthenticationFilter> {
    @Setter
    @NonNull
    private String                                           loginProcessingUrl = AlipayLoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;

    private final RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository;
    private final IdentityProviderAuthenticationService      identityProviderAuthenticationService;

    AlipayAuthenticationConfigurer(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                   IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        Assert.notNull(registeredIdentityProviderClientRepository,
            "registeredIdentityProviderClientRepository must not be null");
        Assert.notNull(identityProviderAuthenticationService, "userIdpService must not be null");
        this.registeredIdentityProviderClientRepository = registeredIdentityProviderClientRepository;
        this.identityProviderAuthenticationService = identityProviderAuthenticationService;
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        //支付宝登录认证
        this.setAuthenticationFilter(new AlipayLoginAuthenticationFilter(
            identityProviderAuthenticationService, registeredIdentityProviderClientRepository));
        putFilterBefore(http, this.getAuthenticationFilter(),
            OAuth2LoginAuthenticationFilter.class);

        //支付宝请求重定向
        http.addFilterBefore(
            new AlipayAuthorizationRequestRedirectFilter(
                registeredIdentityProviderClientRepository),
            OAuth2AuthorizationRequestRedirectFilter.class);

        //登录处理地址
        super.loginProcessingUrl(this.loginProcessingUrl);
        super.init(http);
    }

    /**
     * Create the {@link RequestMatcher} given a loginProcessingUrl
     *
     * @param loginProcessingUrl creates the {@link RequestMatcher} based upon the
     *                           loginProcessingUrl
     * @return the {@link RequestMatcher} to use based upon the loginProcessingUrl
     */
    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.GET.name());
    }

    public RequestMatcher getRequestMatcher() {
        return new OrRequestMatcher(AlipayAuthorizationRequestRedirectFilter.getRequestMatcher(),
            AlipayLoginAuthenticationFilter.getRequestMatcher());
    }

    public static AlipayAuthenticationConfigurer alipayOauth(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                                             IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        return new AlipayAuthenticationConfigurer(registeredIdentityProviderClientRepository,
            identityProviderAuthenticationService);
    }
}
