import { createAction } from "typesafe-actions";

export const showNotification = createAction("Show notification")<{
  type: "success" | "error" | "info" | "warning" | "open";
  message?: string;
}>();
