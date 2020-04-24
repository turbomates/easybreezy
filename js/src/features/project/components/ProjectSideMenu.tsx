import React from "react";
import { Menu } from "antd";
import { Link, useLocation } from "react-router-dom"

interface Props {
  slug: string;
}

export const ProjectSideMenu: React.FC<Props> = ({ slug }) => {
  const location = useLocation();

  return (
    <Menu mode="horizontal" selectedKeys={[location.pathname]} className="project-menu">
      <Menu.Item key={`/projects/${slug}`}>
        <Link to={`/projects/${slug}`}>
          <span>Project</span>
        </Link>
      </Menu.Item>
      <Menu.Item key={`/projects/${slug}/role`}>
        <Link to={`/projects/${slug}/role`}>
          <span>Role</span>
        </Link>
      </Menu.Item>
    </Menu>
  );
};
