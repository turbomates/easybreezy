import { createReducer } from "typesafe-actions"
import {
  fetchProjectAsync,

  openProjectCreateForm,
  closeProjectCreateForm,
  createProjectAsync,

  openProjectStatusForm,
  closeProjectStatusForm,
  changeProjectStatusAsync,

  openProjectDescriptionForm,
  closeProjectDescriptionForm,
  editProjectDescriptionAsync,

  openProjectRoleForm,
  closeProjectRoleForm,
  createProjectRoleAsync,
  editProjectRoleAsync,
  removeProjectRoleAsync
} from "./actions"
import { FormErrorMap } from "MyTypes"
import { normalizeErrors } from "utils/error"
import { Project } from "ProjectModels"

export interface State {
  project: Project | null
  isOpenCreateForm: boolean
  isOpenRoleForm: boolean
  isOpenStatusForm: boolean
  isOpenDescriptionForm: boolean
  loading: boolean
  errors: FormErrorMap
}

const initialSate: State = {
  project: null,
  loading: false,
  isOpenCreateForm: false,
  isOpenRoleForm: false,
  isOpenStatusForm: false,
  isOpenDescriptionForm: false,
  errors: {}
}

export const projectReducer = createReducer<State>(initialSate)
  //CREATE
  .handleAction(openProjectCreateForm, (state, action) => ({
    ...state,
    isOpenCreateForm: true,
  }))
  .handleAction(closeProjectCreateForm, (state, action) => ({
    ...state,
    isOpenCreateForm: false,
    loading: false,
  }))
  .handleAction(createProjectAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(createProjectAsync.success, (state, action) => ({
    ...state,
    isOpenCreateForm: false,
    loading: false,
    formErrors: {},
  }))
  .handleAction(createProjectAsync.failure, (state, action) => ({
    ...state,
    formErrors: normalizeErrors(action.payload),
    loading: false,
  }))
  // ROLE
  .handleAction(openProjectRoleForm, (state, action) => ({
    ...state,
    isOpenRoleForm: true,
  }))
  .handleAction(closeProjectRoleForm, (state, action) => ({
    ...state,
    isOpenRoleForm: false,
    loading: false,
    project: null,
  }))
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
  .handleAction(openProjectStatusForm, (state, action) => ({
    ...state,
    isOpenStatusForm: true,
  }))
  .handleAction(closeProjectStatusForm, (state, action) => ({
    ...state,
    isOpenStatusForm: false,
    loading: false,
    project: null,
    formErrors: {},
  }))
  .handleAction(changeProjectStatusAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(changeProjectStatusAsync.success, (state, action) => ({
    ...state,
    isOpenStatusForm: false,
    loading: false,
    project: null,
    formErrors: {},
  }))
  .handleAction(changeProjectStatusAsync.failure, (state, action) => ({
    ...state,
    formErrors: normalizeErrors(action.payload),
    loading: false,
  }))
  // DESCRIPTION
  .handleAction(openProjectDescriptionForm, (state, action) => ({
    ...state,
    isOpenDescriptionForm: true,
  }))
  .handleAction(closeProjectDescriptionForm, (state, action) => ({
    ...state,
    isOpenDescriptionForm: false,
    loading: false,
    project: null,
    formErrors: {},
  }))
  .handleAction(editProjectDescriptionAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(editProjectDescriptionAsync.success, (state, action) => ({
    ...state,
    isOpenDescriptionForm: false,
    loading: false,
    project: null,
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
