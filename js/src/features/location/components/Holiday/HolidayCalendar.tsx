import React, { useCallback, useMemo, useState } from "react";
import Calendar from "react-calendar";
import { Card, Col, Row } from "antd";
import isSameDay from "date-fns/fp/isSameDay";
import format from "date-fns/fp/format";

import { HolidayCalendarEditDate } from "./HolidayCalendarEditDate";
import { HolidayCalendarAddDate } from "./HolidayCalendarAddDate";
import { AddHolidayForm, EditHolidayForm, Holiday } from "LocationModels";

import "./HolidayCalendar.scss";
import "react-calendar/dist/Calendar.css";

type Props = {
  holidays: Holiday[];
  add: (form: AddHolidayForm) => void;
  edit: (form: EditHolidayForm) => void;
  remove: (day: string) => void;
};

export const HolidayCalendar: React.FC<Props> = ({
  holidays,
  remove,
  edit,
  add,
}) => {
  const [activeDate, setActiveDate] = useState(new Date());

  const getHoliday = useCallback(
    (date: Date) => {
      return holidays.find((holiday) => isSameDay(date, new Date(holiday.day)));
    },
    [holidays],
  );

  const isWorkingDay = useCallback(
    (date: Date) => {
      const holiday = getHoliday(date);

      return holiday ? holiday.isWorkingDay : false;
    },
    [getHoliday],
  );

  const getDayType = useCallback(
    (date: Date) => {
      const holiday = getHoliday(date);

      if (holiday) {
        return holiday.isWorkingDay ? "Working day" : "Holiday day";
      }
    },
    [getHoliday],
  );

  const activeDateHolidayName = useMemo(() => {
    const holiday = getHoliday(activeDate);

    return holiday ? holiday.name : "";
  }, [activeDate, getHoliday]);

  return (
    <Row gutter={16} className="calendar">
      <Col>
        <Card>
          <Calendar
            value={activeDate}
            onClickDay={(date) => setActiveDate(date)}
            tileClassName={({ date }) => {
              const holiday = getHoliday(date);

              if (holiday) {
                return holiday.isWorkingDay ? " work" : "holiday";
              }

              return "";
            }}
          />
        </Card>
      </Col>
      <Col>
        <Card
          style={{ width: 400 }}
          className={`${activeDate < new Date() ? "cover" : ""}`}
          title={
            <span>
              {format("yyyy-MM-dd", activeDate)}{" "}
              <span className="day-type">{getDayType(activeDate)}</span>
            </span>
          }
        >
          {!activeDateHolidayName ? (
            <HolidayCalendarAddDate activeDate={activeDate} add={add} />
          ) : (
            <HolidayCalendarEditDate
              activeDate={activeDate}
              activeDateHolidayName={activeDateHolidayName}
              isWorkingDay={isWorkingDay(activeDate)}
              edit={edit}
              remove={remove}
            />
          )}
        </Card>
      </Col>
    </Row>
  );
};
