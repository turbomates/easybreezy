import { RootState } from "MyTypes";

export const isAuthorized = (state: RootState) =>
  state.auth.status === "authorized";

export const signInFailedReason = (state: RootState) =>
  state.auth.status === "unauthorized" ? state.auth.reason : "";
