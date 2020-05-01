import React from "react";
import { Button } from "antd";

interface Props {
  description?: string;
  openProjectDescriptionForm: (open: boolean) => void;
}

export const ProjectDescription: React.FC<Props> = ({
  description,
  openProjectDescriptionForm,
}) => {
  return (
    <div>
      <p>{description}</p>
      <Button type="primary" onClick={()=> openProjectDescriptionForm(true)}>
        Edit
      </Button>
    </div>
  );
};
