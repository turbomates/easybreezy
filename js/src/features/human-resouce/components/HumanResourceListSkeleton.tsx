import React from "react";
import { List, Skeleton } from "antd";

export const HumanResourceListSkeleton = (props: { size: number }) => (
  <List
    itemLayout="horizontal"
    dataSource={Array.from(Array(props.size).keys())}
    size="small"
    renderItem={item => (
      <List.Item key={item}>
        <Skeleton active avatar paragraph={{ rows: 1 }}>
          {item}
        </Skeleton>
      </List.Item>
    )}
  />
);
