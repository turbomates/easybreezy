import React, { FC } from "react";
import { Menu } from "antd";
import { UserOutlined, LaptopOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom";

const { SubMenu, Item } = Menu;

export const SiderMenu: FC<{}> = () => (
  <Menu
    mode="inline"
    defaultSelectedKeys={["1"]}
    defaultOpenKeys={["sub1"]}
    className="app-menu"
  >
    <SubMenu
      key="hr"
      title={
        <span>
          <UserOutlined />
          <span>HR</span>
        </span>
      }
    >
      <Item key="1">
        <Link to="/human-resources">Timeline</Link>
      </Item>
    </SubMenu>
    <SubMenu
      key="projects"
      title={
        <span>
          <LaptopOutlined />
          <span>Projects</span>
        </span>
      }
    >
      <Item key="21">Task</Item>
      <Item key="22">Category</Item>
      <Item key="23">Team</Item>
      <Item key="24">Member</Item>
      <Item key="25">Role</Item>
      <Item key="26">Report</Item>
    </SubMenu>
  </Menu>
);
