import {
  ProjectStatusTypeResponse,
  Role,
} from "ProjectModels";

export function convertToSlug(name: string) {
  return name.toLocaleLowerCase().replace(/[^\w]+/g, "-");
}

export function getMemberRoleName(roles: Role[], memberRoleId: string) {
  return roles.find((role) => role.id === memberRoleId)?.name;
}

export function chooseColor(status: ProjectStatusTypeResponse) {
  switch (status) {
    case "Active":
      return "green";
    case "Closed":
      return "red";
    case "Suspended":
      return "orange";
  }
}
