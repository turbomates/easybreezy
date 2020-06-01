import React from "react";
import { Tooltip } from "antd";
import { Absence } from "HumanResourceModels";
import { generateCellInfo } from "./helpers";

type Props = {
  date: Date;
  absences: Absence[];
};

export const HumanResourceCalendarDay: React.FC<Props> = ({
  date,
  absences = [],
}) => {
  const { className, title } = generateCellInfo(date, absences);

  if (!className) {
    return <td className="timetable-cell" />;
  }

  return (
    <Tooltip title={title}>
      <td className={`timetable-cell ${className}`}>
        {className.includes("sick") && <span>&#10010;</span>}
      </td>
    </Tooltip>
  );
};
