import { createAction, createAsyncAction } from "typesafe-actions"

import { FormError, Paging } from "MyTypes"
import {
  ProjectList,
  EditProjectStatusRequest,
  CreateProjectRequest,
  EditProjectDescriptionRequest,
  EditProjectRoleRequest,
  RemoveProjectRoleRequest,
  CreateProjectRoleRequest,
  Project
} from "ProjectModels"

//PROJECT
export const fetchProjectAsync = createAsyncAction(
  "FETCH_PROJECT_REQUEST",
  "FETCH_PROJECT_SUCCESS",
  "FETCH_PROJECT_FAILURE",
)<string, Project, any>()

// PROJECTS
export const fetchProjectsAsync = createAsyncAction(
  "FETCH_PROJECTS_REQUEST",
  "FETCH_PROJECTS_SUCCESS",
  "FETCH_PROJECTS_FAILURE",
)<undefined, Paging<ProjectList>, string>()

//CREATE PROJECT
export const openProjectCreateForm = createAction(
  "OPEN_PROJECT_CREATE_FORM"
)()

export const closeProjectCreateForm = createAction(
  "CLOSE_PROJECT_CREATE_FORM"
)()

export const createProjectAsync = createAsyncAction(
  "CREATE_PROJECT_REQUEST",
  "CREATE_PROJECT_SUCCESS",
  "CREATE_PROJECT_FAILURE",
)<CreateProjectRequest, undefined, FormError[]>()

// ROLE
export const openProjectRoleForm = createAction(
  "OPEN_PROJECT_ROLE_FORM"
)()

export const closeProjectRoleForm = createAction(
  "CLOSE_PROJECT_ROLE_FORM"
)()

export const createProjectRoleAsync = createAsyncAction(
  "CREATE_PROJECT_ROLE_REQUEST",
  "CREATE_PROJECT_ROLE_SUCCESS",
  "CREATE_PROJECT_ROLE_FAILURE",
)<CreateProjectRoleRequest, undefined, FormError[]>()

export const editProjectRoleAsync = createAsyncAction(
  "EDIT_PROJECT_ROLE_REQUEST",
  "EDIT_PROJECT_ROLE_SUCCESS",
  "EDIT_PROJECT_ROLE_FAILURE",
)<EditProjectRoleRequest, undefined, FormError[]>()

export const removeProjectRoleAsync = createAsyncAction(
  "REMOVE_PROJECT_ROLE_REQUEST",
  "REMOVE_PROJECT_ROLE_SUCCESS",
  "REMOVE_PROJECT_ROLE_FAILURE",
)<RemoveProjectRoleRequest, undefined, FormError[]>()

// STATUS
export const openProjectStatusForm = createAction(
  "OPEN_PROJECT_STATUS_FORM"
)()

export const closeProjectStatusForm = createAction(
  "CLOSE_PROJECT_STATUS_FORM"
)()

export const changeProjectStatusAsync = createAsyncAction(
  "CHANGE_PROJECT_STATUS_REQUEST",
  "CHANGE_PROJECT_STATUS_SUCCESS",
  "CHANGE_PROJECT_STATUS_FAILURE",
)<EditProjectStatusRequest, undefined, FormError[]>()

// DESCRIPTION
export const openProjectDescriptionForm = createAction(
  "OPEN_PROJECT_DESCRIPTION_FORM"
)()

export const closeProjectDescriptionForm = createAction(
  "CLOSE_PROJECT_DESCRIPTION_FORM"
)()

export const editProjectDescriptionAsync = createAsyncAction(
  "EDIT_PROJECT_DESCRIPTION_REQUEST",
  "EDIT_PROJECT_DESCRIPTION_SUCCESS",
  "EDIT_PROJECT_DESCRIPTION_FAILURE",
)<EditProjectDescriptionRequest, undefined, FormError[]>()

