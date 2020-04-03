import React, { useEffect } from "react";
import { Form, Input, Button } from "antd";
import { useDispatch, useSelector } from "react-redux";

import { signInAsync } from "../actions";
import { signInFailedReason } from "../selectors";

export const LoginForm: React.FC = () => {
  const [form] = Form.useForm();
  const dispatch = useDispatch();
  const error = useSelector(signInFailedReason);

  useEffect(() => {
    if (error) {
      form.setFields([
        { name: "email", errors: [error] },
        { name: "password", errors: [error] },
      ]);
    }
  }, [error, form]);

  const onFinish = (values: any) => {
    console.log("onFinish:", values);
    dispatch(signInAsync.request(values));
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log("onFinishFailed:", errorInfo);
  };

  return (
    <Form
      name="login"
      form={form}
      labelCol={{ span: 8 }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
    >
      <Form.Item
        label="Email"
        name="email"
        rules={[{ required: true, message: "Please input your email!" }]}
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Password"
        name="password"
        rules={[{ required: true, message: "Please input your password!" }]}
      >
        <Input.Password />
      </Form.Item>

      <Form.Item>
        <Button type="primary" htmlType="submit">
          Submit
        </Button>
      </Form.Item>
    </Form>
  );
};
