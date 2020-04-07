import { RootEpic } from "MyTypes";
import { User } from "AuthModels";
import { from, of } from "rxjs";
import { filter, switchMap, map, catchError, tap } from "rxjs/operators";
import { isActionOf } from "typesafe-actions";

import { checkAuth, signInAsync, signOutAsync } from "./actions";

export const checkAuthEpic: RootEpic = (action$, state$, { jwt }) =>
  action$.pipe(
    filter(isActionOf(checkAuth.request)),
    map(() => {
      const token = jwt.get();
      return token
        ? checkAuth.success(jwt.decode<User>(token))
        : checkAuth.failure();
    }),
  );

export const signInEpic: RootEpic = (action$, state$, { api, jwt }) =>
  action$.pipe(
    filter(isActionOf(signInAsync.request)),
    switchMap(action =>
      from(api.auth.signIn(action.payload)).pipe(
        map(result => {
          if (result.success) {
            const token = result.data;
            jwt.set(token);
            return signInAsync.success(jwt.decode(token));
          } else {
            return signInAsync.failure(result.reason);
          }
        }),

        catchError(message => of(signInAsync.failure(message))),
      ),
    ),
  );

export const signOutEpic: RootEpic = (action$, state$, { jwt }) =>
  action$.pipe(
    filter(isActionOf(signOutAsync.request)),
    tap(() => {
      jwt.remove();
    }),
    map(() => {
      return signOutAsync.success();
    }),
  );
