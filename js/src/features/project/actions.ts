import { createAction, createAsyncAction } from "typesafe-actions";

import { FormError, Paging } from "MyTypes";
import {
  ProjectList,
  EditProjectStatusRequest,
  CreateProjectRequest,
  EditProjectDescriptionRequest,
  EditProjectRoleRequest,
  RemoveProjectRoleRequest,
  CreateProjectRoleRequest,
  Project,
  ProjectsRequest,
  RolePermissions,
  EditProjectSlugRequest,
  CreateProjectTeamRequest,
} from "ProjectModels";

//PROJECT
export const fetchProjectAsync = createAsyncAction(
  "FETCH_PROJECT_REQUEST",
  "FETCH_PROJECT_SUCCESS",
  "FETCH_PROJECT_FAILURE",
)<string, Project, any>();

// PROJECTS
export const fetchProjectsAsync = createAsyncAction(
  "FETCH_PROJECTS_REQUEST",
  "FETCH_PROJECTS_SUCCESS",
  "FETCH_PROJECTS_FAILURE",
)<ProjectsRequest, Paging<ProjectList>, string>();

//CREATE PROJECT
export const openProjectCreateForm = createAction("OPEN_PROJECT_CREATE_FORM")();

export const closeProjectCreateForm = createAction(
  "CLOSE_PROJECT_CREATE_FORM",
)();

export const createProjectAsync = createAsyncAction(
  "CREATE_PROJECT_REQUEST",
  "CREATE_PROJECT_SUCCESS",
  "CREATE_PROJECT_FAILURE",
)<CreateProjectRequest, undefined, FormError[]>();

// ROLE
export const createProjectRoleAsync = createAsyncAction(
  "CREATE_PROJECT_ROLE_REQUEST",
  "CREATE_PROJECT_ROLE_SUCCESS",
  "CREATE_PROJECT_ROLE_FAILURE",
)<CreateProjectRoleRequest, undefined, FormError[]>();

export const editProjectRoleAsync = createAsyncAction(
  "EDIT_PROJECT_ROLE_REQUEST",
  "EDIT_PROJECT_ROLE_SUCCESS",
  "EDIT_PROJECT_ROLE_FAILURE",
)<EditProjectRoleRequest, undefined, FormError[]>();

export const removeProjectRoleAsync = createAsyncAction(
  "REMOVE_PROJECT_ROLE_REQUEST",
  "REMOVE_PROJECT_ROLE_SUCCESS",
  "REMOVE_PROJECT_ROLE_FAILURE",
)<RemoveProjectRoleRequest, undefined, FormError[]>();

export const fetchProjectRoleAsync = createAsyncAction(
  "FETCH_PROJECT_ROLE_PERMISSIONS_REQUEST",
  "FETCH_PROJECT_ROLE_PERMISSIONS_SUCCESS",
  "FETCH_PROJECT_ROLE_PERMISSIONS_FAILURE",
)<undefined, RolePermissions, FormError[]>();

// STATUS
export const changeProjectStatusAsync = createAsyncAction(
  "CHANGE_PROJECT_STATUS_REQUEST",
  "CHANGE_PROJECT_STATUS_SUCCESS",
  "CHANGE_PROJECT_STATUS_FAILURE",
)<EditProjectStatusRequest, undefined, FormError[]>();

// DESCRIPTION
export const editProjectDescriptionAsync = createAsyncAction(
  "EDIT_PROJECT_DESCRIPTION_REQUEST",
  "EDIT_PROJECT_DESCRIPTION_SUCCESS",
  "EDIT_PROJECT_DESCRIPTION_FAILURE",
)<EditProjectDescriptionRequest, undefined, FormError[]>();

// SLUG
export const editProjectSlugAsync = createAsyncAction(
  "EDIT_PROJECT_SLUG_REQUEST",
  "EDIT_PROJECT_SLUG_SUCCESS",
  "EDIT_PROJECT_SLUG_FAILURE",
)<EditProjectSlugRequest, undefined, FormError[]>();

//TEAM
export const createProjectTeamAsync = createAsyncAction(
  "CREATE_PROJECT_PROJECT_REQUEST",
  "CREATE_PROJECT_PROJECT_SUCCESS",
  "CREATE_PROJECT_PROJECT_FAILURE",
)<CreateProjectTeamRequest, undefined, FormError[]>();

export const openProjectTeamCreateFormAction = createAction(
  "OPEN_PROJECT_TEAM_CREATE_FORM",
)();

export const closeProjectTeamCreateFormAction = createAction(
  "CLOSE_PROJECT_TEAM_CREATE_FORM",
)();
