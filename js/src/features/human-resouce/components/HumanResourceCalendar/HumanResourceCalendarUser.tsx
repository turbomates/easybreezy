import React from "react";
import { Link } from "react-router-dom";
import { Avatar, Popover } from "antd";

import { EmployeeShort } from "HumanResourceModels";
import { getCapitalLetter } from "./helpers";

interface Props {
  employee: EmployeeShort;
}

export const HumanResourceCalendarUser: React.FC<Props> = ({ employee }) => {
  const name =
    employee.firstName && employee.lastName
      ? `${employee.firstName} ${employee.lastName}`
      : employee.userId;

  return (
    <Popover
      placement="topLeft"
      content={
        <Avatar>
          {getCapitalLetter(employee.firstName)}{" "}
          {getCapitalLetter(employee.lastName)}
        </Avatar>
      }
      overlayClassName="avatar"
    >
      <div>
        <Link to={`/users/${employee.userId}`}>
          <span className="title">{name}</span>
        </Link>
      </div>
    </Popover>
  );
};
