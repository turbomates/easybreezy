import React from "react";
import { NavLink } from "react-router-dom";
import { List } from "antd";

import { Team } from "ProjectModels";

interface Props {
  team: Team;
  slug: string;
}

export const ProjectTeamsListItem: React.FC<Props> = ({ team, slug }) => (
  <List.Item>
    <List.Item.Meta
      title={
        <NavLink to={`/projects/${slug}/teams/${team.id}`}>{team.name}</NavLink>
      }
    />
  </List.Item>
);
