import { RootState } from "MyTypes";

export const vacations = (state: RootState) => state.humanResource.vacations;

export const userDetails = (state: RootState) => state.humanResource.details;

export const profile = (state: RootState) => state.humanResource.profile;
