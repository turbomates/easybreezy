import React from "react";
import { Menu } from "antd";
import { UserOutlined, LogoutOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom";

interface Props {
  userId: string;
  logout: () => void;
}

export const HeaderProfileMenu = ({ userId, logout, ...other }: Props) => (
  <Menu {...other}>
    <Menu.Item>
      <Link to={`/users/${userId}`}>
        <UserOutlined /> Profile
      </Link>
    </Menu.Item>
    <Menu.Item onClick={logout}>
      <LogoutOutlined />
      Logout
    </Menu.Item>
  </Menu>
);
