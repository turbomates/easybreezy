import React from "react";
import { Button } from "antd";
import { LeftOutlined, RightOutlined } from "@ant-design/icons";

type Props = {
  moveBack: () => void;
  moveForward: () => void;
  today: () => void;
};

export const HumanResourceCalendarControls: React.FC<Props> = ({
  moveBack,
  moveForward,
  today,
}) => {
  return (
    <div>
      <Button type="ghost" onClick={moveBack}>
        <LeftOutlined style={{ userSelect: "none" }} />
      </Button>
      <Button type="ghost" onClick={today} className="today-btn">
        Today
      </Button>
      <Button type="ghost" onClick={moveForward}>
        <RightOutlined style={{ userSelect: "none" }} />
      </Button>
    </div>
  );
};
