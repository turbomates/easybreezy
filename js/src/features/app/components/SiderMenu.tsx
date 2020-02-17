import React, { FC } from "react";
import { Menu, Icon } from "antd";
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
          <Icon type="user" />
          <span>HR</span>
        </span>
      }
    >
      <Item key="1">
        <Link to="/users">Users</Link>
      </Item>
      <Item key="2">Salary</Item>
      <Item key="3">Comments</Item>
      <Item key="4">Bio</Item>
      <Item key="5">Vacations</Item>
      <Item key="6">
        <Link to="/human-resources">Timeline</Link>
      </Item>
    </SubMenu>
    <SubMenu
      key="projects"
      title={
        <span>
          <Icon type="laptop" />
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
