import { RootEpic } from "MyTypes";
import { from, of } from "rxjs";
import { filter, switchMap, map, catchError, delay } from "rxjs/operators";
import { isActionOf } from "typesafe-actions";

import {
  fetchUsersVacationsAsync,
  fetchUserDetailsAsync,
  fetchProfileAsync,
} from "./actions";

export const fetchUsersEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(fetchUsersVacationsAsync.request)),
    switchMap(action =>
      from(api.humanResource.fetchVacations()).pipe(
        delay(1000),
        map(result =>
          result.success
            ? fetchUsersVacationsAsync.success(result.data)
            : fetchUsersVacationsAsync.failure(result.reason),
        ),
        catchError(message => of(fetchUsersVacationsAsync.failure(message))),
      ),
    ),
  );

export const fetchUserDetailsEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(fetchUserDetailsAsync.request)),
    switchMap(action =>
      from(api.humanResource.fetchUserDetails(action.payload)).pipe(
        delay(1000),
        map(result =>
          result.success
            ? fetchUserDetailsAsync.success(result.data)
            : fetchUserDetailsAsync.failure(result.reason),
        ),
        catchError(message => of(fetchUserDetailsAsync.failure(message))),
      ),
    ),
  );

export const fetchProfileEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(fetchProfileAsync.request)),
    switchMap(() =>
      from(api.humanResource.fetchProfile()).pipe(
        delay(1000),
        map(result =>
          result.success
            ? fetchProfileAsync.success(result.data)
            : fetchProfileAsync.failure(result.reason),
        ),
        catchError(message => of(fetchProfileAsync.failure(message))),
      ),
    ),
  );
