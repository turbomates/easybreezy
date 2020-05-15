import { RootState } from "MyTypes";

export const isAuthorized = (state: RootState) =>
  state.auth.info.status === "authorized";

export const signInFailedReason = (state: RootState) =>
  state.auth.info.status === "unauthorized" ? state.auth.info.reason : "";

export const authUserId = (state: RootState) => {
  if (state.auth.info.status !== "authorized") return null;

  return state.auth.info.user.sub;
};

export const getRules = (state: RootState) => state.auth.rules;
