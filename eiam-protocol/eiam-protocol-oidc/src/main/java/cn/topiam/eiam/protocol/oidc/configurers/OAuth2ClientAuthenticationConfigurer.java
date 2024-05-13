/*
 * eiam-protocol-oidc - Employee Identity and Access Management
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
package cn.topiam.eiam.protocol.oidc.configurers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.ClientSecretAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.JwtClientAssertionAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.PublicClientAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.web.OAuth2ClientAuthenticationFilter;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.topiam.eiam.protocol.oidc.authorization.client.OidcConfigRegisteredClientRepositoryWrapper;
import cn.topiam.employee.common.constant.ProtocolConstants;
import cn.topiam.employee.protocol.code.EndpointMatcher;
import cn.topiam.employee.protocol.code.configurer.AbstractConfigurer;
import cn.topiam.employee.support.security.crypto.password.NoOpPasswordEncoder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static cn.topiam.eiam.protocol.oidc.constant.OidcProtocolConstants.OIDC_ERROR_URI;

/**
 * 客户端认证配置器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/27 21:11
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public final class OAuth2ClientAuthenticationConfigurer extends AbstractConfigurer {

    /**
     * RequestMatcher
     */
    private RequestMatcher                          requestMatcher;

    /**
     * 认证失败处理器
     */
    private final AuthenticationFailureHandler      authenticationFailureHandler = this::onAuthenticationFailure;

    /**
     * error Http 响应转换器
     */
    private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter   = new OAuth2ErrorHttpMessageConverter();

    /**
     * Restrict for internal use only.
     */
    OAuth2ClientAuthenticationConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
        super(objectPostProcessor);
    }

    @Override
    public void init(HttpSecurity httpSecurity) {
        this.requestMatcher = new OrRequestMatcher(
            //令牌端点
            new AntPathRequestMatcher(ProtocolConstants.OidcEndpointConstants.TOKEN_ENDPOINT,
                HttpMethod.POST.name()),
            //令牌内省端点
            new AntPathRequestMatcher(
                ProtocolConstants.OidcEndpointConstants.TOKEN_INTROSPECTION_ENDPOINT,
                HttpMethod.POST.name()),
            //令牌吊销端点
            new AntPathRequestMatcher(
                ProtocolConstants.OidcEndpointConstants.TOKEN_REVOCATION_ENDPOINT,
                HttpMethod.POST.name()),
            //设备授权端点
            new AntPathRequestMatcher(
                ProtocolConstants.OidcEndpointConstants.DEVICE_AUTHORIZATION_ENDPOINT,
                HttpMethod.POST.name()));

        List<AuthenticationProvider> authenticationProviders = createDefaultAuthenticationProviders(
            httpSecurity);
        authenticationProviders.forEach(authenticationProvider -> httpSecurity
            .authenticationProvider(postProcess(authenticationProvider)));
    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        AuthenticationManager authenticationManager = httpSecurity
            .getSharedObject(AuthenticationManager.class);
        OAuth2ClientAuthenticationFilter clientAuthenticationFilter = new OAuth2ClientAuthenticationFilter(
            authenticationManager, this.requestMatcher);
        clientAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        //认证转换器
        List<AuthenticationConverter> authenticationConverters = createDefaultAuthenticationConverters();
        clientAuthenticationFilter.setAuthenticationConverter(
            new DelegatingAuthenticationConverter(authenticationConverters));
        httpSecurity.addFilterAfter(postProcess(clientAuthenticationFilter),
            AbstractPreAuthenticatedProcessingFilter.class);
    }

    @Override
    public EndpointMatcher getEndpointMatcher() {
        return new EndpointMatcher(this.requestMatcher, false);
    }

    /**
     * 默认认证转换器
     *
     * @return {@link List}
     */
    private static List<AuthenticationConverter> createDefaultAuthenticationConverters() {
        List<AuthenticationConverter> authenticationConverters = new ArrayList<>();
        //Jwt 断言
        authenticationConverters.add(new JwtClientAssertionAuthenticationConverter());
        //Basic 认证
        authenticationConverters.add(new ClientSecretBasicAuthenticationConverter());
        //Post 认证
        authenticationConverters.add(new ClientSecretPostAuthenticationConverter());
        // PKCE
        authenticationConverters.add(new PublicClientAuthenticationConverter());
        return authenticationConverters;
    }

    /**
     * 默认身份提供商
     *
     * @param httpSecurity {@link HttpSecurity}
     * @return {@link List}
     */
    private static List<AuthenticationProvider> createDefaultAuthenticationProviders(HttpSecurity httpSecurity) {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();

        RegisteredClientRepository registeredClientRepository = new OidcConfigRegisteredClientRepositoryWrapper(
            OAuth2ConfigurerUtils.getRegisteredClientRepository(httpSecurity));
        OAuth2AuthorizationService authorizationService = OAuth2ConfigurerUtils
            .getAuthorizationService(httpSecurity);

        //JWT 断言提供商
        JwtClientAssertionAuthenticationProvider jwtClientAssertionAuthenticationProvider = new JwtClientAssertionAuthenticationProvider(
            registeredClientRepository, authorizationService);
        authenticationProviders.add(jwtClientAssertionAuthenticationProvider);

        //客户端秘钥提供商
        ClientSecretAuthenticationProvider clientSecretAuthenticationProvider = new ClientSecretAuthenticationProvider(
            registeredClientRepository, authorizationService);

        clientSecretAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        authenticationProviders.add(clientSecretAuthenticationProvider);

        //PKCE提供商
        PublicClientAuthenticationProvider publicClientAuthenticationProvider = new PublicClientAuthenticationProvider(
            registeredClientRepository, authorizationService);
        authenticationProviders.add(publicClientAuthenticationProvider);

        return authenticationProviders;
    }

    private void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                         AuthenticationException exception) throws IOException {

        SecurityContextHolder.clearContext();

        // TODO
        // The authorization server MAY return an HTTP 401 (Unauthorized) status code
        // to indicate which HTTP authentication schemes are supported.
        // If the client attempted to authenticate via the "Authorization" request header field,
        // the authorization server MUST respond with an HTTP 401 (Unauthorized) status code and
        // include the "WWW-Authenticate" response header field
        // matching the authentication scheme used by the client.

        OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        if (OAuth2ErrorCodes.INVALID_CLIENT.equals(error.getErrorCode())) {
            httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            error = new OAuth2Error(error.getErrorCode(),
                "Client authentication failed. Either the client or the client credentials are invalid.",
                OIDC_ERROR_URI);
        } else {
            httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
        }
        this.errorHttpResponseConverter.write(error, null, httpResponse);
    }

}
