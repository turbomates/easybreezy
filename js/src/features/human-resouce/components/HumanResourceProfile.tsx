import React from "react";
import { State } from "../profile.reducer";
import { Skeleton } from "antd";

interface Props {
  profile: State;
}

export const Profile = (props: Props) => {
  const { data, loading } = props.profile;

  if (loading || !data)
    return <Skeleton active loading={loading} paragraph={{ rows: 9 }} />;

  return (
    <ul>
      <li>EPICS</li>
      <li>SICK DAYS</li>
      <li>VACATIONS</li>
      <li>FEATURES</li>
      <li>DAYONS</li>
      <li>FIRST NAME: {data?.firstName}</li>
      <li>LAST NAME: {data?.lastName}</li>
      <li>ROLE: Member</li>
      <li>USERNAME: {data?.username}</li>
      <li>Messengers</li>
    </ul>
  );
};
