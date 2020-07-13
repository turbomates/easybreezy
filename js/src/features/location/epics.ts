import { RootEpic } from "MyTypes";
import { from } from "rxjs";
import { filter, switchMap, map, mergeMap } from "rxjs/operators";
import { isActionOf } from "typesafe-actions";
import {
  fetchLocationsAsync,
  createLocationAsync,
  removeLocationAsync,
  fetchCalendarsAsync,
  fetchHolidaysAsync,
  importCalendarAsync,
  removeCalendarAsync,
  addHolidayAsync,
  editHolidayAsync,
  removeHolidayAsync,
  changeCalendarAction,
  changeVisibilityEditCalendarAction,
  changeVisibilityImportCalendarAction,
} from "./actions";
import { showNotification } from "../notification/actions";

export const fetchLocationsEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(
      isActionOf([
        fetchLocationsAsync.request,
        createLocationAsync.success,
        removeLocationAsync.success,
      ]),
    ),
    switchMap((action) =>
      from(api.location.fetchAll()).pipe(
        map((result) =>
          result.success
            ? fetchLocationsAsync.success(result.data)
            : showNotification({ type: "error" }),
        ),
      ),
    ),
  );

export const createLocationEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(createLocationAsync.request)),
    switchMap((action) =>
      from(api.location.create(action.payload)).pipe(
        map((result) =>
          result.success
            ? createLocationAsync.success()
            : createLocationAsync.failure(result.errors),
        ),
      ),
    ),
  );

export const removeLocationEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(removeLocationAsync.request)),
    switchMap((action) =>
      from(api.location.remove(action.payload)).pipe(
        map((result) =>
          result.success
            ? removeLocationAsync.success()
            : showNotification({ type: "error" }),
        ),
      ),
    ),
  );

export const fetchCalendars: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(fetchCalendarsAsync.request)),
    switchMap(() =>
      from(api.calendar.fetchCalendars()).pipe(
        map((result) =>
          result.success
            ? fetchCalendarsAsync.success(result.data)
            : showNotification({ type: "error" }),
        ),
      ),
    ),
  );

export const fetchHolidays: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(fetchHolidaysAsync.request)),
    switchMap((action) =>
      from(api.calendar.fetchHolidays(action.payload)).pipe(
        map((result) =>
          result.success
            ? fetchHolidaysAsync.success(result.data)
            : showNotification({ type: "error" }),
        ),
      ),
    ),
  );

export const importCalendar: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(importCalendarAsync.request)),
    switchMap((action) =>
      from(api.calendar.importCalendar(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                importCalendarAsync.success(),
                fetchCalendarsAsync.request(),
                changeVisibilityImportCalendarAction(false),
                changeVisibilityEditCalendarAction(false),
              ]
            : [showNotification({ type: "error" })],
        ),
      ),
    ),
  );

export const removeCalendar: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(removeCalendarAsync.request)),
    switchMap((action) =>
      from(api.calendar.removeCalendar(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                removeCalendarAsync.success(),
                fetchCalendarsAsync.request(),
                changeVisibilityEditCalendarAction(false),
              ]
            : [showNotification({ type: "error" })],
        ),
      ),
    ),
  );

export const changeCalendar: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(changeCalendarAction)),
    switchMap((action) =>
      from(api.calendar.removeCalendar(action.payload.calendarId)).pipe(
        map((result) =>
          result.success
            ? importCalendarAsync.request({
                name: action.payload.name,
                locationId: action.payload.locationId,
                encodedCalendar: action.payload.encodedCalendar,
              })
            : showNotification({ type: "error" }),
        ),
      ),
    ),
  );

export const addHoliday: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(addHolidayAsync.request)),
    switchMap((action) =>
      from(api.calendar.addHoliday(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                addHolidayAsync.success(),
                fetchHolidaysAsync.request(action.payload.calendarId),
              ]
            : [addHolidayAsync.failure(result.errors)],
        ),
      ),
    ),
  );

export const editHoliday: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(editHolidayAsync.request)),
    switchMap((action) =>
      from(api.calendar.editHoliday(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                editHolidayAsync.success(),
                fetchHolidaysAsync.request(action.payload.calendarId),
              ]
            : [editHolidayAsync.failure(result.errors)],
        ),
      ),
    ),
  );

export const removeHoliday: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(removeHolidayAsync.request)),
    switchMap((action) =>
      from(api.calendar.removeHoliday(action.payload)).pipe(
        mergeMap((result) =>
          result.success
            ? [
                removeHolidayAsync.success(),
                fetchHolidaysAsync.request(action.payload.calendarId),
              ]
            : [showNotification({ type: "error" })],
        ),
      ),
    ),
  );
