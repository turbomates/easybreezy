import React, { useReducer, useRef } from "react";
import formatDate from "date-fns/fp/format";
import isToday from "date-fns/isToday";

import { EmployeeShort, AbsencesMap, VacationMap } from "HumanResourceModels";
import { HumanResourceCalendarDay } from "./HumanResourceCalendarDay";
import { HumanResourceCalendarUser } from "./HumanResourceCalendarUser";
import { HumanResourceCalendarControls } from "./HumanResourceCalendarControls";
import { initial, reducer } from "./reducer";
import {
  getAbsencesDays,
  getDateInterval,
  getMonthsFromInterval,
} from "./helpers";

import "./HumanResourceCalendar.scss";

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
    <div className="content timetable">
      <HumanResourceCalendarControls
        moveBack={() => dispatch({ type: "move", direction: "back" })}
        moveForward={() => dispatch({ type: "move", direction: "forward" })}
        today={() => dispatch({ type: "today" })}
      />
      <table
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
            {interval.map((date) => (
              <th
                key={date.getTime()}
                className={`timetable-date ${isToday(date) ? "today" : ""} `}
              >
                {formatDate(format, date)}
              </th>
            ))}
            <th className="timetable-title__info">Days left</th>
            <th className="timetable-title__info">Sick leave</th>
          </tr>
        </thead>
        <tbody>
          {employees.map((employee) => (
            <tr key={employee.userId}>
              <th className="timetable-user">
                <HumanResourceCalendarUser employee={employee} />
              </th>
              {interval.map((date) => (
                <HumanResourceCalendarDay
                  key={date.toString()}
                  date={date}
                  absences={absences[employee.userId]}
                />
              ))}
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
