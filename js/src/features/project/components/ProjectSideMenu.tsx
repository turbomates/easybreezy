import React from "react";
import { Menu } from "antd";
import { Link } from "react-router-dom";

interface Props {
  slug: string;
}

export const ProjectSideMenu: React.FC<Props> = ({ slug }) => {
  return (
    <Menu mode="inline" defaultSelectedKeys={["project"]} className="app-menu">
      <Menu.Item key="project">
        <Link to={`/projects/${slug}`}>
          <span>Project</span>
        </Link>
      </Menu.Item>
      <Menu.Item key="role">
        <Link to={`/projects/${slug}/role`}>
          <span>Role</span>
        </Link>
      </Menu.Item>
    </Menu>
  );
};
