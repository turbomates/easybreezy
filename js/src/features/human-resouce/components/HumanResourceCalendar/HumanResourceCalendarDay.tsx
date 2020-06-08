import React, { useMemo } from "react";
import { Tooltip } from "antd";
import isLastDayOfMonth from "date-fns/fp/isLastDayOfMonth";

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

  const lastDayOfMonthClassName = useMemo(
    () => (isLastDayOfMonth(date) ? "last-day-of-month" : ""),
    [date],
  );

  if (!className) {
    return <td className={`timetable-cell ${lastDayOfMonthClassName}`} />;
  }

  return (
    <Tooltip title={title}>
      <td className={`timetable-cell ${className} ${lastDayOfMonthClassName}`}>
        {className.includes("sick") && <span>&#10010;</span>}
      </td>
    </Tooltip>
  );
};
