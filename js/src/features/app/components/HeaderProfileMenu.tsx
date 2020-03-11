import React from "react";
import { Menu } from "antd";
import { UserOutlined, LogoutOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom";

interface Props {
  userId: number;
}

export const HeaderProfileMenu = (props: Props) => {
  const { userId, ...other } = props;
  return (
    <Menu {...other}>
      <Menu.Item>
        <Link to={`/users/${userId}`}>
          <UserOutlined /> Profile
        </Link>
      </Menu.Item>
      <Menu.Item>
        <LogoutOutlined />
        Logout
      </Menu.Item>
    </Menu>
  );
};
