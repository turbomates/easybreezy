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

  useFormServerErrors(form, errors, ["startedAt", "endedAt", "locationId"]);

  const onFinish = useCallback(
    (values: any) => {
      const startedAt = values.startedAt.format(DATE_FORMAT);
      const endedAt = values.endedAt.format(DATE_FORMAT);

      edit({
        form: {
          startedAt,
          endedAt,
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
      endedAt: moment(employeeLocation.endedAt),
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
      >
        <DatePicker />
      </Form.Item>

      <Form.Item
        label="Ended at"
        name="endedAt"
        rules={[{ required: true, message: "Please input Ended at!" }]}
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
          filterOption={(input, option) =>
            option!.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
          }
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
