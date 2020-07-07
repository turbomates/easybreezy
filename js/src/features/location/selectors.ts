import { RootState } from "MyTypes";

export const selectLocation = (state: RootState) => state.location.locations;

export const selectIsOpenCreateLocationForm = (state: RootState) =>
  state.location.locations.isVisibleLocationForm;

export const selectCalendars = (state: RootState) =>
  state.location.holiday.calendars;

export const selectHolidays = (state: RootState) =>
  state.location.holiday.holidays;

export const selectIsVisibleImportCalendar = (state: RootState) =>
  state.location.holiday.isVisibleImportCalendar;

export const selectIsVisibleEditCalendar = (state: RootState) =>
  state.location.holiday.isVisibleEditCalendar;
