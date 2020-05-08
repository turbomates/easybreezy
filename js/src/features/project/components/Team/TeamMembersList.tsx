import React from "react";
import { List } from "antd"

import { ProjectTeamMember, Role } from "ProjectModels";
import { TeamMembersListItem } from "./TeamMembersListItem";

interface Props {
  members?: ProjectTeamMember[];
  roles: Role[];
}

export const TeamMembersList: React.FC<Props> = ({ members, roles }) => {
  return (
    <List
      itemLayout="horizontal"
      dataSource={members}
      renderItem={(item) => (
        <TeamMembersListItem name={item.email} roles={roles} member={item} />
      )}
    />
  );
};
