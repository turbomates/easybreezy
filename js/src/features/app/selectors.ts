import { RootState } from "MyTypes";

export const canRender = (state: RootState) =>
  state.auth.status !== "initial" && state.auth.status !== "checking";
