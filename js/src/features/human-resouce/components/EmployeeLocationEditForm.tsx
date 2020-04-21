import React, { useCallback, useMemo } from "react";
import moment from "moment";
import { Form, Button, Select, DatePicker } from "antd";
import {
  Location,
  EditEmployeeLocationData,
  EmployeeLocation,
} from "LocationModels";
import { FormErrorMap } from "MyTypes";
import { useFormServerErrors } from "hooks/useFormServerErrors";
import { filterOptions } from "utils/filterOptions";

const { Option } = Select;

interface Props {
  locations: Location[];
  employeeLocation: EmployeeLocation;
  errors: FormErrorMap;
  edit: (data: EditEmployeeLocationData) => void;
}

const DATE_FORMAT = "YYYY-MM-DD";

export const EmployeeLocationEditForm: React.FC<Props> = ({
  locations,
  employeeLocation,
  errors,
  edit,
}) => {
  const [form] = Form.useForm();

  useFormServerErrors(form, errors, ["startedAt", "locationId"]);

  const onFinish = useCallback(
    (values: any) => {
      const startedAt = values.startedAt.format(DATE_FORMAT);

      edit({
        form: {
          startedAt,
          locationId: values.locationId,
        },
        employeeLocationId: employeeLocation.id,
      });
    },
    [employeeLocation, edit],
  );

  const onFinishFailed = useCallback((errorInfo: any) => {
    console.log("onFinishFailed:", errorInfo);
  }, []);

  const initialFormValues = useMemo(
    () => ({
      startedAt: moment(employeeLocation.startedAt),
      locationId: employeeLocation.location.id,
    }),
    [employeeLocation],
  );

  return (
    <Form
      form={form}
      labelCol={{ span: 8 }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      initialValues={initialFormValues}
    >
      <Form.Item
        label="Started at"
        name="startedAt"
        rules={[{ required: true, message: "Please input Started at!" }]}
        normalize={(value) => {
          console.log(value);
          return value.format(DATE_FORMAT);
        }}
      >
        <DatePicker />
      </Form.Item>

      <Form.Item
        label="Location"
        name="locationId"
        rules={[{ required: true, message: "Please input location!" }]}
      >
        <Select
          showSearch
          placeholder="Select a location"
          optionFilterProp="children"
          filterOption={filterOptions}
        >
          {locations.map((item) => (
            <Option key={item.id} value={item.id}>
              {item.name}
            </Option>
          ))}
        </Select>
      </Form.Item>

      <Form.Item>
        <Button type="primary" htmlType="submit">
          Edit
        </Button>
      </Form.Item>
    </Form>
  );
};
