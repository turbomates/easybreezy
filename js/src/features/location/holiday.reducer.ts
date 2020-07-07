import { createReducer } from "typesafe-actions";

import { Calendar, Holiday } from "LocationModels";
import {
  fetchCalendarsAsync,
  fetchHolidaysAsync,
  changeVisibilityImportCalendarAction,
  changeVisibilityEditCalendarAction,
  addHolidayAsync,
  editHolidayAsync,
} from "./actions";
import { FormErrorMap } from "MyTypes";
import { normalizeErrors } from "../../utils/errors";

export type State = {
  calendars: Calendar[];
  holidays: Holiday[];
  isVisibleImportCalendar: boolean;
  isVisibleEditCalendar: boolean;
  editHolidayForm: FormErrorMap;
  addHolidayForm: FormErrorMap;
};

const initialState: State = {
  calendars: [],
  holidays: [],
  isVisibleEditCalendar: false,
  isVisibleImportCalendar: false,
  editHolidayForm: {},
  addHolidayForm: {},
};

export const reducer = createReducer<State>(initialState)
  .handleAction(fetchCalendarsAsync.success, (state, action) => ({
    ...state,
    calendars: action.payload,
  }))
  .handleAction(fetchHolidaysAsync.success, (state, action) => ({
    ...state,
    holidays: action.payload,
  }))
  .handleAction(changeVisibilityImportCalendarAction, (state, action) => ({
    ...state,
    isVisibleImportCalendar: action.payload,
  }))
  .handleAction(changeVisibilityEditCalendarAction, (state, action) => ({
    ...state,
    isVisibleEditCalendar: action.payload,
  }))
  .handleAction(addHolidayAsync.failure, (state, action) => ({
    ...state,
    addHolidayForm: normalizeErrors(action.payload),
  }))
  .handleAction(editHolidayAsync.failure, (state, action) => ({
    ...state,
    editHolidayForm: normalizeErrors(action.payload),
  }));
