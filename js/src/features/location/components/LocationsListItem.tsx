import React from "react";
import { List } from "antd";
import { CloseCircleOutlined } from "@ant-design/icons";
import { Location } from "LocationModels";

interface Props {
  item: Location;
  remove: (id: string) => void;
}

export const LocationsListItem: React.FC<Props> = ({ item, remove }) => (
  <List.Item>
    {item.name} {item.vacationDays}{" "}
    <CloseCircleOutlined onClick={() => remove(item.id)} />
  </List.Item>
);
