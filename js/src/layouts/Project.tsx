import React from "react";
import { Layout } from "antd";
import { useParams } from "react-router"

import { ProjectHeaderMenu } from "../features/project/components/ProjectHeaderMenu"

import "./Project.scss";

const { Content, Header } = Layout;

export const Project: React.FC = ({ children }) => {
  const {slug} = useParams<{ slug: string }>()

  return (
    <Layout className="project-layout">
      <Header className="project-header">
        <ProjectHeaderMenu slug={slug}/>
      </Header>
      <Layout>
        <Content>{children}</Content>
      </Layout>
    </Layout>
  );
};
