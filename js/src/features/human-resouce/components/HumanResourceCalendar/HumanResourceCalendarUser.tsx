import React from "react";
import { Link } from "react-router-dom";
import { EmployeeShort } from "HumanResourceModels";

interface Props {
  employee: EmployeeShort;
}

export const HumanResourceCalendarUser: React.FC<Props> = ({ employee }) => {
  const name =
    employee.firstName && employee.lastName
      ? `${employee.firstName} ${employee.lastName}`
      : employee.userId;

  return (
    <Link to={`/users/${employee.userId}`}>
      <span className="title">{name}</span>
    </Link>
  );
};
