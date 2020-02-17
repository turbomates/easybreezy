import { RootEpic } from "MyTypes";
import { from, of } from "rxjs";
import { filter, switchMap, map, catchError, delay } from "rxjs/operators";
import { isActionOf } from "typesafe-actions";

import { fetchProfileAsync } from "./actions";

export const fetchProfileEpic: RootEpic = (action$, state$, { api }) =>
  action$.pipe(
    filter(isActionOf(fetchProfileAsync.request)),
    switchMap(() =>
      from(api.humanResource.fetchProfile()).pipe(
        delay(400),
        map(result =>
          result.success
            ? fetchProfileAsync.success(result.data)
            : fetchProfileAsync.failure(result.reason),
        ),
        catchError(message => of(fetchProfileAsync.failure(message))),
      ),
    ),
  );
