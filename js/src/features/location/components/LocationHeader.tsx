import React, { useCallback } from "react";
import { Button, Modal, PageHeader, Descriptions } from "antd";

import { Location } from "LocationModels";
import { useDispatch, useSelector } from "react-redux";
import {
  changeCalendarAction,
  changeVisibilityEditCalendarAction,
  changeVisibilityImportCalendarAction,
  importCalendarAsync,
  removeCalendarAsync,
} from "../actions";
import { ImportCalendar } from "./Holiday/ImportCalendar";
import { EditCalendar } from "./Holiday/EditCalendar";
import {
  selectIsVisibleEditCalendar,
  selectIsVisibleImportCalendar,
} from "../selectors";

type Props = {
  location: Location;
  hasCalendar: boolean;
  calendarId: string;
};

export const LocationHeader: React.FC<Props> = ({
  location,
  hasCalendar,
  calendarId,
}) => {
  const dispatch = useDispatch();

  const isVisibleImportCalendar = useSelector(selectIsVisibleImportCalendar);
  const isVisibleEditCalendar = useSelector(selectIsVisibleEditCalendar);

  const changeVisibilityImportCalendar = useCallback(
    (visible: boolean) =>
      dispatch(changeVisibilityImportCalendarAction(visible)),
    [dispatch],
  );

  const changeVisibilityEditCalendar = useCallback(
    (visible: boolean) => dispatch(changeVisibilityEditCalendarAction(visible)),
    [dispatch],
  );

  const importCalendar = useCallback(
    (locationId: string, name: string) => (encodedCalendar: string) => {
      dispatch(
        importCalendarAsync.request({
          locationId,
          name,
          encodedCalendar,
        }),
      );
    },
    [dispatch],
  );

  const changeCalendar = useCallback(
    (locationId: string, name: string, calendarId: string) => (
      encodedCalendar: string,
    ) => {
      dispatch(
        changeCalendarAction({
          calendarId,
          locationId,
          name,
          encodedCalendar,
        }),
      );
    },
    [dispatch],
  );

  const removeCalendar = useCallback(
    (calendarId: string) => {
      dispatch(removeCalendarAsync.request(calendarId));
    },
    [dispatch],
  );

  return (
    <>
      <PageHeader
        className="page-header"
        title={location.name}
        extra={[
          <CalendarButton
            key="1"
            hasCalendar={hasCalendar}
            changeVisibilityImportCalendar={() =>
              changeVisibilityImportCalendar(true)
            }
            changeVisibilityEditCalendar={() =>
              changeVisibilityEditCalendar(true)
            }
          />,
        ]}
      >
        <Descriptions size="small">
          <Descriptions.Item label="Vacation days">
            {location.vacationDays}
          </Descriptions.Item>
        </Descriptions>
      </PageHeader>

      <Modal
        title="Import calendar"
        visible={isVisibleImportCalendar}
        onCancel={() => changeVisibilityImportCalendar(false)}
        footer={null}
        destroyOnClose={true}
      >
        <ImportCalendar
          importCalendar={importCalendar(location.id, location.name)}
        />
      </Modal>

      <Modal
        title="Edit calendar"
        visible={isVisibleEditCalendar}
        onCancel={() => changeVisibilityEditCalendar(false)}
        footer={null}
        destroyOnClose={true}
      >
        <EditCalendar
          remove={() => removeCalendar(calendarId)}
          change={changeCalendar(location.id, location.name, calendarId)}
        />
      </Modal>
    </>
  );
};

type CalendarButtonProps = {
  hasCalendar: boolean;
  changeVisibilityImportCalendar: () => void;
  changeVisibilityEditCalendar: () => void;
};

const CalendarButton: React.FC<CalendarButtonProps> = ({
  hasCalendar,
  changeVisibilityImportCalendar,
  changeVisibilityEditCalendar,
}) => {
  return (
    <>
      {hasCalendar ? (
        <Button type="primary" onClick={changeVisibilityEditCalendar}>
          Edit calendar
        </Button>
      ) : (
        <Button type="primary" onClick={changeVisibilityImportCalendar}>
          Import calendar
        </Button>
      )}
    </>
  );
};
