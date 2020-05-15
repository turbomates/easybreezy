import React from "react";
import { List } from "antd";

import { Team } from "ProjectModels";
import { ProjectTeamsListItem } from "./ProjectTeamsListItem";

type Props = {
  teams: Team[];
  slug: string;
}

export const ProjectTeamsList: React.FC<Props> = ({ teams, slug }) => {
  return (
    <List
      itemLayout="horizontal"
      dataSource={teams}
      renderItem={(item) => <ProjectTeamsListItem team={item} slug={slug} />}
    />
  );
};
