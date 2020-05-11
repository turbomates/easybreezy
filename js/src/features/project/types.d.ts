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
    permissions: RolePermissions;
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
    slug: string;
  }

  export interface EditProjectDescriptionRequest {
    slug: string;
    description: string;
  }

  export interface ProjectsRequest {
    pageSize?: number;
    currentPage?: number;
  }

  export type RolePermissions = string[];

  export interface EditProjectSlugRequest {
    newSlug: string;
    slug: string;
  }

  export interface CreateProjectTeamRequest {
    name: string;
    project: string;
  }

  export interface ProjectTeam {
    id: string;
    name: string;
    status: ProjectTeamStatusResponse;
    members?: ProjectTeamMember[];
  }

  export interface ProjectTeamMember {
    user: string;
    email: string;
    role: string;
    last: string;
    first: string;
  }

  export interface EditProjectTeamMemberRoleRequest {
    newRoleId: string;
    teamId: string;
    memberId: string;
  }

  export interface RemoveProjectTeamMemberRequest {
    teamId: string;
    memberId: string;
  }

  export interface AddProjectTeamMemberRequest {
    role: string;
    user: string;
    teamId: string;
  }

  export interface ChangeProjectTeamStatusRequest {
    teamId: string;
    status: ProjectTeamStatusRequest;
  }

  export type ProjectTeamStatusResponse = "Active" | "Closed";
  export type ProjectTeamStatusRequest = "activate" | "close";
}
