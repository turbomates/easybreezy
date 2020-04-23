import React, { useReducer, useRef } from "react";
import { LeftOutlined, RightOutlined } from "@ant-design/icons";
import formatDate from "date-fns/fp/format";
import { EmployeeShort, AbsencesMap } from "HumanResourceModels";
import { HumanResourceCalendarDay } from "./HumanResourceCalendarDay";
import { HumanResourceCalendarUser } from "./HumanResourceCalendarUser";
import { initial, reducer } from "./reducer";
import { getDateInterval, getMonthsFromInterval } from "./helpers";

import "./HumanResourceCalendar.scss";

type Props = {
  employees: EmployeeShort[];
  absences: AbsencesMap;
};

export const HumanResourceCalendar: React.FC<Props> = ({
  employees,
  absences,
}) => {
  const tableRef = useRef<HTMLTableElement>(null);

  const [{ date, daysCount }, dispatch] = useReducer(reducer, initial);
  const { interval, format, title } = getDateInterval(date, daysCount);
  const months = getMonthsFromInterval(interval);

  return (
    <div className="content human-resource-calendar">
      <LeftOutlined
        style={{ userSelect: "none" }}
        onClick={() => dispatch({ type: "move", direction: "back" })}
      />
      <RightOutlined
        style={{ userSelect: "none" }}
        onClick={() => dispatch({ type: "move", direction: "forward" })}
      />
      <table className="timetable" ref={tableRef}>
        <thead>
          <tr>
            <th className="timetable-title" rowSpan={2}>
              {title}
            </th>
            {months.map(([month, days]) => (
              <th key={month} colSpan={days.length}>
                {month}
              </th>
            ))}
          </tr>
          <tr>
            {interval.map((date) => (
              <th key={date.getTime()} className="timetable-date">
                {formatDate(format, date)}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {employees.map((employee) => (
            <tr key={employee.userId}>
              <td className="timetable-user">
                <HumanResourceCalendarUser employee={employee} />
              </td>
              {interval.map((date) => (
                <HumanResourceCalendarDay
                  key={date.toString()}
                  date={date}
                  absences={absences[employee.userId]}
                />
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
