import { RootEpic } from "MyTypes";
import { from, of } from "rxjs";
import { filter, switchMap, map, catchError } from "rxjs/operators";
import { isActionOf } from "typesafe-actions";

import { checkAuthAsync, signInAsync } from "./actions";

export const checkAuthEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(checkAuthAsync.request)),
    switchMap(() =>
      from(api.auth.check()).pipe(
        map(result =>
          result.success
            ? checkAuthAsync.success(result.data)
            : checkAuthAsync.failure(result.reason),
        ),
        catchError(message => of(checkAuthAsync.failure(message))),
      ),
    ),
  );

export const signInEpic: RootEpic = (action$, state$, { api, logger }) =>
  action$.pipe(
    filter(isActionOf(signInAsync.request)),
    switchMap(action =>
      from(api.auth.signIn(action.payload)).pipe(
        map(result =>
          result.success
            ? signInAsync.success(result.data)
            : signInAsync.failure(result.reason),
        ),
        catchError(message => of(signInAsync.failure(message))),
      ),
    ),
  );
