import { createReducer } from "typesafe-actions";

import { Calendar, Holiday } from "LocationModels";
import {
  fetchCalendarsAsync,
  fetchHolidaysAsync, clearHolidaysAction,
} from "./actions"

export type State = {
  calendars: Calendar[];
  holidays: Holiday[];
};

const initialState: State = {
  calendars: [],
  holidays: [],
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
  .handleAction(clearHolidaysAction, (state => ({
    ...state,
    holidays: []
  })))
