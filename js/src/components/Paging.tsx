import React from "react";
import { Button, Select } from "antd";
import { LeftOutlined, RightOutlined } from "@ant-design/icons";

import "./Paging.scss";

const { Option } = Select;

interface Props {
  onChange: (props: { page: number; perPage: number }) => void;
  perPage: number;
  page: number;
  haveMore: boolean;
}

const DEFAULT_PAGE_SIZE_OPTIONS = [5, 10, 20, 30];

export const Paging = (props: Props) => (
  <div className="paging">
    <span>page {props.page}</span>
    <Select
      value={props.perPage}
      onChange={(value: number) => props.onChange({ page: 1, perPage: value })}
    >
      {DEFAULT_PAGE_SIZE_OPTIONS.map((option) => (
        <Option key={option} value={option}>
          {option} / page
        </Option>
      ))}
    </Select>
    <Button
      disabled={props.page === 1}
      onClick={() =>
        props.onChange({ page: props.page - 1, perPage: props.perPage })
      }
    >
      <LeftOutlined />
      Prev
    </Button>

    <Button
      disabled={props.haveMore === false}
      onClick={() =>
        props.onChange({ page: props.page + 1, perPage: props.perPage })
      }
    >
      Next
      <RightOutlined />
    </Button>
  </div>
);
