import { RootState } from "MyTypes";

export const users = (state: RootState) => state.humanResource.list;

export const userDetails = (state: RootState) => state.humanResource.details;

export const profile = (state: RootState) => state.humanResource.profile;
