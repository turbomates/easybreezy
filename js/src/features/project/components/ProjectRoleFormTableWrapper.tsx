import React from "react";

interface Props {
  permissions: string[];
}

export const ProjectRoleFormTableWrapper: React.FC<Props> = ({
  permissions,
  children,
}) => {
  return (
    <table>
      <thead>
        <tr>
          <th />
          {permissions.map((permission, index) => (
            <th key={index}>{permission}</th>
          ))}
        </tr>
      </thead>
      {children}
    </table>
  );
};
