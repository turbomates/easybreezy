import { createReducer } from "typesafe-actions";
import {
  fetchProjectAsync,
  changeProjectStatusAsync,
  editProjectDescriptionAsync,
  createProjectRoleAsync,
  editProjectRoleAsync,
  removeProjectRoleAsync,
  fetchProjectRoleAsync,
  openProjectTeamCreateFormAction,
  closeProjectTeamCreateFormAction,
  editProjectSlugAsync,
  createProjectTeamAsync,
} from "./actions";
import { FormErrorMap } from "MyTypes";
import { normalizeErrors } from "utils/errors";
import { Project, RolePermissions } from "ProjectModels";

export type State = {
  data: Project | null;
  loading: boolean;
  rolePermissions: RolePermissions;
  isOpenNewTeamForm: boolean;
  descriptionFormErrors: FormErrorMap;
  slugFormErrors: FormErrorMap;
  newTeamFormErrors: FormErrorMap;
};

const initialSate: State = {
  data: null,
  loading: false,
  rolePermissions: [],
  isOpenNewTeamForm: false,
  descriptionFormErrors: {},
  slugFormErrors: {},
  newTeamFormErrors: {},
};

export const reducer = createReducer<State>(initialSate)
  // ROLE
  .handleAction(createProjectRoleAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(createProjectRoleAsync.success, (state, action) => ({
    ...state,
    loading: false,
  }))
  .handleAction(createProjectRoleAsync.failure, (state, action) => ({
    ...state,
    loading: false,
  }))
  .handleAction(editProjectRoleAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(editProjectRoleAsync.success, (state, action) => ({
    ...state,
    loading: false,
    formErrors: {},
  }))
  .handleAction(editProjectRoleAsync.failure, (state, action) => ({
    ...state,
    formErrors: normalizeErrors(action.payload),
    loading: false,
  }))
  .handleAction(removeProjectRoleAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(removeProjectRoleAsync.success, (state, action) => ({
    ...state,
    loading: false,
  }))
  .handleAction(removeProjectRoleAsync.failure, (state, action) => ({
    ...state,
    loading: false,
  }))
  // STATUS
  .handleAction(changeProjectStatusAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(changeProjectStatusAsync.success, (state, action) => ({
    ...state,
    loading: false,
  }))
  .handleAction(changeProjectStatusAsync.failure, (state, action) => ({
    ...state,
    loading: false,
  }))
  // DESCRIPTION
  .handleAction(editProjectDescriptionAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(editProjectDescriptionAsync.success, (state, action) => ({
    ...state,
    loading: false,
    descriptionFormErrors: {},
  }))
  .handleAction(editProjectDescriptionAsync.failure, (state, action) => ({
    ...state,
    descriptionFormErrors: normalizeErrors(action.payload),
    loading: false,
  }))
  //PROJECT
  .handleAction(fetchProjectAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchProjectAsync.success, (state, action) => ({
    ...state,
    loading: false,
    data: action.payload,
  }))
  .handleAction(fetchProjectAsync.failure, (state, action) => ({
    ...state,
    loading: false,
  }))
  .handleAction(fetchProjectRoleAsync.success, (state, action) => ({
    ...state,
    rolePermissions: action.payload,
  }))
  .handleAction(openProjectTeamCreateFormAction, (state, action) => ({
    ...state,
    isOpenNewTeamForm: true,
  }))
  .handleAction(closeProjectTeamCreateFormAction, (state, action) => ({
    ...state,
    isOpenNewTeamForm: false,
  }))
  // SLUG
  .handleAction(editProjectSlugAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(editProjectSlugAsync.success, (state, action) => ({
    ...state,
    loading: false,
    slugFormErrors: {},
  }))
  .handleAction(editProjectSlugAsync.failure, (state, action) => ({
    ...state,
    slugFormErrors: normalizeErrors(action.payload),
    loading: false,
  }))
  // TEAM
  .handleAction(createProjectTeamAsync.success, (state, action) => ({
    ...state,
    newTeamFormErrors: {},
  }))
  .handleAction(createProjectTeamAsync.failure, (state, action) => ({
    ...state,
    newTeamFormErrors: normalizeErrors(action.payload),
  }));
