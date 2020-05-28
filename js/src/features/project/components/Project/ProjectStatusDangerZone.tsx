import React from "react";
import { Button } from "antd";

import { Project, ProjectStatusTypeRequest } from "ProjectModels";

import "./ProjectStatusDangerZone.scss";

type Props = {
  change: (statusType: ProjectStatusTypeRequest) => void;
  project: Project;
};

export const ProjectStatusDangerZone: React.FC<Props> = ({ change, project }) => {
  return (
    <div className="status">
      {project.status !== "Active" && (
        <div className="status__item">
          <span>Start work on the project.</span>
          <Button
            onClick={() => change("activate")}
            className="status__btn success"
          >
            Activate
          </Button>
        </div>
      )}

      {project.status !== "Suspended" && (
        <div className="status__item">
          <span> Pause the project. You can return to work later.</span>
          <Button
            onClick={() => change("suspend")}
            className="status__btn accent"
          >
            Suspend
          </Button>
        </div>
      )}

      {project.status !== "Closed" && (
        <div className="status__item">
          <span>Finish the project.</span>
          <Button
            onClick={() => change("close")}
            className="status__btn warning"
          >
            Close
          </Button>
        </div>
      )}
    </div>
  );
};
