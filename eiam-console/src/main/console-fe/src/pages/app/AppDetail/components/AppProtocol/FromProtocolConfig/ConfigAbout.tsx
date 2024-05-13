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
import { ProForm, ProFormText } from '@ant-design/pro-components';
import { useAsyncEffect } from 'ahooks';
import { Form, Typography } from 'antd';
import { useIntl } from '@umijs/max';
import { createStyles } from 'antd-style';
import { ColProps } from 'antd/es/grid/col';

const useStyles = createStyles(({}) => ({
  config: {
    backgroundColor: '#f1f1f2',
  },
}));

/**
 * 配置信息
 *
 * @param props
 */
export default (props: {
  appId: string;
  protocolEndpoint: Record<string, string>;
  collapsed?: boolean;
  labelCol?: ColProps;
  wrapperCol?: ColProps;
}) => {
  const [configForm] = Form.useForm();
  const {
    protocolEndpoint,
    appId,
    labelCol = {
      span: 6,
    },
    wrapperCol = {
      span: 14,
    },
  } = props;
  const intl = useIntl();
  const { styles } = useStyles();

  useAsyncEffect(async () => {
    configForm.setFieldsValue(protocolEndpoint);
  }, [appId, protocolEndpoint]);

  return (
    <ProForm
      layout={'horizontal'}
      labelCol={labelCol}
      wrapperCol={wrapperCol}
      labelAlign={'right'}
      submitter={false}
      labelWrap
      form={configForm}
      className={styles.config}
    >
      <ProFormText
        label={intl.formatMessage({
          id: 'pages.app.config.detail.protocol_config.form.config_about.idp_sso_endpoint',
        })}
        name={'idpSsoEndpoint'}
        extra={intl.formatMessage({
          id: 'pages.app.config.detail.protocol_config.form.config_about.idp_sso_endpoint.extra',
        })}
        readonly
        proFieldProps={{
          render: (value: string) => {
            return value && <Typography.Text copyable>{value}</Typography.Text>;
          },
        }}
        fieldProps={{ autoComplete: 'off' }}
      />
    </ProForm>
  );
};
