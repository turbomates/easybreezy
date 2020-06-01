import React from "react";
import { Avatar, Popover } from "antd";

import { EmployeeShort } from "HumanResourceModels";

interface Props {
  employee: EmployeeShort;
}
export const HumanResourceCalendarUserAvatar: React.FC<Props> = ({
  employee,
  children,
}) => {
  const initials =
    employee.firstName && employee.lastName
      ? `${employee.firstName.charAt(0)} ${employee.lastName.charAt(0)}`
      : "";

  return (
    <Popover
      placement="topLeft"
      content={<Avatar>{initials}</Avatar>}
      overlayClassName="avatar"
    >
      <div>{children}</div>
    </Popover>
  );
};
