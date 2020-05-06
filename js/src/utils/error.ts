import { FormError, FormErrorMap } from "MyTypes";

export const normalizeErrors = (errors: FormError[]): FormErrorMap =>
  errors.reduce((result, error) => {
    result[error.property] = error;
    return result;
  }, {} as FormErrorMap);

export const fillErrors = (fields: string[], error: string): FormErrorMap =>
  fields.reduce((result, field) => {
    result[field] = {
      message: error,
      property: field,
      value: "",
    };
    return result;
  }, {} as FormErrorMap);
