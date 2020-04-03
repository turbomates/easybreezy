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
        <UserOutlined /> Timeline
      </Link>
    </Menu.Item>
    <Menu.Item key="projects">
      <span>
        <LaptopOutlined />
        <span>Projects</span>
      </span>
    </Menu.Item>
    <Menu.Item key="31">
      <Link to="/locations">
        <GlobalOutlined /> Locations
      </Link>
    </Menu.Item>
  </Menu>
);
