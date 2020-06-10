import { filter, ignoreElements, tap } from "rxjs/operators";
import { isActionOf } from "typesafe-actions";
import { notification } from "antd";

import { RootEpic } from "MyTypes";
import { showNotification } from "./actions";

export const notificationEpic: RootEpic = (action$) =>
  action$.pipe(
    filter(isActionOf(showNotification)),
    tap(({ payload }) =>
      notification[payload.type]({
        message: payload.message ? payload.message : "Something went wrong",
      }),
    ),
    ignoreElements(),
  );
