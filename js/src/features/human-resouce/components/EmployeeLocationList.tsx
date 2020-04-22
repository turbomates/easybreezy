import React from "react";
import { Timeline } from "antd";
import { EmployeeLocation } from "LocationModels";
import { isTodayInRange } from "utils/date";
import { EmployeeLocationListInner } from "./EmployeeLocationListInner";

interface Props {
  employeeLocations: EmployeeLocation[];
  openEmployeeLocationEditForm: (val: EmployeeLocation) => void;
  remove: (id: string) => void;
}

export const EmployeeLocationList: React.FC<Props> = ({
  employeeLocations,
  openEmployeeLocationEditForm,
  remove,
}) => (
  <Timeline mode="left">
    {employeeLocations.map((item) => (
      <Timeline.Item
        key={item.id}
        label={`${item.startedAt} - ${item.endedAt}`}
        color={isTodayInRange(item.startedAt, item.endedAt) ? "green" : "blue"}
      >
        <EmployeeLocationListInner
          item={item}
          edit={openEmployeeLocationEditForm}
          remove={remove}
        />
      </Timeline.Item>
    ))}
  </Timeline>
);
