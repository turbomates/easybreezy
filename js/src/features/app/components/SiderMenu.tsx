import React, { FC } from "react";
import { Menu } from "antd";
import {
  UserOutlined,
  LaptopOutlined,
  GlobalOutlined,
} from "@ant-design/icons";
import { Link } from "react-router-dom";

export const SiderMenu: FC<{}> = () => (
  <Menu
    mode="inline"
    defaultSelectedKeys={["1"]}
    defaultOpenKeys={["sub1"]}
    className="app-menu"
  >
    <Menu.Item key="hr">
      <Link to="/human-resources">
        <UserOutlined /> <span>Timeline</span>
      </Link>
    </Menu.Item>
    <Menu.Item key="projects">
      <Link to="/projects">
        <LaptopOutlined />
        <span>Projects</span>
      </Link>
    </Menu.Item>
    <Menu.Item key="31">
      <Link to="/locations">
        <GlobalOutlined /> <span>Locations</span>
      </Link>
    </Menu.Item>
  </Menu>
);
