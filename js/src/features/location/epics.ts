import { RootEpic } from "MyTypes";
import { from, of } from "rxjs";
import { filter, switchMap, map, catchError } from "rxjs/operators";
import { isActionOf } from "typesafe-actions";

import {
  fetchLocationsAsync,
  createLocationAsync,
  removeLocationAsync,
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
    switchMap(action =>
      from(api.location.fetchAll()).pipe(
        map(result =>
          result.success
            ? fetchLocationsAsync.success(result.data)
            : fetchLocationsAsync.failure(result.reason),
        ),
        catchError(message => of(fetchLocationsAsync.failure(message))),
      ),
    ),
  );

export const createLocationEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(createLocationAsync.request)),
    switchMap(action =>
      from(api.location.create(action.payload)).pipe(
        map(result =>
          result.success
            ? createLocationAsync.success()
            : createLocationAsync.failure(result.reason),
        ),
        catchError(message => of(createLocationAsync.failure(message))),
      ),
    ),
  );

export const removeLocationEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(removeLocationAsync.request)),
    switchMap(action =>
      from(api.location.remove(action.payload)).pipe(
        map(result =>
          result.success
            ? removeLocationAsync.success()
            : removeLocationAsync.failure(result.reason),
        ),
        catchError(message => of(removeLocationAsync.failure(message))),
      ),
    ),
  );
