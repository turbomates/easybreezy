import { first, mergeMap } from "rxjs/operators";
import { RootEpic } from "MyTypes";
import { checkAuth, fetchRulesAsync } from "../auth/actions";

export const loadDataOnAppStart: RootEpic = (action$) =>
  action$.pipe(
    first(),
    mergeMap(() => [checkAuth.request(), fetchRulesAsync.request()]),
  );
