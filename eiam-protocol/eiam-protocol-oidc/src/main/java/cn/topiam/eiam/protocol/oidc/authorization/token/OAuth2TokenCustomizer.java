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
package cn.topiam.eiam.protocol.oidc.authorization.token;

import java.security.Principal;
import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

import cn.topiam.employee.common.entity.account.UserEntity;
import cn.topiam.employee.common.repository.account.UserRepository;
import cn.topiam.employee.support.security.userdetails.UserDetails;
import cn.topiam.employee.support.util.BeanUtils;

import lombok.Setter;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.*;

import static cn.topiam.employee.support.constant.EiamConstants.DEFAULT_DATE_TIME_FORMATTER;

/**
 * 令牌定制器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/26 21:44
 */
@Setter
@SuppressWarnings({ "AlibabaClassNamingShouldBeCamel" })
public class OAuth2TokenCustomizer implements
                                   org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {
        //@formatter:off
        Set<String> authorizedScopes = context.getAuthorizedScopes();
        if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
            OAuth2Authorization authorization = context.getAuthorization();
            if (!Objects.isNull(authorization)){
                Authentication authentication = authorization.getAttribute(Principal.class.getName());
                if (!Objects.isNull(authentication)){
                    //存在 UserRepository 实时查询用户信息
                    if (!Objects.isNull(userRepository)){
                        UserDetails principal = (UserDetails) authentication.getPrincipal();
                        Optional<UserEntity> optional = userRepository.findById(principal.getId());
                        optional.ifPresent(user->BeanUtils.merge(user.toUserDetails(principal.getAuthorities()),principal));
                    }
                    UserDetails user = (UserDetails) authentication.getPrincipal();
                        boolean hasCustomClaims=false;
                        OidcUserInfo.Builder userInfoBuilder = OidcUserInfo.builder();
                        // Customize headers/claims for id_token
                        // 用户主体
                        userInfoBuilder.subject(user.getId());
                        if (authorizedScopes.contains(EMAIL)) {
                            hasCustomClaims=true;
                            userInfoBuilder.email(Objects.toString(user.getEmail(), ""));
                            userInfoBuilder.emailVerified(!Objects.isNull(user.getEmailVerified()) && user.getEmailVerified());
                        }
                        if (authorizedScopes.contains(PHONE)) {
                            hasCustomClaims=true;
                            userInfoBuilder.phoneNumber(Objects.toString(user.getPhone(), ""));
                            userInfoBuilder.phoneNumberVerified(!Objects.isNull(user.getPhoneVerified()) && user.getPhoneVerified());
                        }
                        if (authorizedScopes.contains(PROFILE)) {
                            hasCustomClaims=true;
                            //用户名
                            userInfoBuilder.preferredUsername(user.getUsername());
                            //昵称
                            userInfoBuilder.nickname(Objects.toString(user.getNickName(), ""));
                            userInfoBuilder.picture(user.getAvatar());
                            userInfoBuilder.updatedAt(user.getUpdateTime().format(DEFAULT_DATE_TIME_FORMATTER));
                        }
                        if (hasCustomClaims){
                            context.getClaims().claims(claims ->
                                    claims.putAll(userInfoBuilder.build().getClaims()));
                        }
                }
            }
        }
        //@formatter:on
    }

    private UserRepository userRepository;

}
