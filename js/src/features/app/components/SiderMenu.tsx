import React, { FC } from "react";
import { Menu } from "antd";
import {
  UserOutlined,
  LaptopOutlined,
  GlobalOutlined,
} from "@ant-design/icons";
import { Link, useLocation } from "react-router-dom";

export const SiderMenu: FC<{}> = () => {
  const location = useLocation();

  return (
    <Menu
      mode="inline"
      selectedKeys={[location.pathname.split("/")[1]]}
      defaultOpenKeys={["sub1"]}
      className="app-menu"
    >
      <Menu.Item key="human-resources">
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
      <Menu.Item key="locations">
        <Link to="/locations">
          <GlobalOutlined /> <span>Locations</span>
        </Link>
      </Menu.Item>
    </Menu>
  );
};
