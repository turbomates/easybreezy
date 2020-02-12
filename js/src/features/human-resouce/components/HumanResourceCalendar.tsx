import React from "react";
import Timeline from "react-calendar-timeline";
import moment from "moment";
// import containerResizeDetector from "react-calendar-timeline/lib/resize-detector/container";

import "react-calendar-timeline/lib/Timeline.css";
import "./HumanResourceCalendar.css";

const groups = [
  { id: 1, title: "Dmitry" },
  { id: 2, title: "Vadim" },
  { id: 3, title: "Andrew" },
  { id: 4, title: "Ivan" },
  { id: 5, title: "Vova" },
  { id: 6, title: "Alex" },
  { id: 7, title: "Anna" },
];

const startingPoint = moment("2020-01-24");

const items = [
  {
    id: 1,
    group: 1,
    title: "item 1",
    start_time: moment(startingPoint),
    end_time: moment(startingPoint).add(1, "week"),
  },
  {
    id: 2,
    group: 2,
    title: "item 2",
    start_time: moment(startingPoint).add(-0.5, "week"),
    end_time: moment(startingPoint).add(3, "day"),
  },
  {
    id: 3,
    group: 3,
    title: "item 3",
    start_time: moment(startingPoint).add(2, "day"),
    end_time: moment(startingPoint).add(3, "day"),
  },
  {
    id: 4,
    group: 4,
    title: "Vacation 4",
    start_time: moment(startingPoint).add(1, "month"),
    end_time: moment(startingPoint).add(2, "month"),
  },
  {
    id: 5,
    group: 5,
    title: "Vacation 5",
    start_time: moment(startingPoint).add(-25, "day"),
    end_time: moment(startingPoint).add(-10, "day"),
  },
  {
    id: 6,
    group: 6,
    title: "Vacation 6",
    start_time: moment(startingPoint).add(-20, "day"),
    end_time: moment(startingPoint).add(-15, "day"),
  },
  {
    id: 7,
    group: 7,
    title: "Vacation 7",
    start_time: moment(startingPoint).add(-40, "day"),
    end_time: moment(startingPoint).add(-35, "day"),
  },
];

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

export const HumanResourceCalendar = () => {
  return (
    <div className="content human-resource-calendar">
      <Timeline
        groups={groups}
        items={items}
        defaultTimeStart={moment().add(-6, "month")}
        defaultTimeEnd={moment().add(6, "month")}
        canResize="both"
        dragSnap={day}
        verticalLineClassNamesForTime={verticalLineClassNamesForTime}
      ></Timeline>
    </div>
  );
};
