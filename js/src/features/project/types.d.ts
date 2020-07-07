declare module "ProjectModels" {
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
    statuses: ProjectStatuses[];
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

  export type RolePermissions = string[];

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

  export type ProjectStatuses = {
    id: string;
    name: string;
  };

  ///////////////////////////////////////////////////////////

  export type ProjectStatusTypeResponse = "Active" | "Closed" | "Suspended";
  export type ProjectStatusTypeRequest = "activate" | "close" | "suspend";

  export type ProjectResponse = {
    data: Project;
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

  export type ProjectsListQuery = {
    pageSize?: number;
    currentPage?: number;
  };

  export type EditProjectSlugRequest = {
    newSlug: string;
    slug: string;
  };

  export type CreateProjectTeamRequest = {
    name: string;
    project: string;
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

  export type NewProjectTeamMemberRequest = {
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

  export type AddProjectStatusRequest = {
    slug: string;
    name: string;
  };

  export type ChangeProjectStatusRequest = {
    slug: string;
    name: string;
    statusId: string;
  };

  export type RemoveProjectStatusRequest = {
    slug: string;
    statusId: string;
  };
}
