import React from "react";
import { Button } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import parseDate from "date-fns/parse";

import { UserVacation } from "HumanResourceModels";

import DatePicker from "components/antd/DatePicker";
import { DEFAULT_DATE_FORMAT } from "../../../constants";

const { RangePicker } = DatePicker;

interface Props {
  vacations: UserVacation[];
  canEdit: boolean;
}

export const Vacations: React.FC<Props> = ({ vacations, canEdit }) => (
  <>
    {vacations.map((vacation, index) => (
      <span key={index} className="vacations__item">
        <RangePicker
          defaultValue={[
            parseDate(vacation.from, DEFAULT_DATE_FORMAT, new Date()),
            parseDate(vacation.to, DEFAULT_DATE_FORMAT, new Date()),
          ]}
          format="DD/MM/YYYY"
        />
        <span className="vacations__item-description">
          {vacation.description}
        </span>
      </span>
    ))}
    {canEdit && (
      <Button onClick={() => {}}>
        <PlusOutlined /> Add Vacation
      </Button>
    )}
  </>
);
