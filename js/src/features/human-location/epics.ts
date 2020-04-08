import { RootEpic } from "MyTypes";
import { from, of } from "rxjs";
import { filter, switchMap, map, catchError } from "rxjs/operators";
import { isActionOf } from "typesafe-actions";
import {
  fetchLocationsAsync,
  createLocationAsync,
  removeLocationAsync,
  fetchEmployeesAsync,
  assignLocationAsync,
  fetchEmployeeLocationsAsync,
  removeEmployeeLocationAsync,
  editEmployeeLocationAsync,
} from "./actions";

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
            : fetchLocationsAsync.failure(result.reason),
        ),
        catchError((message) => of(fetchLocationsAsync.failure(message))),
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
        catchError((message) => of(createLocationAsync.failure(message))),
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
            : removeLocationAsync.failure(result.reason),
        ),
        catchError((message) => of(removeLocationAsync.failure(message))),
      ),
    ),
  );

export const removeEmployeeLocationEpic: RootEpic = (
  action$,
  state$,
  { api },
) =>
  action$.pipe(
    filter(isActionOf(removeEmployeeLocationAsync.request)),
    switchMap((action) =>
      from(api.location.removeUserLocation(action.payload)).pipe(
        map((result) =>
          result.success
            ? removeEmployeeLocationAsync.success()
            : removeEmployeeLocationAsync.failure(result.reason),
        ),
        catchError((message) =>
          of(removeEmployeeLocationAsync.failure(message)),
        ),
      ),
    ),
  );

export const fetchEmployeesEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf([fetchEmployeesAsync.request])),
    switchMap((action) =>
      from(api.realHr.fetchEmployees()).pipe(
        map((result) =>
          result.success
            ? fetchEmployeesAsync.success(result.data)
            : fetchEmployeesAsync.failure(result.reason),
        ),
        catchError((message) => of(fetchEmployeesAsync.failure(message))),
      ),
    ),
  );

export const assignLocationEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(assignLocationAsync.request)),
    switchMap((action) =>
      from(api.location.assignUserLocations(action.payload)).pipe(
        map((result) =>
          result.success
            ? assignLocationAsync.success()
            : assignLocationAsync.failure(result.errors),
        ),
        catchError((message) => of(assignLocationAsync.failure(message))),
      ),
    ),
  );

export const editEmployeeLocationEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(editEmployeeLocationAsync.request)),
    switchMap((action) =>
      from(api.location.editUserLocation(action.payload)).pipe(
        map((result) =>
          result.success
            ? editEmployeeLocationAsync.success()
            : editEmployeeLocationAsync.failure(result.errors),
        ),
        catchError((message) => of(editEmployeeLocationAsync.failure(message))),
      ),
    ),
  );

export const fetchEmployeeLocationsEpic: RootEpic = (
  action$,
  state$,
  { api },
) =>
  action$.pipe(
    filter(
      isActionOf([
        fetchEmployeeLocationsAsync.request,
        assignLocationAsync.success,
        removeEmployeeLocationAsync.success,
        editEmployeeLocationAsync.success,
      ]),
    ),
    switchMap((action) =>
      from(api.location.fetchUserLocations()).pipe(
        map((result) =>
          result.success
            ? fetchEmployeeLocationsAsync.success(result.data)
            : fetchEmployeeLocationsAsync.failure(result.reason),
        ),
        catchError((message) =>
          of(fetchEmployeeLocationsAsync.failure(message)),
        ),
      ),
    ),
  );
