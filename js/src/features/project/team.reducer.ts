import { createReducer } from "typesafe-actions";

import { ProjectTeam } from "ProjectModels";
import { FormErrorMap } from "MyTypes";
import { fetchProjectTeamAsync } from "./actions";
import { normalizeErrors } from "../../utils/error";

export interface State {
  team: ProjectTeam | null;
  loading: boolean;
  errors: FormErrorMap;
}

const initialState: State = {
  team: null,
  loading: false,
  errors: {},
};

export const reducer = createReducer<State>(initialState)
  .handleAction(fetchProjectTeamAsync.request, (state, action) => ({
    ...state,
    loading: true,
  }))
  .handleAction(fetchProjectTeamAsync.success, (state, action) => ({
    ...state,
    team: action.payload,
    loading: false,
  }))
  .handleAction(fetchProjectTeamAsync.failure, (state, action) => ({
    ...state,
    loading: false,
    errors: normalizeErrors(action.payload),
  }));
