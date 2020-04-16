import { RootState } from "MyTypes";

export const account = (state: RootState) => state.account;

export const isAdmin = (state: RootState) =>
  state.account.profile?.roles.some((role) => role === "ADMIN");
