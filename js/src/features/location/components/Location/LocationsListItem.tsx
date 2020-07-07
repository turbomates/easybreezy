import React from "react";
import { List } from "antd";
import { CloseCircleTwoTone } from "@ant-design/icons";
import { Location } from "LocationModels";
import { NavLink } from "react-router-dom"

interface Props {
  item: Location;
  remove: (id: string) => void;
}

export const LocationsListItem: React.FC<Props> = ({ item, remove }) => (
  <List.Item>
    <NavLink to={`locations/${item.id}`}>
      {item.name}
    </NavLink>
    <CloseCircleTwoTone twoToneColor="#ff7875" onClick={() => remove(item.id)} />
  </List.Item>
);
