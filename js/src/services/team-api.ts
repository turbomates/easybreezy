import { api } from "./api";
import { FormFailure, Success } from "MyTypes";
import { CreateProjectTeamRequest, ProjectTeam } from "ProjectModels";

export const createTeam = (body: CreateProjectTeamRequest) =>
  api
    .post(`/teams/add`, body)
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));

export const fetchTeam = (id: string) =>
  api
    .get(`/teams/${id}`)
    .then<Success<{ data: ProjectTeam }>>((resp) => ({
      success: true,
      data: resp.data,
    }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));
