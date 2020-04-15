import React from "react";
import { List } from "antd";
import { EmployeeSalary } from "HumanResourceModels";

interface Props {
  salaries: EmployeeSalary[];
}

export const Salaries: React.FC<Props> = ({ salaries }) => (
  <List
    dataSource={salaries}
    renderItem={(salary) => (
      <List.Item key={salary.id}>
        <List.Item.Meta
          title={salary.amount}
          description={`since ${salary.since} till ${salary.till ?? "now"}`}
        />
        {salary.comment}
      </List.Item>
    )}
  />
);
