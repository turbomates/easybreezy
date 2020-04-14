declare module "ProjectModels" {
  export type StatusType = 'activate' | 'close' | 'suspend' | string

  export interface ProjectResponse {
    data: Project
  }

  export interface ProjectList {
    id: string
    slug: string
    name: string
    status: StatusType
    description: string
  }

  export interface Project {
    id: string
    slug: string
    name: string
    status: StatusType
    description: string
    roles: Role[]
    teams: Team[]
  }

  interface Role {
    id: string
    name: string
    permissions: string[]
  }

  interface Team {
    id: string
    name: string
  }

  export interface EditProjectStatusRequest {
    statusType: StatusType
    name: string
  }

  export interface CreateProjectRoleRequest {
    slug: string,
    roles: Role
  }

  export interface EditProjectRoleRequest {
    slug: string,
    roles: Role
  }

  export interface RemoveProjectRoleRequest {
    slug: string,
    roleId: string
  }

  export interface CreateProjectRequest {
    name: string
    description: string
  }

  export interface EditProjectDescriptionRequest {
    slug: string
    description: string
  }
}
