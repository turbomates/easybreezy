import React, { FC, useState } from "react";
import { Layout, Drawer } from "antd";
import { useParams } from "react-router"

import { ProjectSideMenu } from "../features/project/components/ProjectSideMenu"

import "./Main.scss";

const { Sider, Content } = Layout;

export const Project: FC = ({ children }) => {
  const {slug} = useParams<{ slug: string }>()

  const [collapsed, setCollapsed] = useState(false);

  const [menuVisible, setMenuVisible] = useState(false);

  const onClose = () => setMenuVisible(false);

  return (
    <Layout className="app-layout">
      <Drawer
        className="app-mobile-drawer"
        placement="left"
        closable={false}
        onClose={onClose}
        visible={menuVisible}
      >
        <ProjectSideMenu slug={slug}/>
      </Drawer>
      <Sider
        width={200}
        className="app-sider"
        collapsible
        collapsed={collapsed}
        onCollapse={setCollapsed}
      >
        <ProjectSideMenu slug={slug}/>
      </Sider>
      <Layout style={{padding: "10px"}}>
        <Content>{children}</Content>
      </Layout>
    </Layout>
  );
};
