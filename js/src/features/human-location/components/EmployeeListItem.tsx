import React, { useCallback } from "react";
import { List, Button, Typography } from "antd";
import { EmployeeShort, Location, EmployeeLocation } from "LocationModels";
import { EmployeeLocationListItem } from "./EmployeeLocationListItem";

const { Text } = Typography;

interface Props {
  employee: EmployeeShort;
  employeeLocation?: EmployeeLocation[];
  locations: Location[];
  selectEmployee: (id: string) => void;
  selectEmployeeLocation: (val: EmployeeLocation | null) => void;
  remove: (id: string) => void;
}

export const EmployeeListItem: React.FC<Props> = ({
  employee,
  locations,
  employeeLocation,
  selectEmployee,
  selectEmployeeLocation,
  remove,
  ...rest
}) => {
  const handleAssign = useCallback(() => selectEmployee(employee.userId), [
    employee,
    selectEmployee,
  ]);

  return (
    <List.Item {...rest} style={{ flexDirection: "column" }}>
      <div>
        <Text strong>
          {employee.userId} {employee.firstName} {employee.lastName}
        </Text>
        <Button onClick={handleAssign}>Assign</Button>
      </div>
      {employeeLocation && (
        <List
          dataSource={employeeLocation}
          renderItem={(item) => (
            <EmployeeLocationListItem
              item={item}
              edit={selectEmployeeLocation}
              remove={remove}
            />
          )}
        />
      )}
    </List.Item>
  );
};
