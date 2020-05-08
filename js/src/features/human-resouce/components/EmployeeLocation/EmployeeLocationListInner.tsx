import React, { useCallback } from "react";
import { EditOutlined } from "@ant-design/icons";
import { EmployeeLocation } from "LocationModels";

interface Props {
  item: EmployeeLocation;
  edit: (value: EmployeeLocation) => void;
}

export const EmployeeLocationListInner: React.FC<Props> = ({ item, edit }) => {
  const handleEdit = useCallback(() => edit(item), [item, edit]);

  return (
    <>
      {item.location.name} <EditOutlined onClick={handleEdit} />
    </>
  );
};
