import React from "react";
import { List } from "antd";
import { Paging } from "components/Paging";
import { UsersParams } from "HumanResourceModels";
import { HumanResourceListSkeleton } from "./HumanResourceListSkeleton";
import { HumanResourceListItem } from "./HumanResourceListItem";
import { State } from "../list.reducer";

import "./HumanResourceList.css";

interface Props {
  users: State;
  load: (params: UsersParams) => void;
}

export const HumanResourceList = (props: Props) => {
  const { items, loading, perPage, page, haveMore } = props.users;

  return (
    <div className="content human-resource-list">
      {loading ? (
        <HumanResourceListSkeleton size={perPage} />
      ) : (
        <List
          itemLayout="horizontal"
          dataSource={items}
          renderItem={item => (
            <HumanResourceListItem key={item.id} item={item} />
          )}
        />
      )}
      <Paging
        perPage={perPage}
        page={page}
        haveMore={haveMore}
        onChange={props.load}
      />
    </div>
  );
};
