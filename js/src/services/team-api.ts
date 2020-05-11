import { api } from "./api";
import { FormFailure, Success } from "MyTypes";
import {
  AddProjectTeamMemberRequest,
  ChangeProjectTeamStatusRequest,
  CreateProjectTeamRequest,
  EditProjectTeamMemberRoleRequest,
  ProjectTeam,
  RemoveProjectTeamMemberRequest,
} from "ProjectModels";

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

export const editMemberRole = ({
  memberId,
  newRoleId,
  teamId,
}: EditProjectTeamMemberRoleRequest) =>
  api
    .post(`/teams/${teamId}/members/${memberId}/change-role`, { newRoleId })
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));

export const removeMember = ({
  teamId,
  memberId,
}: RemoveProjectTeamMemberRequest) =>
  api
    .post(`/teams/${teamId}/members/${memberId}/remove`, {})
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));

export const addMember = ({
  role,
  user,
  teamId,
}: AddProjectTeamMemberRequest) =>
  api
    .post(`/teams/${teamId}/members/add`, {
      role,
      user,
    })
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));

export const changeStatus = ({
  teamId,
  status,
}: ChangeProjectTeamStatusRequest) =>
  api
    .post(`/teams/${teamId}/${status}`, {})
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));
