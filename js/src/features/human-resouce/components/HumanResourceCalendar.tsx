import React from "react";
import Timeline from "react-calendar-timeline";
import moment from "moment";
import { HumanResourceCalendarGroup } from "./HumanResourceCalendarGroup";
import {
  CalendarVacationItem,
  CalendarVacationGroup,
} from "HumanResourceModels";

import "react-calendar-timeline/lib/Timeline.css";
import "./HumanResourceCalendar.scss";

const LINE_HEIGHT = 45;

const day = 60 * 60 * 24 * 1000;

const format = "DD.MM.YYYY";
const holidays = [
  moment("08.12.2019", format),
  moment("24.12.2019", format),
  moment("25.12.2019", format),
  moment("26.12.2019", format),
  moment("31.12.2019", format),
  moment("01.01.2020", format),
  moment("06.01.2020", format),
  moment("23.01.2020", format),
  moment("24.01.2020", format),
  moment("25.01.2020", format),
];

// TODO disable default behaviour and render working days and holidays
const verticalLineClassNamesForTime = (timeStart: number, timeEnd: number) => {
  const currentTimeStart = moment(timeStart);
  const currentTimeEnd = moment(timeEnd);

  let classes = [];
  for (let holiday of holidays) {
    if (
      holiday.isSame(currentTimeStart, "day") &&
      holiday.isSame(currentTimeEnd, "day")
    ) {
      classes.push("holiday");
    }
  }

  return classes;
};

interface Props {
  items: CalendarVacationItem[];
  groups: CalendarVacationGroup[];
}

export const HumanResourceCalendar: React.FC<Props> = ({ items, groups }) => (
  <div className="content human-resource-calendar">
    <Timeline
      groups={groups}
      groupRenderer={data => (
        <HumanResourceCalendarGroup
          username={data.group.item.username}
          avatar={data.group.item.avatar}
          id={data.group.item.id}
        />
      )}
      items={items}
      itemHeightRatio={0.7}
      lineHeight={LINE_HEIGHT}
      defaultTimeStart={moment().add(-6, "month")}
      defaultTimeEnd={moment().add(6, "month")}
      canResize="both"
      dragSnap={day}
      verticalLineClassNamesForTime={verticalLineClassNamesForTime}
    ></Timeline>
  </div>
);
