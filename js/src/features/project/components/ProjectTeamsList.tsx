import React from "react";
import { List } from "antd";

import { Project } from "ProjectModels";
import { ProjectTeamsListItem } from "./ProjectTeamsListItem";

interface Props {
  project: Project;
}

export const ProjectTeamsList: React.FC<Props> = ({ project }) => {
  return (
    <List
      itemLayout="horizontal"
      dataSource={project.teams}
      renderItem={(item) => (
        <ProjectTeamsListItem team={item} slug={project.slug} />
      )}
    />
  );
};
