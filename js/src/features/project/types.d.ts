declare module "ProjectModels" {
  export type ProjectStatusTypeResponse = "Active" | "Closed" | "Suspended";
  export type ProjectStatusTypeRequest = "activate" | "close" | "suspend";

  export interface ProjectResponse {
    data: Project;
  }

  export interface ProjectList {
    id: string;
    slug: string;
    name: string;
    status: ProjectStatusTypeResponse;
    description: string;
  }

  export interface Project {
    id: string;
    slug: string;
    name: string;
    status: ProjectStatusTypeResponse;
    description: string;
    roles: Role[];
    teams: Team[];
  }

  interface Role {
    id: string;
    name: string;
    permissions: string[];
  }

  interface Team {
    id: string;
    name: string;
  }

  export interface EditProjectStatusRequest {
    statusType: ProjectStatusTypeRequest;
    slug: string;
  }

  export interface CreateProjectRoleRequest {
    slug: string;
    roles: {
      name: string;
      permissions: string[];
    };
  }

  export interface EditProjectRoleRequest {
    slug: string;
    roles: Role;
  }

  export interface RemoveProjectRoleRequest {
    slug: string;
    roleId: string;
  }

  export interface CreateProjectRequest {
    name: string;
    description: string;
  }

  export interface EditProjectDescriptionRequest {
    slug: string;
    description: string;
  }

  export interface ProjectsRequest {
    pageSize?: number;
    currentPage?: number;
  }
}
