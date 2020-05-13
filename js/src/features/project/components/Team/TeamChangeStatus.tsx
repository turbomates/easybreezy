import React from "react";
import { Button } from "antd";

import "./TeamChangeStatus.scss";

type Props = {
  onButtonClick: () => void;
  description: string;
  buttonText: string;
  type: "danger" | "primary";
};

export const TeamChangeStatus: React.FC<Props> = ({
  onButtonClick,
  type,
  description,
  buttonText,
}) => (
  <div className="team-status">
    <span>{description}</span>
    <Button type={type} onClick={onButtonClick}>
      {buttonText}
    </Button>
  </div>
);
