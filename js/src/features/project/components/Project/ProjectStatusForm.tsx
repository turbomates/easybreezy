import React from "react";
import { Button } from "antd";

import {
  Project,
  ProjectStatusTypeRequest
} from "ProjectModels"

import "./ProjectStatusForm.scss";

type Props = {
  change: (statusType: ProjectStatusTypeRequest) => void;
  project: Project;
  loading: boolean;
};

export const ProjectStatusForm: React.FC<Props> = ({ change, project }) => {
  return (
    <div className="status">
      {project.status !== "Active" && (
        <div className="status__item">
          <span>Start work on the project.</span>
          <Button
            value="Active"
            onClick={() => change("activate")}
            className="status__btn active"
          >
            Activate
          </Button>
        </div>
      )}

      {project.status !== "Suspended" && (
        <div className="status__item">
          <span> Pause the project. You can return to work later.</span>
          <Button
            value="Suspended"
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
            value="Closed"
            onClick={() => change("close")}
            className="status__btn status__btn-danger danger"
          >
            Close
          </Button>
        </div>
      )}
    </div>
  );
};
