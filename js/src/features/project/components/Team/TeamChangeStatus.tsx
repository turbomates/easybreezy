import React from "react";
import { Button } from "antd";

import "./TeamChangeStatus.scss";

type Props = {
  onChange: () => void;
  description: string;
  btnText: string;
  type: "danger" | "primary";
};

export const TeamChangeStatus: React.FC<Props> = ({
  onChange,
  type,
  description,
  btnText,
}) => (
  <div className="team-status">
    <span>{description}</span>
    <Button type={type} onClick={onChange}>
      {btnText}
    </Button>
  </div>
);
