import { createReducer } from "typesafe-actions";

import { ProjectTeam } from "ProjectModels";
import { FormErrorMap } from "MyTypes";
import {
  closeProjectTeamNewMemberFormAction,
  fetchProjectTeamAsync,
  openProjectTeamNewMemberFormAction,
} from "./actions";
import { normalizeErrors } from "../../utils/error";

export type State = {
  data: ProjectTeam | null;
  loading: boolean;
  errors: FormErrorMap;
  isOpenNewMemberForm: boolean;
}

const initialState: State = {
  data: null,
  loading: false,
  errors: {},
  isOpenNewMemberForm: false,
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
  }));
