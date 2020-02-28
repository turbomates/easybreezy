import React from "react";
import { Card } from "antd";

interface Props {
  sickDays: any;
  loading: boolean;
  canEdit: boolean;
}

export const SickDaysCart = (props: Props) => {
  const { loading } = props;

  return (
    <Card
      title="Sick Days"
      bordered={false}
      className="human-resource-details__card"
      loading={loading}
    >
      Card content
    </Card>
  );
};
