import React from "react";

type Props = {
  permissions: string[];
}

export const ProjectRoleFormTableWrapper: React.FC<Props> = ({
  permissions,
  children,
}) => {
  return (
    <table className="role-form">
      <thead>
        <tr>
          <th />
          {permissions.map((permission, index) => (
            <th key={index}>{permission.replace("_", " ")}</th>
          ))}
        </tr>
      </thead>
      {children}
    </table>
  );
};
