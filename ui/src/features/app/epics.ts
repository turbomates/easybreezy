import { RootEpic } from 'MyTypes';
import { first, map } from 'rxjs/operators';

import { checkAuthAsync } from "../auth/actions";

export const loadDataOnAppStart: RootEpic = (action$, store, { api }) =>
  action$.pipe(
    first(),
    map(checkAuthAsync.request)
  );
