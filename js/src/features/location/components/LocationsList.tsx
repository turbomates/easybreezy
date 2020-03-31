import React from "react";
import { List } from "antd";
import { Location } from "LocationModels";
import { LocationsListItem } from "./LocationsListItem";

interface Props {
  items: Location[];
  remove: (id: string) => void;
}

export const LocationsList: React.FC<Props> = props => {
  return (
    <List
      dataSource={props.items}
      renderItem={item => (
        <LocationsListItem item={item} remove={props.remove} />
      )}
    />
  );
};
