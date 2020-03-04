import React from "react";
import { DatePicker, Button } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { UserVacation } from "HumanResourceModels";
import moment from "moment";

const { RangePicker } = DatePicker;

interface Props {
  vacations: UserVacation[];
  canEdit: boolean;
}

export const Vacations = (props: Props) => {
  const { vacations, canEdit } = props;

  return (
    <>
      {vacations.map((vacation, index) => (
        <span key={index} className="vacations__item">
          <RangePicker
            defaultValue={[moment(vacation.from), moment(vacation.to)]}
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
};
