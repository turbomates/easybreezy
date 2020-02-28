import React from "react";
import { Card } from "antd";
import { UserDetails } from "HumanResourceModels";

interface Props {
  user: UserDetails | null;
  loading: boolean;
  canEdit: boolean;
}

export const PositionsCard = (props: Props) => {
  const { loading } = props;

  return (
    <Card
      title="Positions"
      className="human-resource-details__card user-positions"
      loading={loading}
    ></Card>
  );
};
