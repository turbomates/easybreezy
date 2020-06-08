import { RootState } from "MyTypes";

export const selectLocation = (state: RootState) => state.location.location;

export const selectIsOpenCreateLocationForm = (state: RootState) =>
  state.location.location.isVisibleLocationForm;

export const selectCalendars = (state: RootState) => state.location.holiday.calendars;

export const selectHolidays = (state: RootState) => state.location.holiday.holidays;
