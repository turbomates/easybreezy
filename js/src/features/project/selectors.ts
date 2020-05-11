import { RootState } from "MyTypes";

export const getProjects = (state: RootState) =>
  state.project.projects.list;

export const getProject = (state: RootState) => state.project.project.data;

export const getIsOpenProjectCreateForm = (state: RootState) =>
  state.project.projects.isOpenCreateForm;

export const getIsLoading = (state: RootState) => state.project.project.loading;

export const getErrors = (state: RootState) => state.project.project.errors;

export const getRolePermissions = (state: RootState) =>
  state.project.project.rolePermissions;

export const getIsOpenCreateTeamForm = (state: RootState) =>
  state.project.project.isOpenCreateTeamForm;

export const getProjectTeam = (state: RootState) =>
  state.project.team.data;

export const getIsOpenTeamAddMemberForm = (state: RootState) =>
  state.project.team.isOpenAddMemberForm

export const getTeamErrors = (state: RootState) => state.project.team.errors;

export const getEmployees = (state: RootState) => state.humanResource.employees.items;

