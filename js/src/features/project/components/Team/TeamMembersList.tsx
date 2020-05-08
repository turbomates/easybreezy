import React from "react";
import { List } from "antd";

import {
  EditProjectTeamMemberRoleRequest,
  ProjectTeamMember,
  Role,
} from "ProjectModels";
import { TeamMembersListItem } from "./TeamMembersListItem";

interface Props {
  members?: ProjectTeamMember[];
  roles: Role[];
  edit: (value: EditProjectTeamMemberRoleRequest) => void;
  teamId: string;
}

export const TeamMembersList: React.FC<Props> = ({
  members,
  roles,
  edit,
  teamId,
}) => {
  return (
    <List
      itemLayout="horizontal"
      dataSource={members}
      renderItem={(item) => (
        <TeamMembersListItem
          name={item.email}
          roles={roles}
          member={item}
          edit={edit}
          teamId={teamId}
        />
      )}
    />
  );
};
