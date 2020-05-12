declare module "ProjectModels" {
  export type ProjectStatusTypeResponse = "Active" | "Closed" | "Suspended";
  export type ProjectStatusTypeRequest = "activate" | "close" | "suspend";

  export type ProjectResponse = {
    data: Project;
  };

  export type ProjectList = {
    id: string;
    slug: string;
    name: string;
    status: ProjectStatusTypeResponse;
    description: string;
  };

  export type Project = {
    id: string;
    slug: string;
    name: string;
    status: ProjectStatusTypeResponse;
    description: string;
    roles: Role[];
    teams: Team[];
  };

  type Role = {
    id: string;
    name: string;
    permissions: RolePermissions;
  };

  type Team = {
    id: string;
    name: string;
  };

  export type EditProjectStatusRequest = {
    statusType: ProjectStatusTypeRequest;
    slug: string;
  };

  export type CreateProjectRoleRequest = {
    slug: string;
    roles: {
      name: string;
      permissions: string[];
    };
  };

  export type EditProjectRoleRequest = {
    slug: string;
    roles: Role;
  };

  export type RemoveProjectRoleRequest = {
    slug: string;
    roleId: string;
  };

  export type CreateProjectRequest = {
    name: string;
    description: string;
    slug: string;
  };

  export type EditProjectDescriptionRequest = {
    slug: string;
    description: string;
  };

  export type ProjectsRequest = {
    pageSize?: number;
    currentPage?: number;
  };

  export type RolePermissions = string[];

  export type EditProjectSlugRequest = {
    newSlug: string;
    slug: string;
  };

  export type CreateProjectTeamRequest = {
    name: string;
    project: string;
  };

  export type ProjectTeam = {
    id: string;
    name: string;
    status: ProjectTeamStatusResponse;
    members: ProjectTeamMember[];
  };

  export type ProjectTeamMember = {
    user: string;
    email: string;
    role: string;
    last: string;
    first: string;
  };

  export type EditProjectTeamMemberRoleRequest = {
    newRoleId: string;
    teamId: string;
    memberId: string;
  };

  export type RemoveProjectTeamMemberRequest = {
    teamId: string;
    memberId: string;
  };

  export type AddProjectTeamMemberRequest = {
    role: string;
    user: string;
    teamId: string;
  };

  export type ChangeProjectTeamStatusRequest = {
    teamId: string;
    status: ProjectTeamStatusRequest;
  };

  export type ProjectTeamStatusResponse = "Active" | "Closed";
  export type ProjectTeamStatusRequest = "activate" | "close";
}
