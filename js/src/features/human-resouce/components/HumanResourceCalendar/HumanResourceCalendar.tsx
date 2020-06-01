import React, { useReducer, useRef } from "react";
import { LeftOutlined, RightOutlined } from "@ant-design/icons";
import formatDate from "date-fns/fp/format";

import { EmployeeShort, AbsencesMap, VacationMap } from "HumanResourceModels";
import { HumanResourceCalendarDay } from "./HumanResourceCalendarDay";
import { HumanResourceCalendarUser } from "./HumanResourceCalendarUser";
import { initial, reducer } from "./reducer";
import {
  getAbsencesDays,
  getDateInterval,
  getMonthsFromInterval,
} from "./helpers";

import "./HumanResourceCalendar.scss";
import { HumanResourceCalendarUserAvatar } from "./HumanResourceCalendarUserAvatar";

type Props = {
  employees: EmployeeShort[];
  absences: AbsencesMap;
  vacations: VacationMap;
};

export const HumanResourceCalendar: React.FC<Props> = ({
  employees,
  absences,
  vacations,
}) => {
  const tableRef = useRef<HTMLTableElement>(null);

  const [{ date, daysCount, scrollDaysCount }, dispatch] = useReducer(
    reducer,
    initial,
  );
  const { interval, format, title } = getDateInterval(date, daysCount);
  const months = getMonthsFromInterval(interval);

  return (
    <div className="content human-resource-calendar">
      <table
        className="timetable"
        ref={tableRef}
        onWheel={(e) => {
          dispatch({
            type: "scroll",
            daysCount: Math.sign(e.deltaY) * scrollDaysCount,
          });
        }}
      >
        <thead>
          <tr>
            <th className="timetable-title" rowSpan={2}>
              {title}
            </th>
            <th />
            {months.map(([month, days]) => (
              <th
                key={month}
                colSpan={days.length}
                className="timetable-title__month"
              >
                {month}
              </th>
            ))}
          </tr>
          <tr>
            <th>
              <LeftOutlined
                style={{ userSelect: "none" }}
                onClick={() => dispatch({ type: "move", direction: "back" })}
              />
            </th>
            {interval.map((date) => (
              <th key={date.getTime()} className="timetable-date">
                {formatDate(format, date)}
              </th>
            ))}
            <th>
              <RightOutlined
                style={{ userSelect: "none" }}
                onClick={() => dispatch({ type: "move", direction: "forward" })}
              />
            </th>
            <th>Vacation</th>
            <th>Sick</th>
          </tr>
        </thead>
        <tbody>
          {employees.map((employee) => (
            <tr key={employee.userId} className="select-row">
              <th className="timetable-user">
                <HumanResourceCalendarUserAvatar employee={employee}>
                  <HumanResourceCalendarUser employee={employee} />
                </HumanResourceCalendarUserAvatar>
              </th>
              <td />
              {interval.map((date) => (
                <HumanResourceCalendarDay
                  key={date.toString()}
                  date={date}
                  absences={absences[employee.userId]}
                />
              ))}
              <td />
              <td className="timetable-info">
                {absences[employee.userId] &&
                  vacations[employee.userId] &&
                  vacations[employee.userId].days -
                    getAbsencesDays(absences[employee.userId], "VACATION")}
              </td>
              <td className="timetable-info">
                {absences[employee.userId] &&
                  getAbsencesDays(absences[employee.userId], "SICK")}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
