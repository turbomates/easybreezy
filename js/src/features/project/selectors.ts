import { RootState } from "MyTypes";

export const selectProjects = (state: RootState) => state.project.projects.list;

export const selectProject = (state: RootState) => state.project.project.data;

export const selectIsOpenProjectCreateForm = (state: RootState) =>
  state.project.projects.isOpenCreateForm;

export const selectIsLoading = (state: RootState) => state.project.project.loading;

export const selectErrors = (state: RootState) => state.project.project.errors;

export const selectRolePermissions = (state: RootState) =>
  state.project.project.rolePermissions;

export const selectIsOpenCreateTeamForm = (state: RootState) =>
  state.project.project.isOpenCreateTeamForm;

export const selectProjectTeam = (state: RootState) => state.project.team.data;

export const selectIsOpenTeamAddMemberForm = (state: RootState) =>
  state.project.team.isOpenNewMemberForm;

export const selectTeamErrors = (state: RootState) => state.project.team.errors;

export const selectEmployeesSelectOptions = (state: RootState) =>
  state.humanResource.employees.items.map((emp) => ({
    value: emp.userId,
    label: `${emp.firstName} ${emp.lastName}`,
  }));
