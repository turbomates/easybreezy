import { createReducer } from "typesafe-actions";

import { ProjectTeam } from "ProjectModels";
import { FormErrorMap } from "MyTypes";
import {
  addProjectTeamMemberAsync,
  closeProjectTeamNewMemberFormAction,
  fetchProjectTeamAsync,
  openProjectTeamNewMemberFormAction,
} from "./actions";
import { normalizeErrors } from "../../utils/errors";

export type State = {
  data: ProjectTeam | null;
  loading: boolean;
  isOpenNewMemberForm: boolean;
  newMemberFormErrors: FormErrorMap;
  newTeamFormErrors: FormErrorMap;
};

const initialState: State = {
  data: null,
  loading: false,
  isOpenNewMemberForm: false,
  newMemberFormErrors: {},
  newTeamFormErrors: {},
};

export const reducer = createReducer<State>(initialState)
  .handleAction(fetchProjectTeamAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchProjectTeamAsync.success, (state, action) => ({
    ...state,
    data: action.payload,
    loading: false,
  }))
  .handleAction(fetchProjectTeamAsync.failure, (state, action) => ({
    ...state,
    loading: false,
    errors: normalizeErrors(action.payload),
  }))
  .handleAction(openProjectTeamNewMemberFormAction, (state, action) => ({
    ...state,
    isOpenNewMemberForm: true,
  }))
  .handleAction(closeProjectTeamNewMemberFormAction, (state, action) => ({
    ...state,
    isOpenNewMemberForm: false,
  }))
  .handleAction(addProjectTeamMemberAsync.success, (state, action) => ({
    ...state,
    newMemberFormErrors: {},
  }))
  .handleAction(addProjectTeamMemberAsync.failure, (state, action) => ({
    ...state,
    newMemberFormErrors: normalizeErrors(action.payload),
  }));
