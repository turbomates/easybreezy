import { createReducer } from "typesafe-actions";

import {
  openProjectCreateForm,
  closeProjectCreateForm,
  createProjectAsync,
  fetchProjectsAsync,
} from "./actions";
import { FormErrorMap, Paging } from "MyTypes";
import { normalizeErrors } from "utils/errors";
import { ProjectList } from "ProjectModels";

export type State = {
  list: Paging<ProjectList> | null;
  isOpenNewForm: boolean;
  loading: boolean;
  newFormErrors: FormErrorMap;
}

const initialSate: State = {
  list: null,
  isOpenNewForm: false,
  loading: false,
  newFormErrors: {}
};

export const reducer = createReducer<State>(initialSate)
  //CREATE
  .handleAction(openProjectCreateForm, (state, action) => ({
    ...state,
    isOpenNewForm: true,
  }))
  .handleAction(closeProjectCreateForm, (state, action) => ({
    ...state,
    isOpenNewForm: false,
    loading: false,
  }))
  .handleAction(createProjectAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(createProjectAsync.success, (state, action) => ({
    ...state,
    isOpenNewForm: false,
    loading: false,
    newFormErrors: {},
  }))
  .handleAction(createProjectAsync.failure, (state, action) => ({
    ...state,
    newFormErrors: normalizeErrors(action.payload),
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
