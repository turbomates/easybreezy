import {
  ProjectList,
  ProjectResponse,
  EditProjectStatusRequest,
  CreateProjectRequest,
  EditProjectDescriptionRequest,
  EditProjectRoleRequest,
  RemoveProjectRoleRequest,
  CreateProjectRoleRequest,
  ProjectsListQuery,
  RolePermissions,
  EditProjectSlugRequest,
} from "ProjectModels";
import { Failure, FormFailure, Paging, Success } from "MyTypes";
import { api } from "./api";

export const fetchProjects = (params: ProjectsListQuery) => {
  return api
    .get<Paging<ProjectList>>(`/projects`, { params })
    .then<Success<Paging<ProjectList>>>((resp) => ({
      success: true,
      data: resp.data,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));
};

export const fetchProject = (slug: string) => {
  return api
    .get<ProjectResponse>(`/projects/${slug}`)
    .then<Success<ProjectResponse>>((resp) => ({
      success: true,
      data: resp.data,
    }))
    .catch<Failure>(() => ({ success: false, reason: "Error" }));
};

export const createProject = (body: CreateProjectRequest) => {
  return api
    .post(`/projects`, body)
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));
};

export const createRole = ({ slug, roles }: CreateProjectRoleRequest) => {
  const body = {
    name: roles.name,
    permissions: roles.permissions,
  };

  return api
    .post(`/projects/${slug}/roles/add`, body)
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));
};

export const editRole = ({ slug, roles }: EditProjectRoleRequest) => {
  const body = {
    name: roles.name,
    permissions: roles.permissions,
  };

  return api
    .post(`/projects/${slug}/roles/${roles.id}/change`, body)
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));
};

export const removeRole = ({ slug, roleId }: RemoveProjectRoleRequest) => {
  return api
    .post(`/projects/${slug}/roles/${roleId}/remove`, {})
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));
};

export const editDescription = ({
  slug,
  description,
}: EditProjectDescriptionRequest) => {
  return api
    .post(`/projects/${slug}/write-description`, { description })
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));
};

export const editStatus = ({ slug, statusType }: EditProjectStatusRequest) => {
  return api
    .post(`/projects/${slug}/${statusType}`, {})
    .then<Success<null>>(() => ({ success: true, data: null }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));
};

export const fetchRolePermissions = () => {
  return api
    .get("/projects/permissions")
    .then<Success<RolePermissions>>((resp) => ({
      success: true,
      data: resp.data.data,
    }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));
};

export const editSlug = ({ slug, newSlug }: EditProjectSlugRequest) => {
  return api
    .post(`/projects/${slug}/change-slug`, { slug: newSlug })
    .then<Success<null>>(() => ({
      success: true,
      data: null,
    }))
    .catch<FormFailure>((resp) => ({
      success: false,
      errors: resp?.response?.data?.errors || [],
    }));
};
