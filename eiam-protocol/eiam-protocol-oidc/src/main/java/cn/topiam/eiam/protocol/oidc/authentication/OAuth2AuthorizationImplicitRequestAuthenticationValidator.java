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
package cn.topiam.eiam.protocol.oidc.authentication;

import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationValidator;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_REQUEST;

import static cn.topiam.eiam.protocol.oidc.authentication.OAuth2AuthorizationImplicitRequestAuthenticationProvider.IMPLICIT;
import static cn.topiam.eiam.protocol.oidc.endpoint.OAuth2ParameterNames.*;

/**
 * 验证器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/9 23:54
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public final class OAuth2AuthorizationImplicitRequestAuthenticationValidator implements
                                                                             Consumer<OAuth2AuthorizationImplicitRequestAuthenticationContext> {
    private static final String                                                           ERROR_URI                      = "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";

    private static final Log                                                              LOGGER                         = LogFactory
        .getLog(OAuth2AuthorizationCodeRequestAuthenticationValidator.class);

    /**
     * The default validator for {@link OAuth2AuthorizationImplicitRequestAuthenticationToken#getScopes()}.
     */
    public static final Consumer<OAuth2AuthorizationImplicitRequestAuthenticationContext> DEFAULT_SCOPE_VALIDATOR        = OAuth2AuthorizationImplicitRequestAuthenticationValidator::validateScope;

    /**
     * The default validator for {@link OAuth2AuthorizationImplicitRequestAuthenticationToken#getRedirectUri()}.
     */
    public static final Consumer<OAuth2AuthorizationImplicitRequestAuthenticationContext> DEFAULT_REDIRECT_URI_VALIDATOR = OAuth2AuthorizationImplicitRequestAuthenticationValidator::validateRedirectUri;
    public static final Consumer<OAuth2AuthorizationImplicitRequestAuthenticationContext> DEFAULT_GRANT_TYPE_VALIDATOR   = OAuth2AuthorizationImplicitRequestAuthenticationValidator::validateGrantType;
    public static final Consumer<OAuth2AuthorizationImplicitRequestAuthenticationContext> DEFAULT_VALIDATE_RESPONSE_MODE = OAuth2AuthorizationImplicitRequestAuthenticationValidator::validateResponseMode;

    private final Consumer<OAuth2AuthorizationImplicitRequestAuthenticationContext>       authenticationValidator        = DEFAULT_REDIRECT_URI_VALIDATOR
        .andThen(DEFAULT_SCOPE_VALIDATOR).andThen(DEFAULT_GRANT_TYPE_VALIDATOR)
        .andThen(DEFAULT_VALIDATE_RESPONSE_MODE);

    @Override
    public void accept(OAuth2AuthorizationImplicitRequestAuthenticationContext authenticationContext) {
        this.authenticationValidator.accept(authenticationContext);
    }

    /**
     * 验证 Scope
     *
     * @param authenticationContext {@link OAuth2AuthorizationImplicitRequestAuthenticationContext}
     */
    private static void validateScope(OAuth2AuthorizationImplicitRequestAuthenticationContext authenticationContext) {
        RegisteredClient registeredClient = authenticationContext.getRegisteredClient();
        OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationImplicitRequestAuthenticationToken = authenticationContext
            .getAuthentication();
        Set<String> requestedScopes = authorizationImplicitRequestAuthenticationToken.getScopes();
        Set<String> allowedScopes = registeredClient.getScopes();

        if (!requestedScopes.isEmpty() && !allowedScopes.containsAll(requestedScopes)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(
                    LogMessage.format("Invalid request: requested scope is not allowed"
                                      + " for registered client '%s'",
                        registeredClient.getId()));
            }
            throwError(OAuth2ErrorCodes.INVALID_SCOPE, OAuth2ParameterNames.SCOPE,
                authorizationImplicitRequestAuthenticationToken, registeredClient);
        }

    }

    /**
     * 验证授权类型
     *
     * @param authenticationContext {@link OAuth2AuthorizationImplicitRequestAuthenticationContext}
     */
    private static void validateGrantType(OAuth2AuthorizationImplicitRequestAuthenticationContext authenticationContext) {
        OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationCodeRequestAuthentication = authenticationContext
            .getAuthentication();
        RegisteredClient registeredClient = authenticationContext.getRegisteredClient();
        //校验 grant type
        if (!registeredClient.getAuthorizationGrantTypes().contains(IMPLICIT)) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.warn(LogMessage.format(
                    "Invalid request: requested grant_type is not allowed for registered client '%s'",
                    registeredClient.getId()));
            }
            throwError(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT, OAuth2ParameterNames.CLIENT_ID,
                authorizationCodeRequestAuthentication, registeredClient);
        }
    }

    /**
     * 验证重定向uri
     *
     * @param authenticationContext {@link OAuth2AuthorizationImplicitRequestAuthenticationContext}
     */
    private static void validateRedirectUri(OAuth2AuthorizationImplicitRequestAuthenticationContext authenticationContext) {
        OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationImplicitRequestAuthentication = authenticationContext
            .getAuthentication();
        RegisteredClient registeredClient = authenticationContext.getRegisteredClient();

        String requestedRedirectUri = authorizationImplicitRequestAuthentication.getRedirectUri();

        if (StringUtils.hasText(requestedRedirectUri)) {
            // ***** redirect_uri is available in authorization request

            UriComponents requestedRedirect = null;
            try {
                requestedRedirect = UriComponentsBuilder.fromUriString(requestedRedirectUri)
                    .build();
            } catch (Exception ex) {
            }
            if (requestedRedirect == null || requestedRedirect.getFragment() != null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(LogMessage
                        .format("Invalid request: redirect_uri is missing or contains a fragment"
                                + " for registered client '%s'",
                            registeredClient.getId()));
                }
                throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.REDIRECT_URI,
                    authorizationImplicitRequestAuthentication, registeredClient);
            }

            if (!isLoopbackAddress(requestedRedirect.getHost())) {
                // As per https://datatracker.ietf.org/doc/html/draft-ietf-oauth-security-topics-22#section-4.1.3
                // When comparing client redirect URIs against pre-registered URIs,
                // authorization servers MUST utilize exact string matching.
                if (!registeredClient.getRedirectUris().contains(requestedRedirectUri)) {
                    throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.REDIRECT_URI,
                        authorizationImplicitRequestAuthentication, registeredClient);
                }
            } else {
                // As per https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-08#section-8.4.2
                // The authorization server MUST allow any port to be specified at the
                // time of the request for loopback IP redirect URIs, to accommodate
                // clients that obtain an available ephemeral port from the operating
                // system at the time of the request.
                boolean validRedirectUri = false;
                for (String registeredRedirectUri : registeredClient.getRedirectUris()) {
                    UriComponentsBuilder registeredRedirect = UriComponentsBuilder
                        .fromUriString(registeredRedirectUri);
                    registeredRedirect.port(requestedRedirect.getPort());
                    if (registeredRedirect.build().toString()
                        .equals(requestedRedirect.toString())) {
                        validRedirectUri = true;
                        break;
                    }
                }
                if (!validRedirectUri) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(LogMessage
                            .format("Invalid request: redirect_uri does not match"
                                    + " for registered client '%s'",
                                registeredClient.getId()));
                    }
                    throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.REDIRECT_URI,
                        authorizationImplicitRequestAuthentication, registeredClient);
                }
            }

        } else {
            // ***** redirect_uri is NOT available in authorization request

            if (authorizationImplicitRequestAuthentication.getScopes().contains(OidcScopes.OPENID)
                || registeredClient.getRedirectUris().size() != 1) {
                // redirect_uri is REQUIRED for OpenID Connect
                throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.REDIRECT_URI,
                    authorizationImplicitRequestAuthentication, registeredClient);
            }
        }
    }

    private static boolean isLoopbackAddress(String host) {
        if (!StringUtils.hasText(host)) {
            return false;
        }
        // IPv6 loopback address should either be "0:0:0:0:0:0:0:1" or "::1"
        if ("[0:0:0:0:0:0:0:1]".equals(host) || "[::1]".equals(host)) {
            return true;
        }
        // IPv4 loopback address ranges from 127.0.0.1 to 127.255.255.255
        String[] ipv4Octets = host.split("\\.");
        if (ipv4Octets.length != 4) {
            return false;
        }
        try {
            int[] address = new int[ipv4Octets.length];
            for (int i = 0; i < ipv4Octets.length; i++) {
                address[i] = Integer.parseInt(ipv4Octets[i]);
            }
            return address[0] == 127 && address[1] >= 0 && address[1] <= 255 && address[2] >= 0
                   && address[2] <= 255 && address[3] >= 1 && address[3] <= 255;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * 验证响应模式
     *
     * @param authenticationContext {@link OAuth2AuthorizationImplicitRequestAuthenticationContext}
     */
    private static void validateResponseMode(OAuth2AuthorizationImplicitRequestAuthenticationContext authenticationContext) {
        OAuth2AuthorizationImplicitRequestAuthenticationToken implicitRequestAuthenticationToken = authenticationContext
            .getAuthentication();
        RegisteredClient registeredClient = authenticationContext.getRegisteredClient();
        if (implicitRequestAuthenticationToken.getResponseMode() == null) {
            return;
        }
        if (!FRAGMENT.equals(implicitRequestAuthenticationToken.getResponseMode())
            || !QUERY.equals(implicitRequestAuthenticationToken.getResponseMode())) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.warn(
                    LogMessage.format("Invalidated response_type used by registered client '%s'",
                        registeredClient.getId()));
            }
            throwError(INVALID_REQUEST, RESPONSE_MODE, implicitRequestAuthenticationToken,
                registeredClient);
        }
    }

    private static void throwError(String errorCode, String parameterName,
                                   OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationCodeRequestAuthentication,
                                   RegisteredClient registeredClient) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName,
            ERROR_URI);
        throwError(error, parameterName, authorizationCodeRequestAuthentication, registeredClient);
    }

    private static void throwError(OAuth2Error error, String parameterName,
                                   OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationCodeRequestAuthentication,
                                   RegisteredClient registeredClient) {

        String redirectUri = StringUtils
            .hasText(authorizationCodeRequestAuthentication.getRedirectUri())
                ? authorizationCodeRequestAuthentication.getRedirectUri()
                : registeredClient.getRedirectUris().iterator().next();
        if (error.getErrorCode().equals(INVALID_REQUEST)
            && parameterName.equals(OAuth2ParameterNames.REDIRECT_URI)) {
            // Prevent redirects
            redirectUri = null;
        }

        throw new OAuth2AuthorizationImplicitRequestAuthenticationException(error,
            getRequestAuthenticationToken(authorizationCodeRequestAuthentication, redirectUri));
    }

    @NotNull
    private static OAuth2AuthorizationImplicitRequestAuthenticationToken getRequestAuthenticationToken(OAuth2AuthorizationImplicitRequestAuthenticationToken requestAuthenticationToken,
                                                                                                       String redirectUri) {
        OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationImplicitRequestAuthenticationToken = new OAuth2AuthorizationImplicitRequestAuthenticationToken(
            requestAuthenticationToken.getAuthorizationUri(),
            requestAuthenticationToken.getClientId(),
            (Authentication) requestAuthenticationToken.getPrincipal(), redirectUri,
            requestAuthenticationToken.getState(), requestAuthenticationToken.getScopes(),
            requestAuthenticationToken.getResponseTypes(),
            requestAuthenticationToken.getResponseMode(),
            requestAuthenticationToken.getAdditionalParameters());
        authorizationImplicitRequestAuthenticationToken.setAuthenticated(true);
        return authorizationImplicitRequestAuthenticationToken;
    }

}
