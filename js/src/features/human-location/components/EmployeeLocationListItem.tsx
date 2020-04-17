import React, { useCallback } from "react";
import { List } from "antd";
import { CloseCircleOutlined, EditOutlined } from "@ant-design/icons";
import { EmployeeLocation } from "LocationModels";

interface Props {
  item: EmployeeLocation;
  edit: (val: EmployeeLocation) => void;
  remove: (id: string) => void;
}

export const EmployeeLocationListItem: React.FC<Props> = ({
  item,
  edit,
  remove,
  ...rest
}) => {
  const handleEdit = useCallback(() => edit(item), [item, edit]);
  const handleRemove = useCallback(() => remove(item.id), [item, remove]);

  return (
    <List.Item {...rest} key={item.id}>
      from {item.startedAt} to {item.endedAt} in {item.location.name}{" "}
      <EditOutlined onClick={handleEdit} />{" "}
      <CloseCircleOutlined onClick={handleRemove} />
    </List.Item>
  );
};
