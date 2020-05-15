import { RootState } from "MyTypes";

export const account = (state: RootState) => state.account;

export const isAdmin = (state: RootState) =>
  state.account.profile?.activities.some((activity) => activity === "ADMIN");

export const getActivities = (state: RootState) =>
  state.account.profile?.activities || [];
