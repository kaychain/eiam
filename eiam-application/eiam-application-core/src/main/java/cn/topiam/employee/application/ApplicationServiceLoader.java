/*
 * eiam-application-core - Employee Identity and Access Management
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
package cn.topiam.employee.application;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.*;
import org.springframework.context.annotation.Configuration;

import cn.topiam.employee.application.exception.AppNotExistException;
import cn.topiam.employee.application.exception.AppTemplateNotExistException;
import cn.topiam.employee.common.entity.app.AppEntity;
import cn.topiam.employee.common.repository.app.AppRepository;

/**
 * 应用服务加载器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/20 21:08
 */
@Configuration
public class ApplicationServiceLoader implements ApplicationContextAware {

    private final Logger                          logger                = LoggerFactory
        .getLogger(ApplicationServiceLoader.class);
    /**
     * 用于保存接口实现类名及对应的类
     */
    private Map<String, ApplicationService>       loadMap               = new HashMap<>(16);
    private ApplicationContext                    applicationContext;
    /**
     * key: code，value：templateImpl
     */
    private final Map<String, ApplicationService> applicationServiceMap = new HashMap<>(16);

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        loadMap = applicationContext.getBeansOfType(ApplicationService.class);
    }

    /**
     * 获取应用列表
     *
     * @return {@link List}
     */
    public List<ApplicationService> getApplicationServiceList() {
        return loadMap.values().stream().toList();
    }

    /**
     * 根据CODE获取应用
     *
     * @param code {@link String}
     * @return {@link List}
     */
    public ApplicationService getApplicationService(String code) {
        ApplicationService impl = applicationServiceMap.get(code);
        if (!Objects.isNull(impl)) {
            return impl;
        }
        for (ApplicationService service : getApplicationServiceList()) {
            if (code.equals(service.getCode())) {
                applicationServiceMap.put(code, service);
                return service;
            }
        }
        throw new AppTemplateNotExistException();
    }

    /**
     * 根据应用编码获取应用服务
     *
     * @param appId {@link String}
     * @return {@link List}
     */
    public ApplicationService getApplicationServiceByAppId(String appId) {
        AppRepository repository = applicationContext.getBean(AppRepository.class);
        Optional<AppEntity> optional = repository.findById(appId);
        if (optional.isEmpty()) {
            throw new AppNotExistException();
        }
        AppEntity app = optional.get();
        return getApplicationService(app.getTemplate());
    }

    /**
     * 根据应用编码获取应用服务
     *
     * @param appCode {@link String}
     * @return {@link List}
     */
    public ApplicationService getApplicationServiceByAppCode(String appCode) {
        AppRepository repository = applicationContext.getBean(AppRepository.class);
        Optional<AppEntity> optional = repository.findByCode(appCode);
        if (optional.isEmpty()) {
            throw new AppNotExistException();
        }
        AppEntity app = optional.get();
        return getApplicationService(app.getTemplate());
    }

    public void addApplicationService(List<String> beanNameList) {
        Map<String, ApplicationService> applicationServiceMap = new HashMap<>(16);
        for (String beanName : beanNameList) {
            applicationServiceMap.put(beanName,
                applicationContext.getBean(beanName, ApplicationService.class));
        }
        loadMap.putAll(applicationServiceMap);
    }
}
