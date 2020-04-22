import React, { useCallback } from "react";
import { CloseCircleOutlined, EditOutlined } from "@ant-design/icons";
import { EmployeeLocation } from "LocationModels";

interface Props {
  item: EmployeeLocation;
  edit: (val: EmployeeLocation) => void;
  remove: (id: string) => void;
}

export const EmployeeLocationListInner: React.FC<Props> = ({
  item,
  remove,
  edit,
}) => {
  const handleEdit = useCallback(() => edit(item), [item, edit]);
  const handleRemove = useCallback(() => remove(item.id), [item, remove]);

  return (
    <>
      {item.location.name} <EditOutlined onClick={handleEdit} />{" "}
      <CloseCircleOutlined onClick={handleRemove} />
    </>
  );
};
