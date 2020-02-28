import React from "react";
import { Card, DatePicker, Button } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { UserVacation } from "HumanResourceModels";
import moment from "moment";

const { RangePicker } = DatePicker;

interface Props {
  vacations: UserVacation[];
  loading: boolean;
  canEdit: boolean;
}

export const VacationsCard = (props: Props) => {
  const { loading, vacations, canEdit } = props;

  return (
    <Card
      title="Vacations"
      bordered={false}
      className="human-resource-details__card vacations"
      loading={loading}
    >
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
    </Card>
  );
};
