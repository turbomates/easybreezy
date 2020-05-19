import { RootState } from "MyTypes";

// PROJECTS
export const selectProjects = (state: RootState) => state.project.projects.list;

export const selectCreateProjectFormErrors = (state: RootState) =>
  state.project.projects.createFormErrors;

export const selectIsOpenProjectCreateForm = (state: RootState) =>
  state.project.projects.isOpenCreateForm;

export const selectIsLoadingProjects = (state: RootState) =>
  state.project.projects.loading;

// PROJECT
export const selectProject = (state: RootState) => state.project.project.data;

export const selectIsLoadingProject = (state: RootState) =>
  state.project.project.loading;

export const selectProjectDescriptionFormErrors = (state: RootState) =>
  state.project.project.descriptionFormErrors;

export const selectProjectSlugFormErrors = (state: RootState) =>
  state.project.project.slugFormErrors;

// ROLE
export const selectRolePermissions = (state: RootState) =>
  state.project.project.rolePermissions;

// TEAMS
export const selectIsOpenCreateTeamForm = (state: RootState) =>
  state.project.project.isOpenCreateTeamForm;

export const selectNewTeamFormErrors = (state: RootState) =>
  state.project.project.newTeamFormErrors;

// TEAM
export const selectProjectTeam = (state: RootState) => state.project.team.data;

export const selectIsOpenNewTeamMemberForm = (state: RootState) =>
  state.project.team.isOpenNewMemberForm;

export const selectNewTeamMemberFormErrors = (state: RootState) =>
  state.project.team.newMemberFormErrors;

export const selectEmployeesSelectOptions = (state: RootState) =>
  state.humanResource.employees.items.map((emp) => ({
    value: emp.userId,
    label: `${emp.firstName} ${emp.lastName}`,
  }));
