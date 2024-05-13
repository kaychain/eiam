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

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;
import static cn.topiam.employee.support.constant.EiamConstants.COLON;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/10/31 21:41
 */
@SuppressWarnings({ "AlibabaClassNamingShouldBeCamel",
                    "AlibabaServiceOrDaoClassShouldEndWithImpl" })
public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Long                   TIMEOUT = 10L;

    public RedisOAuth2AuthorizationConsentService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        redisTemplate.opsForValue().set(buildKey(authorizationConsent), authorizationConsent,
            TIMEOUT, TimeUnit.MINUTES);

    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        redisTemplate.delete(buildKey(authorizationConsent));
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return (OAuth2AuthorizationConsent) redisTemplate.opsForValue()
            .get(buildKey(registeredClientId, principalName));
    }

    private static String buildKey(String registeredClientId, String principalName) {
        return "token" + COLON + "consent" + COLON + registeredClientId + COLON + principalName;
    }

    private static String buildKey(OAuth2AuthorizationConsent authorizationConsent) {
        return buildKey(authorizationConsent.getRegisteredClientId(),
            authorizationConsent.getPrincipalName());
    }

}
