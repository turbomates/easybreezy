import React, { useReducer, useRef } from "react";
import { LeftOutlined, RightOutlined } from "@ant-design/icons";
import eachDay from "date-fns/fp/eachDayOfInterval";
import getYear from "date-fns/fp/getYear";
import addDate from "date-fns/fp/add";
import parseISO from "date-fns/fp/parseISO";
import formatDate from "date-fns/fp/format";
import isWithinInterval from "date-fns/fp/isWithinInterval";
import isWeekend from "date-fns/fp/isWeekend";
import compose from "lodash/fp/compose";
import groupBy from "lodash/fp/groupBy";
import toPairs from "lodash/fp/toPairs";
import toLower from "lodash/fp/toLower";

import "./HumanResourceCalendar.scss";

import { EmployeeShort, AbsencesMap, Absence } from "HumanResourceModels";
import { Tooltip } from "antd";
import { Link } from "react-router-dom";

type State = { date: Date; daysCount: number };
type Action =
  | { type: "move"; direction: "back" | "forward" }
  | { type: "setDaysCount"; daysCount: number };

type Props = {
  employees: EmployeeShort[];
  absences: AbsencesMap;
};

export const HumanResourceCalendar: React.FC<Props> = ({
  employees,
  absences,
}) => {
  const tableRef = useRef<HTMLTableElement>(null);

  const [{ date, daysCount }, dispatch] = useReducer(reducer, {
    date: new Date(),
    daysCount: 21,
  });

  const { interval, format, title } = getDateInterval(date, daysCount);
  const months = compose(toPairs, groupBy(formatDate("MMM")))(interval);

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
                <Link to={`/users/${employee.userId}`}>
                  {employee.firstName} {employee.lastName}
                </Link>
              </td>
              {interval.map((date) => (
                <TimelineCell
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

type CellProps = {
  date: Date;
  absences: Absence[];
};

export const TimelineCell: React.FC<CellProps> = ({ date, absences = [] }) => {
  const { className, title } = generateCellInfo(date, absences);

  if (!className) {
    return <td className="timetable-cell" />;
  }

  return (
    <Tooltip title={title}>
      <td className={`timetable-cell ${className}`} />
    </Tooltip>
  );
};

function reducer(state: State, action: Action): State {
  switch (action.type) {
    case "move":
      const days =
        action.direction === "back" ? -state.daysCount : state.daysCount;
      return { ...state, date: addDate({ days }, state.date) };
    case "setDaysCount":
      return {
        ...state,
        daysCount: action.daysCount >= 30 ? 30 : action.daysCount,
      };
    default:
      return state;
  }
}

function getDateInterval(date: Date, cells: number) {
  const start = addDate({ days: -cells }, date);
  const end = addDate({ days: cells }, date);
  const title =
    getYear(start) === getYear(end)
      ? `${formatDate("MMM", start)} - ${formatDate("MMM yyyy", end)}`
      : `${formatDate("MMM yyyy", start)} - ${formatDate("MMM yyyy", end)}`;
  return { interval: eachDay({ start, end }), format: "dd", title };
}

function generateCellInfo(date: Date, absences: Absence[] = []) {
  for (const absence of absences) {
    const interval = {
      start: parseISO(absence.startedAt),
      end: parseISO(absence.endedAt),
    };
    if (isWithinInterval(interval, date)) {
      const reason = toLower(absence.reason);
      const approved = absence.isApproved ? "" : "not-approved";
      const title = absence.isApproved
        ? absence.comment
        : `${absence.comment} (not approved)`;
      return { className: `${reason} ${approved}`, title };
    }
  }
  if (isWeekend(date)) {
    return { className: "weekend", title: formatDate("eeee", date) };
  }
  return { className: "", title: "" };
}
