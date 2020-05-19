import { createReducer } from "typesafe-actions";

import {
  openProjectCreateForm,
  closeProjectCreateForm,
  createProjectAsync,
  fetchProjectsAsync,
} from "./actions";
import { FormErrorMap, Paging } from "MyTypes";
import { normalizeErrors } from "utils/error";
import { ProjectList } from "ProjectModels";

export type State = {
  list: Paging<ProjectList> | null;
  isOpenCreateForm: boolean;
  loading: boolean;
  createFormErrors: FormErrorMap;
}

const initialSate: State = {
  list: null,
  isOpenCreateForm: false,
  loading: false,
  createFormErrors: {}
};

export const reducer = createReducer<State>(initialSate)
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
    createFormErrors: {},
  }))
  .handleAction(createProjectAsync.failure, (state, action) => ({
    ...state,
    createFormErrors: normalizeErrors(action.payload),
    loading: false,
  }))
  //PROJECTS
  .handleAction(fetchProjectsAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchProjectsAsync.success, (state, action) => ({
    ...state,
    loading: false,
    list: action.payload,
  }))
  .handleAction(fetchProjectsAsync.failure, (state, action) => ({
    ...state,
    loading: false,
  }));
