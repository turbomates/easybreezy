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
  ProjectsListQuery,
  RolePermissions,
  EditProjectSlugRequest,
  CreateProjectTeamRequest,
  ProjectTeam,
  EditProjectTeamMemberRoleRequest,
  RemoveProjectTeamMemberRequest,
  NewProjectTeamMemberRequest,
  ChangeProjectTeamStatusRequest,
  AddProjectStatusRequest,
  RemoveProjectStatusRequest,
  ChangeProjectStatusRequest,
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
)<ProjectsListQuery, Paging<ProjectList>, string>();

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
)<RemoveProjectRoleRequest, undefined, string>();

export const fetchProjectRoleAsync = createAsyncAction(
  "FETCH_PROJECT_ROLE_PERMISSIONS_REQUEST",
  "FETCH_PROJECT_ROLE_PERMISSIONS_SUCCESS",
  "FETCH_PROJECT_ROLE_PERMISSIONS_FAILURE",
)<undefined, RolePermissions, string>();

// STATUS
export const changeProjectStatusAsync = createAsyncAction(
  "CHANGE_PROJECT_STATUS_REQUEST",
  "CHANGE_PROJECT_STATUS_SUCCESS",
  "CHANGE_PROJECT_STATUS_FAILURE",
)<EditProjectStatusRequest, undefined, string>();

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

export const fetchProjectTeamAsync = createAsyncAction(
  "FETCH_PROJECT_TEAM_REQUEST",
  "FETCH_PROJECT_TEAM_SUCCESS",
  "FETCH_PROJECT_TEAM_FAILED",
)<string, ProjectTeam, FormError[]>();

export const editProjectTeamMemberRoleAsync = createAsyncAction(
  "EDIT_PROJECT_TEAM_MEMBER_ROLE_REQUEST",
  "EDIT_PROJECT_TEAM_MEMBER_ROLE_SUCCESS",
  "EDIT_PROJECT_TEAM_MEMBER_ROLE_FAILED",
)<EditProjectTeamMemberRoleRequest, undefined, string>();

export const removeProjectTeamMemberAsync = createAsyncAction(
  "REMOVE_PROJECT_TEAM_MEMBER_REQUEST",
  "REMOVE_PROJECT_TEAM_MEMBER_SUCCESS",
  "REMOVE_PROJECT_TEAM_MEMBER_FAILED",
)<RemoveProjectTeamMemberRequest, undefined, string>();

export const addProjectTeamMemberAsync = createAsyncAction(
  "ADD_PROJECT_TEAM_MEMBER_REQUEST",
  "ADD_PROJECT_TEAM_MEMBER_SUCCESS",
  "ADD_PROJECT_TEAM_MEMBER_FAILED",
)<NewProjectTeamMemberRequest, undefined, FormError[]>();

export const openProjectTeamNewMemberFormAction = createAction(
  "OPEN_PROJECT_TEAM_NEW_MEMBER_FORM",
)();

export const closeProjectTeamNewMemberFormAction = createAction(
  "CLOSE_PROJECT_TEAM_NEW_MEMBER_FORM",
)();

export const changeProjectTeamStatusAsync = createAsyncAction(
  "CHANGE_PROJECT_TEAM_STATUS_REQUEST",
  "CHANGE_PROJECT_TEAM_STATUS_SUCCESS",
  "CHANGE_PROJECT_TEAM_STATUS_FAILED",
)<ChangeProjectTeamStatusRequest, undefined, string>();

// ISSUE STATUSES
export const addProjectIssueStatusAsync = createAsyncAction(
  "ADD_PROJECT_ISSUE_STATUS_REQUEST",
  "ADD_PROJECT_ISSUE_STATUS_SUCCESS",
  "ADD_PROJECT_ISSUE_STATUS_FAILED",
)<AddProjectStatusRequest, undefined, FormError[]>();

export const changeProjectIssueStatusAsync = createAsyncAction(
  "CHANGE_PROJECT_ISSUE_STATUS_REQUEST",
  "CHANGE_PROJECT_ISSUE_STATUS_SUCCESS",
  "CHANGE_PROJECT_ISSUE_STATUS_FAILED",
)<ChangeProjectStatusRequest, undefined, FormError[]>();

export const removeProjectIssueStatusAsync = createAsyncAction(
  "REMOVE_PROJECT_ISSUE_STATUS_REQUEST",
  "REMOVE_PROJECT_ISSUE_STATUS_SUCCESS",
  "REMOVE_PROJECT_ISSUE_STATUS_FAILED",
)<RemoveProjectStatusRequest, undefined, string>();
