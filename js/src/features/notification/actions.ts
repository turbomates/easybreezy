import { createAction } from "typesafe-actions";

export const notificationSuccess = createAction("Notification success")<
  string
>();

export const notificationError = createAction("Notification error")<
  string
  >();
