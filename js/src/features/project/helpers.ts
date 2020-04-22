import {
  ProjectStatusTypeRequest,
  ProjectStatusTypeResponse,
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
