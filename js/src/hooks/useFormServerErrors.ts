import { FormErrorMap } from "MyTypes";
import { useEffect } from "react";
import { FormInstance } from "antd/lib/form";

export const useFormServerErrors = (
  form: FormInstance,
  errors: FormErrorMap,
  fieldsNames: string[],
) => {
  useEffect(() => {
    const fieldsWithErrorsNames = fieldsNames.filter(
      (name) => errors[name] !== undefined,
    );

    if (fieldsWithErrorsNames.length) {
      const fields = fieldsWithErrorsNames.map((name) => ({
        name,
        errors: [errors[name].message || "Error"],
      }));
      form.setFields(fields);
    }
  }, [form, errors, fieldsNames]);
};
