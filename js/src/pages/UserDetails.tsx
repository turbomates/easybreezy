import React, { useEffect } from "react";
import { useParams } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { userDetails, profile } from "../features/human-resouce/selectors";
import { fetchUserDetailsAsync } from "../features/human-resouce/actions";
import { HumanResourceDetails } from "features/human-resouce/components/HumanResourceDetails";

export const UserDetailsPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const { user, loading } = useSelector(userDetails);
  const { data } = useSelector(profile);
  const dispatch = useDispatch();
  const userProp = user && user.id.toString() === id ? user : null;
  const load = (id: string) => dispatch(fetchUserDetailsAsync.request(id));

  useEffect(() => {
    load(id);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  return (
    <HumanResourceDetails user={userProp} loading={loading} account={data} />
  );
};
