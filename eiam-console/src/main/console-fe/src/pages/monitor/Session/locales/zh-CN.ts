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
export default {
  'pages.other.session.desc':
    '会话管理可以查看所有当前仍然有效的会话列表，并且可以强制注销某个用户的会话。注销后，该用户凭证即刻失效，之后的所有操作都需要重新认证。',
  'pages.other.session.column.session_id': '会话 ID',
  'pages.other.session.column.username': '用户名称',
  'pages.other.session.column.ip': '活动 IP',
  'pages.other.session.column.geo_location': '活动地点',
  'pages.other.session.column.device_type': '设备类型',
  'pages.other.session.column.platform': '操作系统',
  'pages.other.session.column.browser': '浏览器',
  'pages.other.session.column.login_time': '登录时间',
  'pages.other.session.column.last_request': '最后操作时间',
  'pages.other.session.column.user_type': '用户类型',
  'pages.other.session.column.user_type.admin': '管理员',
  'pages.other.session.column.user_type.user': '用户',
  'pages.other.session.column.unknown': '未知',
  'pages.other.session.column.option.delete': '下线会话',
  'pages.other.session.column.option.delete_confirm_title': '确定要下线该会话吗？',
  'pages.other.session.column.option.delete_confirm_content': '用户登录会话状态将过期。',
  'pages.other.session.batch-delete': '批量下线',
  'pages.other.session.batch-delete.confirm': '确定要批量下线选中用户吗？',
};
