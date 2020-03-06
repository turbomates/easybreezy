import React from "react";
import { Avatar } from "antd";
import { UserOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom";

import "./HumanResourceCalendarGroup.scss";

interface Props {
  username: string;
  avatar: string;
  id: number;
}

export const HumanResourceCalendarGroup: React.FC<Props> = props => (
  <Link to={`/users/${props.id}`} className="human-resource-calendar-group">
    <Avatar icon={<UserOutlined />} src={props.avatar} />{" "}
    <span className="title">{props.username}</span>
  </Link>
);
