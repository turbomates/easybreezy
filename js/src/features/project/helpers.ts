import {
  ProjectStatusTypeRequest,
  ProjectStatusTypeResponse,
  ProjectTeamStatusRequest,
  ProjectTeamStatusResponse,
  Role,
} from "ProjectModels";

export function switchProjectStatus(
  status: ProjectStatusTypeResponse,
): ProjectStatusTypeRequest {
  switch (status) {
    case "Active":
      return "activate";
    case "Closed":
      return "close";
    case "Suspended":
      return "suspend";
    default:
      return "activate";
  }
}

export function convertToSlug(name: string) {
  return name.toLocaleLowerCase().replace(/[^\w]+/g, "-");
}

export function getMemberRoleName(roles: Role[], memberRoleId: string) {
  return roles.find((role) => role.id === memberRoleId)?.name;
}

export function switchProjectTeamStatus(
  status: ProjectTeamStatusResponse,
): ProjectTeamStatusRequest {
  switch (status) {
    case "Active":
      return "activate";
    case "Closed":
      return "close";
    default:
      return "activate";
  }
}
