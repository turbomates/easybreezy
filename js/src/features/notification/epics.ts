import { filter, ignoreElements, tap } from "rxjs/operators";
import { isActionOf } from "typesafe-actions";
import { notification } from "antd";

import { RootEpic } from "MyTypes";
import { notificationError, notificationSuccess } from "./actions";

export const notificationSuccessEpic: RootEpic = (action$) =>
  action$.pipe(
    filter(isActionOf(notificationSuccess)),
    tap((action) => {
      console.log("test");
      notification.success({ message: action.payload });
    }),
    ignoreElements(),
  );

export const notificationFailedEpic: RootEpic = (action$) =>
  action$.pipe(
    filter(isActionOf(notificationError)),
    tap((action) => {
      console.log(action.payload);
      notification.error({ message: action.payload });
    }),
    ignoreElements(),
  );
