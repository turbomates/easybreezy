import React, { useMemo, useState } from "react";
import Calendar from "react-calendar";
import { Card, Col, Row, Tabs } from "antd";
import isSameDay from "date-fns/fp/isSameDay";

import { HolidayCalendarMoveDate } from "./HolidayCalendarMoveDate";
import { HolidayCalendarRemoveDate } from "./HolidayCalendarRemoveDate";
import { HolidayCalendarAddDate } from "./HolidayCalendarAddDate";
import { Holiday } from "LocationModels";

import "./HolidayCalendar.scss";
import "react-calendar/dist/Calendar.css";

type Props = {
  holidays: Holiday[];
};

export const HolidayCalendar: React.FC<Props> = ({ holidays }) => {
  const [activeDate, setActiveDate] = useState(new Date());

  const activeDateHolidayName = useMemo(() => {
    const holiday = holidays.find((holiday) =>
      isSameDay(activeDate, new Date(holiday.day)),
    );

    return holiday ? holiday.name : "";
  }, [activeDate, holidays]);

  return (
    <Row gutter={16}>
      <Col>
        <Card>
          <Calendar
            value={activeDate}
            onClickDay={(date) => setActiveDate(date)}
            tileClassName={({ date }) => {
              const holiday = holidays.find((holiday) =>
                isSameDay(date, new Date(holiday.day)),
              );

              if (holiday) {
                return holiday.isWorkingDay ? " work" : "holiday";
              }

              return "";
            }}
          />
        </Card>
      </Col>
      <Col>
        <Card style={{ width: 400 }}>
          <Tabs defaultActiveKey="add" type="card">
            <Tabs.TabPane tab="Add" key="add">
              <HolidayCalendarAddDate
                activeDate={activeDate}
                activeDateHolidayName={activeDateHolidayName}
              />
            </Tabs.TabPane>
            <Tabs.TabPane tab="Move" key="move">
              <HolidayCalendarMoveDate
                activeDate={activeDate}
                activeDateHolidayName={activeDateHolidayName}
              />
            </Tabs.TabPane>
            <Tabs.TabPane tab="Remove" key="remove">
              <HolidayCalendarRemoveDate
                activeDate={activeDate}
                activeDateHolidayName={activeDateHolidayName}
              />
            </Tabs.TabPane>
          </Tabs>
        </Card>
      </Col>
    </Row>
  );
};
