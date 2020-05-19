import React from "react";
import { Button } from "antd";

type Props = {
  description?: string;
  onButtonClick: () => void;
}

export const ProjectDescription: React.FC<Props> = ({
  description,
  onButtonClick,
}) => {
  return (
    <div>
      <p>{description}</p>
      <Button type="primary" onClick={onButtonClick}>
        Edit
      </Button>
    </div>
  );
};
