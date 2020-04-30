import { RootState } from "MyTypes";

export const getProjects = (state: RootState) =>
  state.project.projects.projects;

export const getProject = (state: RootState) => state.project.project.project;

export const getIsOpenProjectCreateForm = (state: RootState) =>
  state.project.projects.isOpenCreateForm;

export const getIsLoading = (state: RootState) => state.project.project.loading;

export const getErrors = (state: RootState) => state.project.project.errors;

export const getIsOpenProjectDescriptionForm = (state: RootState) =>
  state.project.project.isOpenDescriptionForm;

export const getRolePermissions = (state: RootState) =>
  state.project.project.rolePermissions;
