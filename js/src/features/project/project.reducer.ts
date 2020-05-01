import { createReducer } from "typesafe-actions";
import {
  fetchProjectAsync,
  changeProjectStatusAsync,
  editProjectDescriptionAsync,
  createProjectRoleAsync,
  editProjectRoleAsync,
  removeProjectRoleAsync,
  fetchProjectRoleAsync,
} from "./actions";
import { FormErrorMap } from "MyTypes";
import { normalizeErrors } from "utils/error";
import { Project, RolePermissions } from "ProjectModels";

export interface State {
  project: Project | null;
  loading: boolean;
  errors: FormErrorMap;
  rolePermissions: RolePermissions;
}

const initialSate: State = {
  project: null,
  loading: false,
  errors: {},
  rolePermissions: [],
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
    formErrors: {},
  }))
  .handleAction(createProjectRoleAsync.failure, (state, action) => ({
    ...state,
    formErrors: normalizeErrors(action.payload),
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
    formErrors: {},
  }))
  .handleAction(removeProjectRoleAsync.failure, (state, action) => ({
    ...state,
    formErrors: normalizeErrors(action.payload),
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
    formErrors: {},
  }))
  .handleAction(changeProjectStatusAsync.failure, (state, action) => ({
    ...state,
    formErrors: normalizeErrors(action.payload),
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
    formErrors: {},
  }))
  .handleAction(editProjectDescriptionAsync.failure, (state, action) => ({
    ...state,
    formErrors: normalizeErrors(action.payload),
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
    project: action.payload,
  }))
  .handleAction(fetchProjectAsync.failure, (state, action) => ({
    ...state,
    loading: false,
  }))
  .handleAction(fetchProjectRoleAsync.success, (state, action) => ({
    ...state,
    rolePermissions: action.payload,
  }));
