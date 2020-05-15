import React from "react";
import { Timeline } from "antd";
import { EmployeeLocation } from "LocationModels";
import { EmployeeLocationListInner } from "./EmployeeLocationListInner";
import { isCurrentInterval } from "./helper";

interface Props {
  employeeLocations: EmployeeLocation[];
  openEmployeeLocationEditForm: (value: EmployeeLocation) => void;
}

export const EmployeeLocationList: React.FC<Props> = ({
  employeeLocations,
  openEmployeeLocationEditForm,
}) => (
  <Timeline mode="left">
    {employeeLocations.map((item) => (
      <Timeline.Item
        key={item.id}
        label={`${item.startedAt} - ${item.endedAt || "till now"}`}
        color={
          isCurrentInterval(item.startedAt, item.endedAt) ? "green" : "blue"
        }
      >
        <EmployeeLocationListInner
          item={item}
          edit={openEmployeeLocationEditForm}
        />
      </Timeline.Item>
    ))}
  </Timeline>
);
