import React from "react";
import { List } from "antd";

import { Team } from "ProjectModels";
import { TeamListItem } from "./TeamListItem";

type Props = {
  teams: Team[];
  slug: string;
}

export const TeamList: React.FC<Props> = ({ teams, slug }) => {
  return (
    <List
      itemLayout="horizontal"
      dataSource={teams}
      renderItem={(item) => <TeamListItem team={item} slug={slug} />}
    />
  );
};
