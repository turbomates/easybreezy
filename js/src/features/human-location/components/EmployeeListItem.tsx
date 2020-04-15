import React, { useCallback } from "react";
import { List, Button, Typography } from "antd";
import { EmployeeLocation } from "LocationModels";
import { EmployeeLocationListItem } from "./EmployeeLocationListItem";
import { EmployeeShort } from "HumanResourceModels";

const { Text } = Typography;

interface Props {
  employee: EmployeeShort;
  employeeLocations?: EmployeeLocation[];
  openLocationAssignForm: (id: string) => void;
  openEmployeeLocationEditForm: (val: EmployeeLocation) => void;
  remove: (id: string) => void;
}

export const EmployeeListItem: React.FC<Props> = ({
  employee,
  employeeLocations,
  openLocationAssignForm,
  openEmployeeLocationEditForm,
  remove,
}) => {
  const handleAssign = useCallback(
    () => openLocationAssignForm(employee.userId),
    [employee, openLocationAssignForm],
  );

  return (
    <List.Item style={{ flexDirection: "column" }}>
      <div>
        <Text strong>
          {employee.userId} {employee.firstName} {employee.lastName}
        </Text>
        <Button onClick={handleAssign}>Assign</Button>
      </div>
      {employeeLocations && (
        <List
          dataSource={employeeLocations}
          renderItem={(item) => (
            <EmployeeLocationListItem
              item={item}
              edit={openEmployeeLocationEditForm}
              remove={remove}
            />
          )}
        />
      )}
    </List.Item>
  );
};
