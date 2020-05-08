import { RootState } from "MyTypes";

export const canRender = (state: RootState) =>
  state.auth.info.status !== "initial" && state.auth.info.status !== "checking";
