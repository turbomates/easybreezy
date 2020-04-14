import React, { useCallback, useMemo } from "react"
import { Button, Col, Form, Input, Select } from "antd"
import { PlusOutlined } from "@ant-design/icons"

import { useFormServerErrors } from "hooks/useFormServerErrors"
import { FormErrorMap } from "MyTypes"
import { Project, EditProjectRoleRequest, RemoveProjectRoleRequest } from "ProjectModels"

interface Props {
  project: Project
  create: (form: EditProjectRoleRequest) => void
  edit: (form: EditProjectRoleRequest) => void
  remove: (form: RemoveProjectRoleRequest) => void
  errors: FormErrorMap
  loading: boolean
}

export const ProjectRolesForm: React.FC<Props> = ({errors, create, edit, remove, project, loading}) => {
  const [form] = Form.useForm()

  const initialValues = useMemo(() => ({
    roles: project.roles
  }), [project.roles.length])

  useFormServerErrors(form, errors, [
    "name",
    "description",
  ])

  const onFinishFailed = useCallback((errorInfo: any) => {
    console.log("onFinishFailed:", errorInfo)
  }, [])

  function getSelectionChildren(index: number) {
    return !!project.roles[index] && project.roles[index].permissions.length > 0 ? project.roles[index].permissions : []
  }

  function editRole(index: number) {
    const fields = form.getFieldsValue()
    edit({
      roles: fields.roles[index],
      slug: project.slug,
    })
  }

  function removeRole(index: number) {
    const fields = form.getFieldsValue()
    remove({
      roleId: fields.roles[index].id,
      slug: project.slug,
    })
  }

  function createRole(index: number) {
    const fields = form.getFieldsValue()
    create({
      roles: fields.roles[index],
      slug: project.slug,
    })
  }

  return (
    <Form
      form={form}
      labelCol={{span: 6}}
      onFinishFailed={onFinishFailed}
      initialValues={initialValues}
    >
      <Form.List name="roles">
        {(fields, {add, remove}) => {
          return (
            <div>
              {fields.map((field, index) => {
                return (
                  <React.Fragment key={index}>
                    <Form.Item
                      name={[field.name, "name"]}
                      label={["Name"]}
                      rules={[{required: true, message: "Please input Name!"}]}
                    >
                      <Input placeholder="last name"/>
                    </Form.Item>
                    <Form.Item
                      name={[field.name, "permissions"]}
                      label={["Permissions"]}

                    >
                      <Select mode="tags" style={{width: '100%'}} tokenSeparators={[',']}>
                        {getSelectionChildren(index)}
                      </Select>
                    </Form.Item>

                    {!!project.roles[index] ?
                      <FormRoleControlButtons
                        loading={loading}
                        action={() => editRole(index)}
                        remove={() => removeRole(index)}
                      /> :
                      <FormRoleControlButtons
                        loading={loading}
                        action={() => createRole(index)}
                        remove={() => remove(index)}
                      />}

                    <div style={{borderBottom: "1px solid #f0f0f0", marginBottom: "24px"}}/>
                  </React.Fragment>
                )
              })}
              <Form.Item>
                <Button
                  type="dashed"
                  onClick={() => add()}
                  style={{width: "100%"}}
                  loading={loading}
                >
                  <PlusOutlined/> Add
                </Button>
              </Form.Item>
            </div>
          )
        }}
      </Form.List>
    </Form>
  )
}

interface FormRoleControlButtons {
  remove: () => void
  action: () => void
  loading: boolean
}

const FormRoleControlButtons: React.FC<FormRoleControlButtons> = ({remove, action, loading}) => {
  return (
    <Form.Item>
      <Col span={24} style={{display: 'flex', justifyContent: 'space-between'}}>
        <Button
          onClick={() => remove()}
          type="danger"
          loading={loading}
        >
          Remove
        </Button>

        <Button
          onClick={() => action()}
          type="primary"
          loading={loading}
        >
          Edit
        </Button>
      </Col>
    </Form.Item>
  )
}

