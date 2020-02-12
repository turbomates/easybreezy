import React from "react";
import { Menu, Icon } from "antd";
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
          <Icon type="user" /> Profile
        </Link>
      </Menu.Item>
      <Menu.Item>
        <Icon type="logout" />
        Logout
      </Menu.Item>
    </Menu>
  );
};
