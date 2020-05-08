import React from "react";
import { List, Tooltip, Select } from "antd";
import { CloseCircleOutlined } from "@ant-design/icons";

import {
  EditProjectTeamMemberRoleRequest,
  ProjectTeamMember,
  Role,
} from "ProjectModels";
import { getMemberRoleName } from "../../helpers";

interface Props {
  name: string;
  roles: Role[];
  member: ProjectTeamMember;
  edit: (value: EditProjectTeamMemberRoleRequest) => void;
  teamId: string;
}

const { Option } = Select;

export const TeamMembersListItem: React.FC<Props> = ({
  name,
  roles,
  member,
  edit,
  teamId,
}) => {
  const actions = [
    <Tooltip title="Dismiss">
      <CloseCircleOutlined />
    </Tooltip>,
  ];

  return (
    <List.Item actions={actions}>
      <List.Item.Meta title={name} />
      <Select
        defaultValue={getMemberRoleName(roles, member.role)}
        style={{ width: 120 }}
        onChange={(newRoleId) => {
          edit({
            newRoleId,
            teamId,
            memberId: member.user,
          });
        }}
      >
        {roles.map((role) => (
          <Option value={role.id} key={role.id}>
            {role.name}
          </Option>
        ))}
      </Select>
    </List.Item>
  );
};
