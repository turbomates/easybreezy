import React from "react";
import { List, Tooltip, Select } from "antd";
import { CloseCircleOutlined } from "@ant-design/icons";

import {
  EditProjectTeamMemberRoleRequest,
  ProjectTeamMember,
  RemoveProjectTeamMemberRequest,
  Role,
} from "ProjectModels";
import { getMemberRoleName } from "../../helpers";

const { Option } = Select;

type Props = {
  roles: Role[];
  member: ProjectTeamMember;
  edit: (value: EditProjectTeamMemberRoleRequest) => void;
  remove: (value: RemoveProjectTeamMemberRequest) => void;
  teamId: string;
};

export const TeamMembersListItem: React.FC<Props> = ({
  roles,
  member,
  edit,
  teamId,
  remove,
}) => {
  const actions = [
    <Tooltip title="Remove">
      <CloseCircleOutlined
        onClick={() => remove({ memberId: member.user, teamId })}
      />
    </Tooltip>,
  ];

  return (
    <List.Item actions={actions}>
      <List.Item.Meta
        title={`${member.first} ${member.last}`}
        description={member.email}
      />
      <Select
        defaultValue={getMemberRoleName(roles, member.role)}
        style={{ width: 200 }}
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
