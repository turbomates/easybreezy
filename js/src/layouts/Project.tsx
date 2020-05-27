import React from "react";
import { Layout } from "antd";
import { useParams } from "react-router";

import { ProjectHeaderMenu } from "../features/project/components/ProjectHeaderMenu";

import "./Project.scss";

const { Content } = Layout;

export const Project: React.FC = ({ children }) => {
  const { slug } = useParams<{ slug: string }>();

  return (
    <Layout className="project-layout">
      <Layout>
        <ProjectHeaderMenu slug={slug} />
        <Content>{children}</Content>
      </Layout>
    </Layout>
  );
};
