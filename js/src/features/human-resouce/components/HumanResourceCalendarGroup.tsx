import React from "react";
import { Avatar } from "antd";
import { Link } from "react-router-dom";

import "./HumanResourceCalendarGroup.scss";

interface Props {
  username: string;
  avatar: string;
  id: number;
}

export const HumanResourceCalendarGroup = (props: Props) => (
  <Link to={`/users/${props.id}`} className="human-resource-calendar-group">
    <Avatar icon="user" src={props.avatar} />{" "}
    <span className="title">{props.username}</span>
  </Link>
);
