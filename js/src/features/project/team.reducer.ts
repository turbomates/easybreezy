import { createReducer } from "typesafe-actions";

import { ProjectTeam } from "ProjectModels";
import { FormErrorMap } from "MyTypes";
import {
  closeProjectTeamAddMemberFormAction,
  fetchProjectTeamAsync,
  openProjectTeamAddMemberFormAction,
} from "./actions";
import { normalizeErrors } from "../../utils/error";

export type State = {
  data: ProjectTeam | null;
  loading: boolean;
  errors: FormErrorMap;
  isOpenAddMemberForm: boolean;
}

const initialState: State = {
  data: null,
  loading: false,
  errors: {},
  isOpenAddMemberForm: false,
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
  .handleAction(openProjectTeamAddMemberFormAction, (state, action) => ({
    ...state,
    isOpenAddMemberForm: true,
  }))
  .handleAction(closeProjectTeamAddMemberFormAction, (state, action) => ({
    ...state,
    isOpenAddMemberForm: false,
  }));
