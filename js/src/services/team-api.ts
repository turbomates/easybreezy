import { api } from "./api";
import { FormFailure, Success } from "MyTypes";
import { CreateProjectTeamRequest } from "ProjectModels";

export const createTeam = (body: CreateProjectTeamRequest) =>
  api
    .post(`/teams/add`, body)
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));
