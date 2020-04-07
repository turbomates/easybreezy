import { RootEpic } from "MyTypes";
import { first, map } from "rxjs/operators";

import { checkAuth } from "../auth/actions";

export const loadDataOnAppStart: RootEpic = action$ =>
  action$.pipe(first(), map(checkAuth.request));
