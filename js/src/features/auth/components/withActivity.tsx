import React from "react";
import { useSelector } from "react-redux";
import { getActivities } from "features/account/selectors";
import { Result, Button } from "antd";

export const withActivity = (activity: string) => (
  Component: React.ComponentType<any>,
): React.FC => (props: any) => {
  const activities = useSelector(getActivities);

  if (!activities.includes(activity)) {
    return (
      <Result
        status="403"
        title="403"
        subTitle="Sorry, you are not authorized to access this page."
        extra={<Button type="primary">Go Home</Button>}
      />
    );
  }

  return <Component {...props} />;
};
