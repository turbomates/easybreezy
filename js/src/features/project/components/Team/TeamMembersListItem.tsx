import React from "react";
import { List, Tooltip, Select } from "antd";
import { CloseCircleOutlined } from "@ant-design/icons";

import { ProjectTeamMember, Role } from "ProjectModels";
import { getMemberRoleName } from "../../helpers";

interface Props {
  name: string;
  roles: Role[];
  member: ProjectTeamMember;
}

const { Option } = Select;

export const TeamMembersListItem: React.FC<Props> = ({
  name,
  roles,
  member,
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
