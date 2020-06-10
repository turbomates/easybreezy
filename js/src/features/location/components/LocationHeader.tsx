import React from "react";
import { Button, PageHeader } from "antd";

import { Location } from "LocationModels";

type Props = {
  location: Location;
  hasCalendar: boolean;
};

export const LocationHeader: React.FC<Props> = ({ location, hasCalendar }) => {
  return (
    <PageHeader
      className="page-header"
      title={location.name}
      subTitle={location.vacationDays}
      extra={[<CalendarButton key="1" hasCalendar={hasCalendar} />]}
    />
  );
};

type CalendarButtonProps = {
  hasCalendar: boolean;
};

const CalendarButton: React.FC<CalendarButtonProps> = ({ hasCalendar }) => {
  return (
    <>
      {hasCalendar ? (
        <Button type="primary">Edit calendar</Button>
      ) : (
        <Button type="primary">Import calendar</Button>
      )}
    </>
  );
};
