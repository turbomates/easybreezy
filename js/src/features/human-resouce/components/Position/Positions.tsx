import React from "react";
import { List } from "antd";
import { EmployeePosition } from "HumanResourceModels";

interface Props {
  positions: EmployeePosition[];
}

export const Positions: React.FC<Props> = ({ positions }) => (
  <List
    dataSource={positions}
    renderItem={(position) => (
      <List.Item key={position.id}>
        <List.Item.Meta
          title={position.title}
          description={`since ${position.since} till ${position.till ?? "now"}`}
        />
      </List.Item>
    )}
  />
);
