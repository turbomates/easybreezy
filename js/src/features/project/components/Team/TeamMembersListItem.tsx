import React from "react";
import { List, Tooltip, Select } from "antd";
import { CloseCircleOutlined } from "@ant-design/icons";

import { ProjectTeamMember, Role } from "ProjectModels";

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

  function getRole() {
    return roles.find((role) => role.id === member.role)?.name;
  }

  return (
    <List.Item actions={actions}>
      <List.Item.Meta title={name} />
      <Select defaultValue={getRole()} style={{ width: 120 }}>
        {roles.map((role) => (
          <Option value={role.id} key={role.id}>
            {role.name}
          </Option>
        ))}
      </Select>
    </List.Item>
  );
};
