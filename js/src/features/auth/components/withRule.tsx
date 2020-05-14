import React from "react";
import { useSelector } from "react-redux";
import { Result, Button } from "antd";
import { getRules } from "../selectors";
import { getActivities } from "features/account/selectors";

export const withRule = (rule: string) => (
  Component: React.ComponentType<any>,
): React.FC => (props: any) => {
  const rules = useSelector(getRules);
  const activities = useSelector(getActivities);
  const ruleActivities = (rules.status === "loaded" && rules.map[rule]) || [];

  if (rules.status === "loading") return null;

  if (
    rules.status === "error" ||
    !ruleActivities.every((ra) => activities.includes(ra))
  ) {
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
