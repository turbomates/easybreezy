import React from "react";
import { List } from "antd";

import {
  EditProjectTeamMemberRoleRequest,
  ProjectTeamMember,
  RemoveProjectTeamMemberRequest,
  Role,
} from "ProjectModels";
import { TeamMembersListItem } from "./TeamMembersListItem";

interface Props {
  members?: ProjectTeamMember[];
  roles: Role[];
  edit: (value: EditProjectTeamMemberRoleRequest) => void;
  remove: (value: RemoveProjectTeamMemberRequest) => void;
  teamId: string;
}

export const TeamMembersList: React.FC<Props> = ({
  members,
  roles,
  edit,
  teamId,
  remove,
}) => {
  return (
    <List
      itemLayout="horizontal"
      dataSource={members}
      renderItem={(item) => (
        <TeamMembersListItem
          roles={roles}
          member={item}
          edit={edit}
          teamId={teamId}
          remove={remove}
        />
      )}
    />
  );
};
