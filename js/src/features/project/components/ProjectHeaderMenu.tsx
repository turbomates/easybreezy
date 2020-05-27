import React from "react";
import { Menu, PageHeader, Tag, Typography } from "antd";
import { Link, useLocation } from "react-router-dom";
import { useSelector } from "react-redux";

import { selectProject } from "../selectors";
import { ProjectStatusTypeResponse } from "ProjectModels";
import { chooseColor } from "../helpers";

import "../project.scss";

const { Paragraph } = Typography;

type Props = {
  slug: string;
};

export const ProjectHeaderMenu: React.FC<Props> = ({ slug }) => {
  const location = useLocation();
  const project = useSelector(selectProject);

  return (
    <PageHeader
      className="page-header"
      title={project?.name}
      tags={<Status status={project?.status} />}
      footer={
        <Menu
          mode="horizontal"
          selectedKeys={[location.pathname]}
          className="page-header__menu"
        >
          <Menu.Item key={`/projects/${slug}/role`}>
            <Link to={`/projects/${slug}/role`}>
              <span>Role</span>
            </Link>
          </Menu.Item>
          <Menu.Item
            key={location.pathname.includes("teams") ? location.pathname : ""}
          >
            <Link to={`/projects/${slug}/teams`}>
              <span>Teams</span>
            </Link>
          </Menu.Item>
          <Menu.Item key={`/projects/${slug}/settings`}>
            <Link to={`/projects/${slug}/settings`}>
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
        {project?.description}
      </Paragraph>
    </PageHeader>
  );
};

type StatusProps = {
  status?: ProjectStatusTypeResponse;
};

const Status: React.FC<StatusProps> = ({ status }) => {
  if (!status) return null;

  return <Tag color={chooseColor(status)}>{status}</Tag>;
};
