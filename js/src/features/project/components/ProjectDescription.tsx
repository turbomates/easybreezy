import React from "react";
import { Button } from "antd";

interface Props {
  description?: string;
  openProjectDescriptionForm: () => void;
}

export const ProjectDescription: React.FC<Props> = ({
  description,
  openProjectDescriptionForm,
}) => {
  return (
    <div>
      <p>{description}</p>
      <Button type="primary" onClick={openProjectDescriptionForm}>
        Edit
      </Button>
    </div>
  );
};
