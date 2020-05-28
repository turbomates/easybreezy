import React from "react";
import { Menu, PageHeader, Tag, Typography } from "antd";
import { Link, useLocation } from "react-router-dom";

import { Project, ProjectStatusTypeResponse } from "ProjectModels";
import { chooseColor } from "../helpers";

import "../project.scss";

const { Paragraph } = Typography;

type Props = {
  project: Project;
};

export const ProjectHeaderMenu: React.FC<Props> = ({ project }) => {
  const location = useLocation();

  return (
    <PageHeader
      className="page-header"
      title={project.name}
      tags={<Tag color={chooseColor(project.status)}>{project.status}</Tag>}
      footer={
        <Menu
          mode="horizontal"
          selectedKeys={[location.pathname]}
          className="page-header__menu"
        >
          <Menu.Item key={`/projects/${project.slug}/role`}>
            <Link to={`/projects/${project.slug}/role`}>
              <span>Role</span>
            </Link>
          </Menu.Item>
          <Menu.Item
            key={location.pathname.includes("teams") ? location.pathname : ""}
          >
            <Link to={`/projects/${project.slug}/teams`}>
              <span>Teams</span>
            </Link>
          </Menu.Item>
          <Menu.Item key={`/projects/${project.slug}/settings`}>
            <Link to={`/projects/${project.slug}/settings`}>
              <span>Settings</span>
            </Link>
          </Menu.Item>
        </Menu>
      }
    >
      <Paragraph
        ellipsis={{
          expandable: true,
        }}
      >
        {project.description}
      </Paragraph>
    </PageHeader>
  );
};
