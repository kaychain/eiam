/*
 * eiam-portal - Employee Identity and Access Management
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
import { CaretDownOutlined, CaretRightOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { useAsyncEffect, useSafeState } from 'ahooks';
import { Badge, DatePicker, Select, Space, Tag } from 'antd';
import dayjs from 'dayjs';
import { useRef } from 'react';
import ExpandedCard from './components/ExpandedCard';
import type { AuditList, AuditTypeGroup } from './data.d';
import { EventStatus, UserType } from './data.d';
import { getAuditList, getAuditTypes } from './service';
import useStyle from './style';
import classNames from 'classnames';
import { useIntl } from '@umijs/max';

const { RangePicker } = DatePicker;

const { OptGroup } = Select;
const { Option } = Select;
const prefixCls = 'audit-user';

export default () => {
  const actionRef = useRef<ActionType>();
  const [eventTypes, setEventTypes] = useSafeState<AuditTypeGroup[]>();
  const { styles } = useStyle();
  const intl = useIntl();

  useAsyncEffect(async () => {
    const { result, success } = await getAuditTypes();
    if (success) {
      setEventTypes(result);
    }
  }, []);
  /**
   * columns
   */
  const columns: ProColumns<AuditList>[] = [
    {
      title: intl.formatMessage({ id: 'pages.audit.columns.event_type' }),
      dataIndex: 'eventType',
      fixed: 'left',
      renderFormItem: () => {
        return (
          <Select
            placeholder={intl.formatMessage({ id: 'pages.audit.columns.event_type.placeholder' })}
            key={'select'}
            showSearch
            allowClear
            mode={'multiple'}
            maxTagCount={'responsive'}
          >
            {eventTypes &&
              eventTypes.map((group) => {
                return (
                  <OptGroup label={group.name} key={group.code}>
                    {group.types &&
                      group.types.map((t) => {
                        return (
                          <Option key={t.code} value={t.code}>
                            {t.name}
                          </Option>
                        );
                      })}
                  </OptGroup>
                );
              })}
          </Select>
        );
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.audit.columns.username' }),
      ellipsis: true,
      dataIndex: 'username',
      hideInSearch: true,
      fieldProps: {
        placeholder: intl.formatMessage({ id: 'pages.audit.columns.username.placeholder' }),
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.audit.columns.geo_location' }),
      dataIndex: 'geoLocation',
      ellipsis: true,
      valueType: 'text',
      hideInSearch: true,
      render: (_, { geoLocation }) => {
        return geoLocation ? (
          <Badge status={'success'} text={geoLocation?.ip} />
        ) : (
          <Badge status={'error'} text={intl.formatMessage({ id: 'app.unknown' })} />
        );
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.audit.columns.platform' }),
      ellipsis: true,
      dataIndex: 'platform',
      hideInSearch: true,
      render: (_, { userAgent }) => {
        return userAgent.platform;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.audit.columns.browser' }),
      ellipsis: true,
      dataIndex: 'browser',
      hideInSearch: true,
      render: (_, { userAgent }) => {
        return userAgent.browser;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.audit.columns.event_time' }),
      sorter: true,
      defaultSortOrder: 'descend',
      ellipsis: true,
      align: 'center',
      dataIndex: 'eventTime',
      valueType: 'dateTime',
      renderFormItem: () => {
        return (
          <RangePicker
            showTime={{
              hideDisabledOptions: true,
              defaultValue: [dayjs('00:00:00', 'HH:mm:ss'), dayjs('11:59:59', 'HH:mm:ss')],
            }}
            placeholder={[
              intl.formatMessage({ id: 'app.start_time' }),
              intl.formatMessage({ id: 'app.end_time' }),
            ]}
            format="YYYY-MM-DD HH:mm:ss"
          />
        );
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.audit.columns.event_status' }),
      dataIndex: 'eventStatus',
      hideInSearch: true,
      align: 'center',
      fixed: 'right',
      width: 80,
      render: (text: any) => (
        <Space>
          {text === EventStatus.success && (
            <Tag color="#87d068">{intl.formatMessage({ id: 'app.success' })}</Tag>
          )}
          {text === EventStatus.fail && (
            <Tag color="#e54545">{intl.formatMessage({ id: 'app.fail' })}</Tag>
          )}
        </Space>
      ),
    },
  ];

  return (
    <PageContainer className={styles.main} content={intl.formatMessage({ id: 'pages.audit.desc' })}>
      <ProTable<AuditList>
        columns={columns}
        scroll={{ x: 1200 }}
        pagination={{
          showQuickJumper: true,
          defaultPageSize: 10,
        }}
        search={{}}
        expandable={{
          expandRowByClick: true,
          expandedRowClassName: () => {
            return classNames(`${prefixCls}`);
          },
          expandedRowRender: (record, index) => {
            return <ExpandedCard record={record} index={index} />;
          },
          expandIcon: ({ expanded, onExpand, record }) =>
            expanded ? (
              <CaretDownOutlined onClick={(e) => onExpand(record, e)} />
            ) : (
              <CaretRightOutlined onClick={(e) => onExpand(record, e)} />
            ),
        }}
        actionRef={actionRef}
        params={{ userType: UserType.user }}
        request={getAuditList}
        rowKey="id"
        dateFormatter="string"
      />
    </PageContainer>
  );
};
